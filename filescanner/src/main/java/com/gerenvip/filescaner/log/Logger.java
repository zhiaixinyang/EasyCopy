package com.gerenvip.filescaner.log;

import android.util.Log;

import com.gerenvip.filescaner.ScannerConfig;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author wangwei on 2017/10/25.
 *         wangwei@jiandaola.com
 */
public class Logger {
    public static final String TAG = "CleanerEngine";
    public static boolean DEBUG = ScannerConfig.DEBUG;

    public static void v(String subTag, String msg) {
        if (DEBUG) Log.v(TAG, getLogMsg(subTag, msg));
    }

    public static void d(String subTag, String msg) {
        if (DEBUG) Log.d(TAG, getLogMsg(subTag, msg));
    }

    public static void i(String subTag, String msg) {
        if (DEBUG) Log.i(TAG, getLogMsg(subTag, msg));
    }

    public static void w(String subTag, String msg) {
        if (DEBUG) Log.w(TAG, getLogMsg(subTag, msg));
    }

    public static void w(String subTag, String msg, Throwable e) {
        if (DEBUG) Log.w(TAG, getLogMsg(subTag, msg + " Exception: " + getExceptionMsg(e)));
    }

    public static void e(String subTag, String msg) {
        if (DEBUG) Log.e(TAG, getLogMsg(subTag, msg));
    }

    public static void e(String subTag, String msg, Throwable e) {
        if (DEBUG) Log.e(TAG, getLogMsg(subTag, msg + " Exception: " + getExceptionMsg(e)));
    }

    private static String getLogMsg(String subTag, String msg) {
        return "[" + subTag + "] " + msg;
    }

    private static String getExceptionMsg(Throwable e) {
        StringWriter sw = new StringWriter(1024);
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.close();
        return sw.toString();
    }
}
