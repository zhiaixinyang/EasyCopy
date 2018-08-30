package com.gerenvip.filescaner;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * @author wangwei on 2017/11/2.
 *         wangwei@jiandaola.com
 */
public class ScannerConfig {

    public static boolean DEBUG = BuildConfig.DEBUG;
    public static Context sGlobalAppContext;
    public static final String[] BLACK_LIST = new String[]{};

    public static int MAX_DIR_DEPTH = 20;

    public static long DEFAULT_AUDIO_FILE_SIZE_THRESHOLD = 500 * 1024;//500K
    public static long DEFAULT_VIDEO_FILE_SIZE_THRESHOLD = 500 * 1024;//500K
    public static long DEFAULT_IMG_FILE_SIZE_THRESHOLD = 50 * 1024;//50K
    public static long DEFAULT_LARGE_FILE_FILE_SIZE_THRESHOLD = 10 * 1024 * 1024;//10MB
    public static long DEFAULT_APK_FILE_FILE_SIZE_THRESHOLD = 0;//0MB

    private static final String PREF_FILE = "files_canner";
    private static final String PREF_KEY_PREFIX = "type_";

    /**
     * 标记本次扫描被强制停止
     *
     * @param context
     * @param type
     * @param foreStop
     */
    public static void setScannerForeStop(@NonNull Context context, @FileScanner.SupportFileType int type, boolean foreStop) {
        SharedPreferences sp = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        sp.edit().putBoolean(PREF_KEY_PREFIX + type, foreStop).apply();
    }

    /**
     * 上一次扫描是否被强制停止
     *
     * @param context
     * @param type
     * @return
     */
    public static boolean isForeStoped(@NonNull Context context, @FileScanner.SupportFileType int type) {
        SharedPreferences sp = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        return sp.getBoolean(PREF_KEY_PREFIX + type, false);
    }
}
