package com.gerenvip.filescaner;

import android.content.Context;
import android.text.TextUtils;

import com.gerenvip.filescaner.log.Logger;
import com.gerenvip.filescaner.media.MediaFile;

import java.io.File;
import java.net.URI;
import java.util.HashMap;

/**
 * @author wangwei on 2017/11/2.
 *         wangwei@jiandaola.com
 */
public class FilterUtil {

    public static final String TAG = "FilterUtil";
    private static int MAX_DIR_DEPTH = ScannerConfig.MAX_DIR_DEPTH;
    private static String[] sBlackList = ScannerConfig.BLACK_LIST;
    private static boolean sFilterHiddenDir = true;
    private static boolean sFilterNoMediaDir = true;
    public static final String NOMEDIA = ".nomedia";

    static long sFileSizeFilter = 500 * 1024;// 10M
    static long sMediaTimeLengthFilter = 60 * 1000;//60s

    private static boolean sSupportFilterMediaLength = false;


    public static void setFileSizeFilter(int fileSize) {
        sFileSizeFilter = fileSize;
    }

    public static long getFileSizeFilter() {
        return sFileSizeFilter;
    }

    /**
     * 过滤媒体文件时长
     *
     * @param length 毫秒
     */
    public static void setMediaTimeLengthFilter(long length) {
        sMediaTimeLengthFilter = length;
    }

    /**
     * 媒体文件过滤时长
     *
     * @return 毫秒
     */
    public static long getMediaTimeLengthFilter() {
        return sMediaTimeLengthFilter;
    }

    /**
     * 获取文件夹扫描的最大深度 目前 java 层扫描需要限制深度，防止嵌套过深。JNI 层扫描不做限制
     *
     * @return
     */
    public static int getMaxDirDepth() {
        return MAX_DIR_DEPTH;
    }

    /**
     * 设置文件夹扫描的最大深度（目前 java 层扫描需要限制深度，防止嵌套过深）。JNI 层扫描不做限制
     *
     * @param depth
     */
    public static void setMaxDirDepth(int depth) {
        MAX_DIR_DEPTH = depth;
    }

    /**
     * 是否跳过 对.nomedia 目录的遍历
     *
     * @return
     */
    public static boolean isFilterNoMediaDir() {
        return sFilterNoMediaDir;
    }

    /**
     * 如果true 跳过对 .nomedia 目录的遍历；反之遍历
     *
     * @param filterNoMediaDir
     */
    public static void setFilterNoMediaDir(boolean filterNoMediaDir) {
        sFilterNoMediaDir = filterNoMediaDir;
    }

    /**
     * 是否跳过 对隐藏目录的遍历
     *
     * @return
     */
    public static boolean isFilterHiddenDir() {
        return sFilterHiddenDir;
    }

    /**
     * 如果 true ,跳过对隐藏文件的遍历；反之 遍历
     *
     * @param filterHiddenDir
     */
    public static void setFilterHiddenDir(boolean filterHiddenDir) {
        sFilterHiddenDir = filterHiddenDir;
    }


    public static String[] getBlackList() {
        return sBlackList;
    }

    public static boolean isSupportType(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        if (ScannerConfig.sGlobalAppContext != null) {
            Context context = ScannerConfig.sGlobalAppContext;
            LocalFileCacheManager fileCacheManager = LocalFileCacheManager.getInstance(context);
            int supportType = fileCacheManager.getSupportType();
            switch (supportType) {
                case FileScanner.SUPPORT_FILE_TYPE_LARGEFILE:
                    return true;
                case FileScanner.SUPPORT_FILE_TYPE_AUDIO:
                    return MediaFile.getSingleton().isAudioFileTypeByPath(filePath);
                case FileScanner.SUPPORT_FILE_TYPE_VIDEO:
                    return MediaFile.getSingleton().isVideoFileTypeByPath(filePath);
                case FileScanner.SUPPORT_FILE_TYPE_IMAGE:
                    return MediaFile.getSingleton().isImageFileTypeByPath(filePath);
                case FileScanner.SUPPORT_FILE_TYPE_APK:
                    return isApkFile(filePath);
            }
            return false;
        } else {
            return false;
        }
    }

    private static boolean isApkFile(String filePath) {
        String extension = Util4File.getFileExtension(filePath);
        if (TextUtils.isEmpty(extension)) {
            return false;
        }
        return extension.equalsIgnoreCase("APK");
    }

    public static boolean isSupportFileExtension(String extension) {
        if (TextUtils.isEmpty(extension)) {
            return false;
        }
        if (ScannerConfig.sGlobalAppContext != null) {
            Context context = ScannerConfig.sGlobalAppContext;
            LocalFileCacheManager fileCacheManager = LocalFileCacheManager.getInstance(context);
            int supportType = fileCacheManager.getSupportType();
            switch (supportType) {
                case FileScanner.SUPPORT_FILE_TYPE_LARGEFILE:
                    return true;
                case FileScanner.SUPPORT_FILE_TYPE_AUDIO:
                    return MediaFile.getSingleton().isAudioFileTypeByExtension(extension);
                case FileScanner.SUPPORT_FILE_TYPE_VIDEO:
                    return MediaFile.getSingleton().isVideoFileTypeByExtension(extension);
                case FileScanner.SUPPORT_FILE_TYPE_IMAGE:
                    return MediaFile.getSingleton().isImageFileTypeByExtension(extension);
                case FileScanner.SUPPORT_FILE_TYPE_APK:
                    return extension.equalsIgnoreCase("APK");
            }
            return false;
        } else {
            return false;
        }
    }

    public static boolean isFileSizeSupport(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        return file.length() > sFileSizeFilter;
    }

    public static boolean isFileSizeSupport(long length) {
        Logger.d(TAG, "isFileSizeSupport length=" + length + ";sFileSizeFilter=" + sFileSizeFilter);
        return length > sFileSizeFilter;
    }

    public static boolean isMediaFileTimeLengthSupport(String filePath) {
        if (!sSupportFilterMediaLength) {
            return true;
        }
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        URI uri = file.toURI();
        String durationStr = null;
        android.media.MediaMetadataRetriever mmr = new android.media.MediaMetadataRetriever();
        try {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.4.2; zh-CN; MW-KW-001 Build/JRO03C) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 UCBrowser/1.0.0.001 U4/0.8.0 Mobile Safari/533.1");
            mmr.setDataSource(uri.toString(), headers);
            durationStr = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);//毫秒
        } catch (Exception ex) {
        } finally {
            mmr.release();
        }

        int duration = 0;
        try {
            duration = Integer.parseInt(durationStr);
            if (duration >= sMediaTimeLengthFilter) {
                return true;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        Logger.e(TAG, "duration " + duration);
        return false;
    }

    public static boolean isInBlackList(String str) {
        for (CharSequence contains : sBlackList) {
            if (str.contains(contains)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containNoMediaFile(File file) {
        if (file == null || !file.isDirectory()) {
            return false;
        }
        return new File(file.getAbsolutePath() + NOMEDIA).exists();
    }
}
