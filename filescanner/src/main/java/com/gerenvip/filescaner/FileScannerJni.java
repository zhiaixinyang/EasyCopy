package com.gerenvip.filescaner;

import java.util.ArrayList;

/**
 * @author wangwei on 2017/7/18.
 *         wangwei@jiandaola.com
 */
public class FileScannerJni {
    public static final String TAG = "FileScannerJni";
    private static boolean mLoadSuccess;

    static {
        try {
            System.loadLibrary("FileScanner");
            mLoadSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
            mLoadSuccess = false;
            // Log.e(TAG, "static initializer: loadLibrary error!!!" + e.getMessage());
        } catch (Error error) {
            error.printStackTrace();
            mLoadSuccess = false;
        }

    }


    public static boolean isLoadJNISuccess() {
        return mLoadSuccess;
    }

    /**
     * call from jni
     *
     * @param fileSize
     * @return
     */
    public static boolean isFileSizeSupport(long fileSize) {
        return FilterUtil.isFileSizeSupport(fileSize);
    }

    /**
     * call from jni
     *
     * @param extension 参数 extension 带. 例如 .mp3
     * @return
     */
    public static boolean isFileExtensionSupport(String extension) {
        //extension 带 . 例如 .mp3 所以可以使用文件路径判断
        return FilterUtil.isSupportType(extension);
    }

    /**
     * @param paramString
     * @param filterHiddenDir 是否扫描隐藏目录
     * @return
     */
    public static native ArrayList<FileInfo> scanDirs(String paramString, boolean filterHiddenDir);

    public static native ArrayList<FileInfo> scanFiles(String filePath);

    public static native long getFileLastModifiedTime(String filePath);

    public static native ArrayList<FileInfo> scanUpdateDirs(String filePath);
}
