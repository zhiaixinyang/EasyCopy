package com.mdove.easycopy.utils;

import android.support.annotation.ArrayRes;
import android.support.annotation.StringRes;

import com.mdove.easycopy.App;

/**
 * Created by MDove on 2018/8/23.
 */

public class StringUtil {
    public static String getString(@StringRes int resId) {
        return App.getInstance().getString(resId);
    }

    public static String getString(String stringFormat, Object... values) {
        return String.format(stringFormat, values);
    }

    public static String getString(@StringRes int stringFormatResId, Object... values) {
        return getString(getString(stringFormatResId), values);
    }

    public static String[] getStringArray(@ArrayRes int stringArrayResId) {
        return App.getInstance().getResources().getStringArray(stringArrayResId);
    }
}
