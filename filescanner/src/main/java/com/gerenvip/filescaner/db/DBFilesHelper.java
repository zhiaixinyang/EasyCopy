package com.gerenvip.filescaner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.text.TextUtils;

import com.gerenvip.filescaner.FileInfo;
import com.gerenvip.filescaner.FileScanner;
import com.gerenvip.filescaner.FileScannerListener;
import com.gerenvip.filescaner.FilterUtil;
import com.gerenvip.filescaner.IFileScanFilter;
import com.gerenvip.filescaner.LocalFileCacheManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangwei on 2017/11/2.
 *         wangwei@jiandaola.com
 */
public class DBFilesHelper {
    private Context mContext;

    public DBFilesHelper(Context context) {
        this.mContext = context;
    }

    private static String deleteTableSql(@FileScanner.SupportFileType int type) {
        switch (type) {
            case FileScanner.SUPPORT_FILE_TYPE_LARGEFILE:
                return "DROP TABLE IF EXISTS " + FilesDBContract.Table.TABLE_NAME_LARGE_FILE;
            case FileScanner.SUPPORT_FILE_TYPE_AUDIO:
                return "DROP TABLE IF EXISTS " + FilesDBContract.Table.TABLE_NAME_AUDIO;
            case FileScanner.SUPPORT_FILE_TYPE_VIDEO:
                return "DROP TABLE IF EXISTS " + FilesDBContract.Table.TABLE_NAME_VIDEO;
            case FileScanner.SUPPORT_FILE_TYPE_IMAGE:
                return "DROP TABLE IF EXISTS " + FilesDBContract.Table.TABLE_NAME_IMG;
            case FileScanner.SUPPORT_FILE_TYPE_APK:
                return "DROP TABLE IF EXISTS " + FilesDBContract.Table.TABLE_NAME_APK;
            default:
                return null;
        }
    }

    private static String createTableSql(@FileScanner.SupportFileType int type) {
        @FilesDBContract.Table.TableName
        String tableName = null;
        switch (type) {
            case FileScanner.SUPPORT_FILE_TYPE_LARGEFILE:
                tableName = FilesDBContract.Table.TABLE_NAME_LARGE_FILE;
                break;
            case FileScanner.SUPPORT_FILE_TYPE_AUDIO:
                tableName = FilesDBContract.Table.TABLE_NAME_AUDIO;
                break;
            case FileScanner.SUPPORT_FILE_TYPE_VIDEO:
                tableName = FilesDBContract.Table.TABLE_NAME_VIDEO;
                break;
            case FileScanner.SUPPORT_FILE_TYPE_IMAGE:
                tableName = FilesDBContract.Table.TABLE_NAME_IMG;
                break;
            case FileScanner.SUPPORT_FILE_TYPE_APK:
                tableName = FilesDBContract.Table.TABLE_NAME_APK;
                break;
            default:
                break;
        }
        if (tableName == null) {
            return null;
        }
        return "CREATE TABLE IF NOT EXISTS " + tableName + "(" +
                FilesDBContract.COLUMN_NAME_DATA + " TEXT NOT NULL PRIMARY KEY," +
                FilesDBContract.COLUMN_NAME_FOLDER_ID + " TEXT," +
                FilesDBContract.COLUMN_NAME_SIZE + " INTEGER," +
                FilesDBContract.COLUMN_NAME_MODIFIED_TIME + " INTEGER)";
    }

    public static void deleteTable(@NonNull SQLiteDatabase db) {
        db.execSQL(deleteTableSql(FileScanner.SUPPORT_FILE_TYPE_LARGEFILE));
        db.execSQL(deleteTableSql(FileScanner.SUPPORT_FILE_TYPE_AUDIO));
        db.execSQL(deleteTableSql(FileScanner.SUPPORT_FILE_TYPE_VIDEO));
        db.execSQL(deleteTableSql(FileScanner.SUPPORT_FILE_TYPE_IMAGE));
        db.execSQL(deleteTableSql(FileScanner.SUPPORT_FILE_TYPE_APK));
    }

    public static void createTable(@NonNull SQLiteDatabase db) {
        db.execSQL(createTableSql(FileScanner.SUPPORT_FILE_TYPE_LARGEFILE));
        db.execSQL(createTableSql(FileScanner.SUPPORT_FILE_TYPE_AUDIO));
        db.execSQL(createTableSql(FileScanner.SUPPORT_FILE_TYPE_VIDEO));
        db.execSQL(createTableSql(FileScanner.SUPPORT_FILE_TYPE_IMAGE));
        db.execSQL(createTableSql(FileScanner.SUPPORT_FILE_TYPE_APK));
    }

    @FilesDBContract.Table.TableName
    @NonNull
    private String getTableName(@FileScanner.SupportFileType int type) {
        switch (type) {
            case FileScanner.SUPPORT_FILE_TYPE_LARGEFILE:
                return FilesDBContract.Table.TABLE_NAME_LARGE_FILE;
            case FileScanner.SUPPORT_FILE_TYPE_AUDIO:
                return FilesDBContract.Table.TABLE_NAME_AUDIO;
            case FileScanner.SUPPORT_FILE_TYPE_VIDEO:
                return FilesDBContract.Table.TABLE_NAME_VIDEO;
            case FileScanner.SUPPORT_FILE_TYPE_IMAGE:
                return FilesDBContract.Table.TABLE_NAME_IMG;
            case FileScanner.SUPPORT_FILE_TYPE_APK:
                return FilesDBContract.Table.TABLE_NAME_APK;
            default:
                throw new IllegalArgumentException("unSupport type");
        }
    }

    /**
     * 将文件数据信息插入到指定类型的表中
     *
     * @param type
     * @param filesArrayList
     * @param folder_id
     * @param mCommonListener
     */
    public void insertNewFiles(@FileScanner.SupportFileType int type, List<FileInfo> filesArrayList, String folder_id, FileScannerListener mCommonListener) {
        DBManager.getWriteDB(mContext).beginTransaction();
        String tableName = getTableName(type);
        try {
            for (FileInfo fileInfo : filesArrayList) {
                if (TextUtils.isEmpty(fileInfo.getFilePath())) {
                    continue;
                }
                if (!FilterUtil.isFileSizeSupport(fileInfo.getFileSize())) {
                    continue;
                }
                if (!FilterUtil.isMediaFileTimeLengthSupport(fileInfo.getFilePath())) {
                    continue;
                }

                //自定义拦截器
                IFileScanFilter filter = LocalFileCacheManager.getInstance(mContext).getFilter();
                if (filter != null) {
                    if (filter.filterFile(fileInfo.getFilePath())) {
                        continue;
                    }
                }
                ContentValues contentValues = new ContentValues();
                if (!TextUtils.isEmpty(fileInfo.getFilePath())) {
                    contentValues.put(FilesDBContract.COLUMN_NAME_DATA, fileInfo.getFilePath());
                }
                contentValues.put(FilesDBContract.COLUMN_NAME_MODIFIED_TIME, fileInfo.getLastModifyTime());
                contentValues.put(FilesDBContract.COLUMN_NAME_SIZE, fileInfo.getFileSize());
                contentValues.put(FilesDBContract.COLUMN_NAME_FOLDER_ID, folder_id);

                DBManager.getWriteDB(mContext).insert(tableName, null, contentValues);
                if (mCommonListener != null) {
                    mCommonListener.onScanningChangedFile(fileInfo, FileScanner.SCANNER_TYPE_ADD);
                }
            }
            DBManager.getWriteDB(mContext).setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.getWriteDB(mContext).endTransaction();
        }
    }

    public void clearTable(@FileScanner.SupportFileType int type) {
        String tableName = getTableName(type);
        DBManager.getWriteDB(mContext).delete(tableName, null, null);
    }

    public void deleteFilesByFolderId(@FileScanner.SupportFileType int type, @NonNull String folderId, @Nullable FileScannerListener mCommonListener) {
        if (mCommonListener != null) {
            List<FileInfo> delList = getFilesByFolderId(type, folderId);
            for (FileInfo fileInfo : delList) {
                mCommonListener.onScanningChangedFile(fileInfo, FileScanner.SCANNER_TYPE_DEL);
            }
        }
        String tableName = getTableName(type);
        String whereClause = FilesDBContract.COLUMN_NAME_FOLDER_ID + "=" + folderId;
        DBManager.getWriteDB(mContext).delete(tableName, whereClause, null);
    }

    public ArrayList<FileInfo> getFilesByFolderId(@FileScanner.SupportFileType int type, @NonNull String folderId) {
        Cursor cursor = null;
        ArrayList<FileInfo> filesList = null;
        String tableName = getTableName(type);
        try {
            cursor = DBManager.getReadDB(mContext).rawQuery("SELECT * FROM " + tableName + " where " + FilesDBContract.COLUMN_NAME_FOLDER_ID + "=" + folderId, null);
            if (cursor == null) {
                return null;
            }
            FileInfo fileInfo;
            filesList = new ArrayList<>();
            while (cursor.moveToNext()) {
                String _data = cursor.getString(cursor.getColumnIndexOrThrow(FilesDBContract.COLUMN_NAME_DATA));
                long size = cursor.getLong(cursor.getColumnIndexOrThrow(FilesDBContract.COLUMN_NAME_SIZE));
                long time = cursor.getLong(cursor.getColumnIndexOrThrow(FilesDBContract.COLUMN_NAME_MODIFIED_TIME));
                fileInfo = new FileInfo();
                fileInfo.setFilePath(_data);
                fileInfo.setLastModifyTime(time);
                fileInfo.setFileSize(size);
                filesList.add(fileInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return filesList;
    }

    public ArrayList<FileInfo> getAllFiles(@FileScanner.SupportFileType int type) {
        Cursor cursor = null;
        ArrayList<FileInfo> filesList = null;
        String tableName = getTableName(type);
        try {
            cursor = DBManager.getReadDB(mContext).rawQuery("SELECT * FROM " + tableName, null);
            if (cursor == null) {
                return null;
            }
            FileInfo fileInfo;
            filesList = new ArrayList<>();
            while (cursor.moveToNext()) {
                String _data = cursor.getString(cursor.getColumnIndexOrThrow(FilesDBContract.COLUMN_NAME_DATA));
                long size = cursor.getLong(cursor.getColumnIndexOrThrow(FilesDBContract.COLUMN_NAME_SIZE));
                long time = cursor.getLong(cursor.getColumnIndexOrThrow(FilesDBContract.COLUMN_NAME_MODIFIED_TIME));
                fileInfo = new FileInfo();
                fileInfo.setFilePath(_data);
                fileInfo.setLastModifyTime(time);
                fileInfo.setFileSize(size);
                filesList.add(fileInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return filesList;
    }

    public void deleteFile(@FileScanner.SupportFileType int type, String filePath) {
        String tableName = getTableName(type);
        try {
            String whereClause = FilesDBContract.COLUMN_NAME_DATA + "=" + "'" + filePath + "'";
            DBManager.getWriteDB(mContext).delete(tableName, whereClause, null);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    private static class FilesDBContract {
        public static class Table {
            /**
             * {@link FileScanner#SUPPORT_FILE_TYPE_LARGEFILE}
             */
            private final static String TABLE_NAME_LARGE_FILE = "large_files";
            /**
             * {@link FileScanner#SUPPORT_FILE_TYPE_AUDIO}
             */
            private final static String TABLE_NAME_AUDIO = "audios";
            /**
             * {@link FileScanner#SUPPORT_FILE_TYPE_VIDEO}
             */
            private final static String TABLE_NAME_VIDEO = "videos";
            /**
             * {@link FileScanner#SUPPORT_FILE_TYPE_IMAGE}
             */
            private final static String TABLE_NAME_IMG = "images";

            /**
             * {@link FileScanner#SUPPORT_FILE_TYPE_APK}
             */
            private final static String TABLE_NAME_APK = "apks";

            @StringDef(value = {TABLE_NAME_LARGE_FILE, TABLE_NAME_AUDIO, TABLE_NAME_VIDEO, TABLE_NAME_IMG, TABLE_NAME_APK})
            @Retention(RetentionPolicy.SOURCE)
            public @interface TableName {
            }
        }

        private final static String COLUMN_NAME_DATA = "_data";
        private final static String COLUMN_NAME_FOLDER_ID = "folder_id";
        private final static String COLUMN_NAME_SIZE = "_size";
        private final static String COLUMN_NAME_MODIFIED_TIME = "modified_time";
    }
}
