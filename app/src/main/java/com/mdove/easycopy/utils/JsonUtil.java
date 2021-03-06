package com.mdove.easycopy.utils;

import android.text.TextUtils;

import com.baidu.ocr.sdk.utils.LogUtil;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Reader;
import java.lang.reflect.Type;

/**
 * Created by MDove on 2016/10/25.
 */

public class JsonUtil {
    private static final String CLASS_TAG = JsonUtil.class.getSimpleName();
    private static final Gson sGson;
    private static final Gson sGsonDisableHtmlEscaping;

    static {
        sGson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .serializeNulls()
                .setDateFormat("yyyy-MM-dd HH:mm:ss") // 时间转换为特定格式
                .create();
        sGsonDisableHtmlEscaping = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .serializeNulls()
                .disableHtmlEscaping()
                .setDateFormat("yyyy-MM-dd HH:mm:ss") // 时间转换为特定格式
                .create();
    }

    public static Gson getGson() {
        return sGson;
    }

    public synchronized static String encode(Object obj) {
        final String TAG = CLASS_TAG + "-encode";
        String json = null;
        if (obj != null) {
            try {
                json = sGson.toJson(obj);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e(TAG, obj.toString() + "\n" + e.getLocalizedMessage());
            }
        }
        return json;
    }

    public synchronized static String encodeDisableHtmlEscaping(Object obj) {
        final String TAG = CLASS_TAG + "-encodeDisableHtmlEscaping";
        String json = null;
        if (obj != null) {
            try {
                json = sGsonDisableHtmlEscaping.toJson(obj);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e(TAG, obj.toString() + "\n" + e.getLocalizedMessage());
            }
        }
        return json;
    }

    public synchronized static <T> T decode(String json, Type type) {
        final String TAG = CLASS_TAG + "-decode";
        T obj = null;
        if (!TextUtils.isEmpty(json)) {
            try {
                obj = sGson.fromJson(json, type);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e(TAG, json + "\n" + e.getLocalizedMessage());
            }
        }
        return obj;
    }

    public synchronized static <T> T decodeDisableHtmlEscaping(String json, Type type) {
        final String TAG = CLASS_TAG + "-decodeDisableHtmlEscaping";
        T obj = null;
        if (!TextUtils.isEmpty(json)) {
            try {
                obj = sGsonDisableHtmlEscaping.fromJson(json, type);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e(TAG, json + "\n" + e.getLocalizedMessage());
            }
        }
        return obj;
    }

    public synchronized static <T> T decode(Reader json, Type type) {
        final String TAG = CLASS_TAG + "-decode-r";
        T obj = null;
        if (json != null) {
            try {
                obj = sGson.fromJson(json, type);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e(TAG, json.toString() + "\n" + e.getLocalizedMessage());
            }
        }
        return obj;
    }
}
