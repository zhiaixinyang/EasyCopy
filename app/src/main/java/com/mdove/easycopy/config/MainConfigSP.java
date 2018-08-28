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

    public static void setAppOrderTodayTime(long time) {
        SharedPreferences.Editor editor = initSharedPreferences().edit();
        editor.putLong(KEY_ORDER_TODAY_TIME, time);
        editor.apply();
    }

    public static long getAppOrderTodayTime() {
        SharedPreferences preferences = initSharedPreferences();
        return preferences.getLong(KEY_ORDER_TODAY_TIME, 0);
    }
}
