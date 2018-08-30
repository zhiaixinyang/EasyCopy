package com.gerenvip.filescaner;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gerenvip.filescaner.log.Logger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * 文件扫描入口类
 *
 * @author wangwei on 2017/11/2.
 *         wangwei@jiandaola.com
 */
public class FileScanner {
    public static final String TAG = "FileScanner";

    /**
     * 大文件
     */
    public static final int SUPPORT_FILE_TYPE_LARGEFILE = 1;
    /**
     * 音频文件
     */
    public static final int SUPPORT_FILE_TYPE_AUDIO = 2;
    /**
     * 视频文件
     */
    public static final int SUPPORT_FILE_TYPE_VIDEO = 3;
    /**
     * 图片
     */
    public static final int SUPPORT_FILE_TYPE_IMAGE = 4;

    /**
     * Apk
     */
    public static final int SUPPORT_FILE_TYPE_APK = 5;

    private IFileScanFilter mFilter;

    /**
     * 每一种支持类型都会独立拥有一个数据库表
     */
    @IntDef(value = {SUPPORT_FILE_TYPE_LARGEFILE, SUPPORT_FILE_TYPE_AUDIO, SUPPORT_FILE_TYPE_VIDEO, SUPPORT_FILE_TYPE_IMAGE, SUPPORT_FILE_TYPE_APK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SupportFileType {
    }

    public static final int SCANNER_TYPE_ADD = 1;
    public static final int SCANNER_TYPE_DEL = 2;

    @IntDef(value = {SCANNER_TYPE_ADD, SCANNER_TYPE_DEL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ScannerType {
    }

    private Context mContext;
    @SupportFileType
    private int mCurrentType = SUPPORT_FILE_TYPE_LARGEFILE;

    private FileScannerListener mScanListener;

    public FileScanner(@NonNull Context context, @SupportFileType int supportType) {
        this(context, supportType, null);
    }

    public FileScanner(@NonNull Context context, @SupportFileType int supportType, @Nullable FileScannerListener listener) {
        mContext = context;
        ScannerConfig.sGlobalAppContext = context.getApplicationContext();
        mCurrentType = supportType;
        mScanListener = listener;
        initType();
    }

    private void initType() {
        switch (mCurrentType) {
            case SUPPORT_FILE_TYPE_LARGEFILE:
                FilterUtil.sFileSizeFilter = ScannerConfig.DEFAULT_LARGE_FILE_FILE_SIZE_THRESHOLD;
                break;
            case SUPPORT_FILE_TYPE_AUDIO:
                FilterUtil.sFileSizeFilter = ScannerConfig.DEFAULT_AUDIO_FILE_SIZE_THRESHOLD;
                break;
            case SUPPORT_FILE_TYPE_VIDEO:
                FilterUtil.sFileSizeFilter = ScannerConfig.DEFAULT_VIDEO_FILE_SIZE_THRESHOLD;
                break;
            case SUPPORT_FILE_TYPE_IMAGE:
                FilterUtil.sFileSizeFilter = ScannerConfig.DEFAULT_IMG_FILE_SIZE_THRESHOLD;
                break;
            case SUPPORT_FILE_TYPE_APK:
                FilterUtil.sFileSizeFilter = ScannerConfig.DEFAULT_APK_FILE_FILE_SIZE_THRESHOLD;
                break;
        }
    }

    public void setFileScannerListener(FileScannerListener listener) {
        mScanListener = listener;
    }

    /**
     * 设置扫描过滤器。注意 只有调用{@link #scan(boolean)}}后 扫描拦截器才生效
     *
     * @param filter
     */
    public void setScanFilter(IFileScanFilter filter) {
        mFilter = filter;
    }

    public void setFileFilterSizeThreshold(long threshold) {
        FilterUtil.sFileSizeFilter = threshold;
    }

    /**
     * 设置是否跳过对隐藏文件夹的扫描
     *
     * @param filterHidden
     */
    public void setFilterHiddenDir(boolean filterHidden) {
        FilterUtil.setFilterHiddenDir(filterHidden);
    }

    public boolean isScanning() {
        return LocalFileCacheManager.getInstance(mContext).getScanStatus() == LocalFileCacheManager.STATUS_SCAN_ING;
    }

    public void stop() {
        LocalFileCacheManager.getInstance(mContext).stop();
        mFilter = null;
    }

    public void clear() {
        LocalFileCacheManager.getInstance(mContext).clear();
    }

    public void scan(boolean scanAll) {
        if (isScanning()) {
            //停止之前的
            stop();
        }
        //设置目标数据类型
        LocalFileCacheManager.getInstance(mContext).setSupportType(mCurrentType);
        //设置回调
        LocalFileCacheManager.getInstance(mContext).setCommonListener(mCommonListener);
        LocalFileCacheManager.getInstance(mContext).setFilter(mFilter);

        boolean bool = isNeedToScannerAll();
        if (bool || scanAll) {
            //全盘扫描
            Logger.i(TAG, "start: 全盘扫描");
            ScannerUtil.scanAllDirAsync(mContext);
        } else {
            //增量扫描
            Logger.i(TAG, "start: 增量扫描");
            ScannerUtil.updateAllDirAsync(mContext);
        }
    }

    /**
     * 获取该类型的所有文件
     *
     * @return
     */
    public List<FileInfo> getAllFiles() {
        return LocalFileCacheManager.getInstance(mContext).getAllFiles(mCurrentType);
    }

    private boolean isNeedToScannerAll() {
        return ScannerUtil.isNeedToScannerAll(mContext);
    }

    private FileScannerListener mCommonListener = new FileScannerListener() {

        @Override
        public void onScanBegin() {
            if (mScanListener != null) {
                mScanListener.onScanBegin();
            }
        }

        @Override
        public void onScanEnd(List<FileInfo> allFiles) {
            if (mScanListener != null) {
                mScanListener.onScanEnd(getAllFiles());
            }
        }

        @Override
        public void onScanning(String dirPath, int progress) {
            if (mScanListener != null) {
                mScanListener.onScanning(dirPath, progress);
            }
        }

        @Override
        public void onScanningChangedFile(FileInfo info, int type) {
            if (mScanListener != null) {
                mScanListener.onScanningChangedFile(info, type);
            }
        }

        @Override
        public void onScanCancel() {
            if (mScanListener != null) {
                mScanListener.onScanCancel();
            }
        }
    };

}
