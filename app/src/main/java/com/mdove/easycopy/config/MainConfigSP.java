package com.mdove.easycopy.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.mdove.easycopy.App;

public class MainConfigSP implements IMainConfigKey {
    private static final String PREFS_FILE = "easy_copy_prefs";
    private static SharedPreferences sPrefs;

    private static SharedPreferences initSharedPreferences() {
        if (sPrefs == null) {
            sPrefs = App.getContext().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        }
        return sPrefs;
    }

    public static void setBaiduOcrToken(String token) {
        SharedPreferences.Editor editor = initSharedPreferences().edit();
        editor.putString(MAIN_CONFIG_KEY_BAIDU_OCR_TOKEN, token);
        editor.apply();
    }

    public static String getBaiduOcrToken() {
        SharedPreferences preferences = initSharedPreferences();
        return preferences.getString(MAIN_CONFIG_KEY_BAIDU_OCR_TOKEN, "");
    }

    public static void setIsOpenFloatView(boolean isOpen) {
        SharedPreferences.Editor editor = initSharedPreferences().edit();
        editor.putBoolean(MAIN_CONFIG_KEY_IS_OPEN_FLOAT, isOpen);
        editor.apply();
    }

    public static boolean isOpenFloatView() {
        SharedPreferences preferences = initSharedPreferences();
        return preferences.getBoolean(MAIN_CONFIG_KEY_IS_OPEN_FLOAT, true);
    }

    public static void setIsScreenShotSelect(boolean isSelect) {
        SharedPreferences.Editor editor = initSharedPreferences().edit();
        editor.putBoolean(KEY_SCREEN_SHOT_SELECT, isSelect);
        editor.apply();
    }

    public static boolean isScreenShotSelect() {
        SharedPreferences preferences = initSharedPreferences();
        return preferences.getBoolean(KEY_SCREEN_SHOT_SELECT, false);
    }

    public static void setIsScreenShotSilentSelect(boolean isSelect) {
        SharedPreferences.Editor editor = initSharedPreferences().edit();
        editor.putBoolean(KEY_SCREEN_SHOT_SILENT_SELECT, isSelect);
        editor.apply();
    }

    public static boolean isScreenShotSilentSelect() {
        SharedPreferences preferences = initSharedPreferences();
        return preferences.getBoolean(KEY_SCREEN_SHOT_SILENT_SELECT, false);
    }

    public static void setAppOrderTodayTime(long time) {
        SharedPreferences.Editor editor = initSharedPreferences().edit();
        editor.putLong(KEY_ORDER_TODAY_TIME, time);
        editor.apply();
    }

    public static long getAppOrderTodayTime() {
        SharedPreferences preferences = initSharedPreferences();
        return preferences.getLong(KEY_ORDER_TODAY_TIME, 0);
    }

    public static void addOCRWordsCount(long addCount) {
        long count = getOCRWordsCount();
        SharedPreferences.Editor editor = initSharedPreferences().edit();
        editor.putLong(KEY_OCR_WORDS_COUNT, count + addCount);
        editor.apply();
    }

    public static long getOCRWordsCount() {
        SharedPreferences preferences = initSharedPreferences();
        return preferences.getLong(KEY_OCR_WORDS_COUNT, 0);
    }

    public static void addOCRCount() {
        long count = getOCRCount();
        SharedPreferences.Editor editor = initSharedPreferences().edit();
        editor.putLong(KEY_OCR_COUNT, count+1);
        editor.apply();
    }

    public static long getOCRCount() {
        SharedPreferences preferences = initSharedPreferences();
        return preferences.getLong(KEY_OCR_COUNT, 0);
    }

    public static void addOCRSucCount() {
        long count = getOCRSucCount();
        SharedPreferences.Editor editor = initSharedPreferences().edit();
        editor.putLong(KEY_OCR_SUC_COUNT, count+1);
        editor.apply();
    }

    public static long getOCRSucCount() {
        SharedPreferences preferences = initSharedPreferences();
        return preferences.getLong(KEY_OCR_SUC_COUNT, 0);
    }
}
