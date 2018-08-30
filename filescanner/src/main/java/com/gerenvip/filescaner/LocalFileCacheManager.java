package com.gerenvip.filescaner;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.gerenvip.filescaner.db.DBFilesHelper;
import com.gerenvip.filescaner.db.DBFolderHelper;
import com.gerenvip.filescaner.db.DBManager;
import com.gerenvip.filescaner.log.Logger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangwei on 2017/11/2.
 *         wangwei@jiandaola.com
 */
public class LocalFileCacheManager {
    public static final String TAG = "LocalFileCacheManager";
    private HandlerThread mWorkThread;
    private Handler mWorkHandler;
    private static LocalFileCacheManager instance = null;
    public static final int WHAT_SCAN_ALL = 4;//全盘扫描
    public static final int WHAT_SCAN_UPDATE = 5;//增量扫描
    public static final int WHAT_SCAN_CANCEL = 6;

    public static final int STATUS_SCAN_START = 1;    //扫描开始
    public static final int STATUS_SCAN_ING = 2;  //正在扫描
    public static final int STATUS_SCAN_END = 3;  //扫描结束

    @ScanStatus
    private int mScanStatus = STATUS_SCAN_END;
    @Nullable
    private FileScannerListener mCommonListener;
    private DBFolderHelper mDBFolderHelper;
    private DBFilesHelper mDBFilesHelper;

    private static final int SUPPORT_TYPE_NO_SET = 0;
    @FileScanner.SupportFileType
    private int mSupportType = SUPPORT_TYPE_NO_SET;

    private boolean mStopRequested = false;

    private Context mCxt;
    private IFileScanFilter mFilter;

    @IntDef(value = {STATUS_SCAN_START, STATUS_SCAN_ING, STATUS_SCAN_END})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ScanStatus {
    }

    public static LocalFileCacheManager getInstance(Context context) {
        if (instance == null) {
            synchronized (LocalFileCacheManager.class) {
                if (instance == null) {
                    instance = new LocalFileCacheManager(context);
                }
            }
        }
        return instance;
    }

    private LocalFileCacheManager(Context context) {
        mCxt = context.getApplicationContext();
        DBManager.open(mCxt);    //初始化数据库
        mWorkThread = new HandlerThread("ScanWorker");
        mWorkThread.start();
        mWorkHandler = new WorkHandler(mWorkThread.getLooper());
        mDBFolderHelper = new DBFolderHelper(context);
        mDBFilesHelper = new DBFilesHelper(context);
    }

    /**
     * 扫描被强行取消,不保证立刻停止扫描任务。新的扫描任务也会排队等待前一个任务停止后才执行
     */
    public void stop() {
        if (getScanStatus() != STATUS_SCAN_END) {
            //如果正在处于扫描状态，如果正在处于扫描状态，而强行停止标记处理
            mStopRequested = true;
            ScannerConfig.setScannerForeStop(mCxt, getSupportType(), true);
            //强行将扫描状态置为 STATUS_SCAN_END
            mScanStatus = STATUS_SCAN_END;

            Message message = mWorkHandler.obtainMessage();
            message.what = WHAT_SCAN_CANCEL;
            mWorkHandler.sendMessage(message);

        }
        mFilter = null;
    }

    /**
     * 扫描文件夹并且保存到数据库中
     */
    public void scanDirAndSaveToDb(String path, int scanType) {
        List<FileInfo> localDirsArrayList;
        localDirsArrayList = ScannerWrapper.scanDirs(path);
        if (localDirsArrayList == null || localDirsArrayList.size() == 0) {
            Logger.i(TAG, "scanDirAndSaveToDb: 文件夹扫描为空");
            return;
        }
        for (int i = 0; i < localDirsArrayList.size(); i++) {
            if (mStopRequested) {
                return;
            }
            //filter
            if (mFilter != null) {
                boolean filter = mFilter.filterDir(localDirsArrayList.get(i).getFilePath());
                if (filter) {
                    continue;
                }
            }
            //扫描目录下的文件，获得指定格式的文件列表
            if (scanType == WHAT_SCAN_ALL) {
                taskIng(localDirsArrayList.get(i).getFilePath(), (int) (100 / (float) (localDirsArrayList.size() / 100.00 * 100) * i));
            }
            String folder_id = ScannerUtil.getFolderId(localDirsArrayList.get(i).getFilePath());
            List<FileInfo> filesArrayList = ScannerWrapper.scanFiles(localDirsArrayList.get(i).getFilePath());
            //循环文件列表,文件插入到文件列表中
            mDBFilesHelper.insertNewFiles(getSupportType(), filesArrayList, folder_id, mCommonListener);
            localDirsArrayList.get(i).setCount(filesArrayList.size());
        }
        mDBFolderHelper.insertNewDirs(localDirsArrayList);
    }

    private void taskIng(String filePath, int progress) {
        if (mCommonListener != null) {
            mCommonListener.onScanning(filePath, progress);
        }
    }

    @ScanStatus
    public int getScanStatus() {
        return mScanStatus;
    }

    /**
     * 扫描任务开始
     */
    private void taskBegin() {
        mScanStatus = STATUS_SCAN_START;
        if (mCommonListener != null) {
            mCommonListener.onScanBegin();
        }
    }

    /**
     * 扫描任务结束
     */
    private void taskEnd() {
        DBManager.close();
        mScanStatus = STATUS_SCAN_END;
        ScannerConfig.setScannerForeStop(mCxt, getSupportType(), false);
        if (mCommonListener != null) {
            mCommonListener.onScanEnd(null);
        }

    }


    /**
     * 扫描handler接收到任务
     *
     * @param what
     */
    private void performTask(int what) {
        switch (what) {
            case WHAT_SCAN_ALL://全盘扫描
                taskBegin();
                mScanStatus = STATUS_SCAN_ING;
                mDBFolderHelper.clearTable();
                mDBFilesHelper.clearTable(getSupportType());
                scanDirAndSaveToDb(Environment.getExternalStorageDirectory().getAbsolutePath(), WHAT_SCAN_ALL);
                taskEnd();
                break;
            case WHAT_SCAN_UPDATE:  //增量扫描
                taskBegin();
                mScanStatus = STATUS_SCAN_ING;
                updateFiles();
                taskEnd();
                break;
        }
    }

    /**
     * 增量更新文件
     *
     * @return
     */
    public void updateFiles() {
        ArrayList<FileInfo> dirsList = mDBFolderHelper.getAllFolder();
        ArrayList<FileInfo> deleteDirsList = new ArrayList<>();
        ArrayList<FileInfo> updateDirsList = new ArrayList<>();
        if (dirsList == null || dirsList.size() == 0) {
            return;
        } else {
            for (FileInfo fileInfo : dirsList) {
                if (mStopRequested) {
                    return;
                }
                long modifiedTime = ScannerWrapper.getFileLastModifiedTime(fileInfo.getFilePath());
                if (modifiedTime == -1l) {
                    //找不到文件夹，代表已经删除
                    deleteDirsList.add(fileInfo);
                } else if (modifiedTime != fileInfo.getLastModifyTime()) {
                    //代表最后一次修改时间已经变，需要更新
                    fileInfo.setLastModifyTime(modifiedTime);
                    updateDirsList.add(fileInfo);
                }
            }
            deleteDirList(deleteDirsList);
            updateDirsList(updateDirsList);
        }
    }

    /**
     * 增量更新中更新需要更新的记录
     *
     * @param updateDirsList
     */
    public void updateDirsList(ArrayList<FileInfo> updateDirsList) {
        FileInfo fileInfo;
        for (int i = 0; i < updateDirsList.size(); i++) {
            fileInfo = updateDirsList.get(i);
            taskIng(fileInfo.getFilePath(), (int) (100 / (float) (updateDirsList.size() / 100.00 * 100) * i));
            List<FileInfo> newFilesList = ScannerWrapper.scanFiles(fileInfo.getFilePath());
            //如果扫描目录下没有文件,数据库里的此目录的数量大于0，需要删除数据库里的文件
            if (newFilesList == null || newFilesList.size() == 0) {
                if (fileInfo.getCount() > 0) {
                    mDBFilesHelper.deleteFilesByFolderId(getSupportType(), ScannerUtil.getFolderId(fileInfo.getFilePath()), mCommonListener);
                }
            } else {
                fileInfo.setCount(newFilesList.size());
                //如果扫描目录中有文件，需要判断数据库中此目录的文件数量是否大于0，如果大于0，先删除，在添加
                if (fileInfo.getCount() == 0) {
                    mDBFilesHelper.insertNewFiles(getSupportType(), newFilesList, ScannerUtil.getFolderId(fileInfo.getFilePath()), mCommonListener);
                } else {
                    List<FileInfo> dirList = mDBFilesHelper.getFilesByFolderId(getSupportType(), ScannerUtil.getFolderId(fileInfo.getFilePath()));
                    for (int i1 = 0; i1 < dirList.size(); i1++) {
                        boolean flag = false;
                        for (int i2 = 0; i2 < newFilesList.size(); i2++) {
                            if (dirList.get(i1).getFilePath().equals(newFilesList.get(i2).getFilePath())) {
                                flag = true;
                                newFilesList.remove(i2);
                                break;
                            }
                        }
                        if (!flag) {
                            //删除数据表中的数据
                            mDBFilesHelper.deleteFile(getSupportType(), dirList.get(i1).getFilePath());
                            if (mCommonListener != null) {
                                mCommonListener.onScanningChangedFile(dirList.get(i1), FileScanner.SCANNER_TYPE_DEL);
                            }
                        }
                    }
                    if (newFilesList.size() > 0) {
                        mDBFilesHelper.insertNewFiles(getSupportType(), newFilesList, ScannerUtil.getFolderId(fileInfo.getFilePath()), mCommonListener);
                    }
                }
            }
            //扫描目录中的文件夹(不扫描子目录)
            List<FileInfo> newDirsList = ScannerWrapper.scanUpdateDirs(fileInfo.getFilePath());
            if (newDirsList == null || newDirsList.size() == 0) {
                //如果目录中没有文件夹，则把数据库中目录的记录删除
                deleteDirList(newDirsList);
            } else {
                for (FileInfo info : newDirsList) {
                    //查询数据库中此目录是否存在，如果不存在就添加
                    if (!mDBFolderHelper.isFolderByPath(info.getFilePath())) {
                        //如果数据表中不存在，就扫描并且插入到目录中
                        scanDirAndSaveToDb(info.getFilePath(), WHAT_SCAN_UPDATE);
                    }
                }
            }
            //需要更新目录表中次目录的count数量和最后一次更改时间
            mDBFolderHelper.updateFolder(fileInfo);
        }

    }

    /**
     * 增量更新中删除需要删除的记录
     *
     * @param deleteDirsList
     */
    private void deleteDirList(List<FileInfo> deleteDirsList) {
        if (deleteDirsList.size() > 0) {
            for (FileInfo fileInfo : deleteDirsList) {
                if (fileInfo.getCount() > 0) {
                    mDBFilesHelper.deleteFilesByFolderId(getSupportType(), ScannerUtil.getFolderId(fileInfo.getFilePath()), mCommonListener);
                }
            }
            mDBFolderHelper.deleteFolder(deleteDirsList);
        }
    }

    public void setFilter(IFileScanFilter filter) {
        mFilter = filter;
    }

    public IFileScanFilter getFilter() {
        return mFilter;
    }

    public void setCommonListener(@Nullable FileScannerListener mScannerListener) {
        this.mCommonListener = mScannerListener;
    }

    public void setSupportType(@FileScanner.SupportFileType int type) {
        mSupportType = type;
    }

    public int getSupportType() {
        if (mSupportType == SUPPORT_TYPE_NO_SET) {
            throw new RuntimeException("you must call LocalFileCacheManager.setSupportType() ");
        }
        return mSupportType;
    }

    /**
     * 是否需要全盘扫描
     *
     * @return
     */
    public boolean isNeedToScannerAll() {
        if (mDBFolderHelper.getFolderCount() <= 1 || ScannerConfig.isForeStoped(mCxt, getSupportType())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 开始执行增量扫描
     */
    public void startUpdate() {
        Logger.i(TAG, "startUpdate: ");
        if (mScanStatus == STATUS_SCAN_ING) {
            Logger.i(TAG, "startAllScan:  isScanning");
            return;
        }
        Message message = mWorkHandler.obtainMessage();
        message.what = WHAT_SCAN_UPDATE;
        mWorkHandler.sendMessage(message);
    }

    /**
     * 开始执行全盘扫描
     */
    public void startAllScan() {
        Logger.i(TAG, "startAllScan:");
        if (mScanStatus == STATUS_SCAN_ING) {
            Logger.i(TAG, "startAllScan:  isScanning");
            return;
        }
        Message message = mWorkHandler.obtainMessage();
        message.what = WHAT_SCAN_ALL;
        mWorkHandler.sendMessage(message);
    }

    public void clear() {
        if (mScanStatus == STATUS_SCAN_ING) {
            return;
        }
        mDBFolderHelper.clearTable();
        mDBFilesHelper.clearTable(getSupportType());
    }

    public ArrayList<FileInfo> getAllFiles(@FileScanner.SupportFileType int type) {
        return mDBFilesHelper.getAllFiles(type);
    }

    /**
     * 扫描handler
     */
    private class WorkHandler extends Handler {

        public WorkHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == WHAT_SCAN_CANCEL) {
                if (mCommonListener != null) {
                    mCommonListener.onScanCancel();
                }
                return;
            }
            if (mStopRequested) {
                //为了下一次扫描任务能正常执行
                mStopRequested = false;
                return;
            }
            performTask(msg.what);
        }
    }
}
