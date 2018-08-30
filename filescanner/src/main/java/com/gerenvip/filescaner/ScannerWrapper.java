package com.gerenvip.filescaner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangwei on 2017/7/18.
 *         wangwei@jiandaola.com
 */
public class ScannerWrapper {
    public static final String TAG = "ScannerWrapper";

    /**
     * 递归扫描
     *
     * @param absolutePath
     * @return
     */
    public static List<FileInfo> scanDirs(String absolutePath) {
        List<FileInfo> result = new ArrayList<>();
        if (FileScannerJni.isLoadJNISuccess()) {
            boolean filterHiddenDir = FilterUtil.isFilterHiddenDir();
            result = FileScannerJni.scanDirs(absolutePath, filterHiddenDir);
        } else {
            result = FileScannerJava.scanDirs(absolutePath);
        }
        return result;
    }

    public static List<FileInfo> scanFiles(String filePath) {
        List<FileInfo> result = new ArrayList<>();
        if (FilterUtil.isInBlackList(filePath)) {
            return result;
        }
        if (FileScannerJni.isLoadJNISuccess()) {
            result = FileScannerJni.scanFiles(filePath);
        } else {
            result = FileScannerJava.scanFiles(filePath);
        }
        return result;
    }

    public static long getFileLastModifiedTime(String filePath) {
        if (FileScannerJni.isLoadJNISuccess()) {
            return FileScannerJni.getFileLastModifiedTime(filePath);
        } else {
            return FileScannerJava.getFileLastModifiedTime(filePath);
        }

    }

    /**
     * 扫描目录中的文件夹(不扫描子目录),不执行递归扫描
     *
     * @param filePath
     * @return
     */
    public static List<FileInfo> scanUpdateDirs(String filePath) {
        List<FileInfo> result = new ArrayList<>();
        if (FileScannerJni.isLoadJNISuccess()) {
            result = FileScannerJni.scanUpdateDirs(filePath);
        } else {
            result = FileScannerJava.scanUpdateDirs(filePath);
        }
        return result;
    }
}
