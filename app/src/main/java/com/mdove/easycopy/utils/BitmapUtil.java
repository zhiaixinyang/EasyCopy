package com.mdove.easycopy.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.baidu.ocr.sdk.utils.LogUtil;
import com.mdove.easycopy.config.DebugConfig;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by lixin on 17/7/20.
 */

public class BitmapUtil {

    private final static String CLASS_TAG = "BitmapUtil";

    public static Bitmap cutBitmap(@NonNull byte[] data, int width, int height, int rotation) {
        Bitmap bitmap = decodeToBitmap(data, width, height);
        if (bitmap == null) {
            return null;
        }
        if (DebugConfig.DEBUG) {
            LogUtil.d(CLASS_TAG, "original bitmap size: " + bitmap.getWidth() + " * " + bitmap.getHeight());
        }
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        if (bitmapWidth == width && bitmapHeight == height) {
            return bitmap;
        }
        float ratioX = width / (float) bitmapWidth;
        float ratioY = height / (float) bitmapHeight;
        if (ratioX == ratioY) {

        }
        float ratio = ratioX > ratioY ? ratioX : ratioY;
        int newWidth, newHeight;
        newWidth = Math.round(bitmapWidth * ratio);
        newHeight = Math.round(bitmapHeight * ratio);
        if (DebugConfig.DEBUG) {
            LogUtil.d(CLASS_TAG, "scale bitmap size: " + newWidth + " * " + newHeight);
        }
        bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);

        if (newWidth != width && newHeight != height) {

            if (DebugConfig.DEBUG) {
                LogUtil.d(CLASS_TAG, "scale size exception : " + newWidth + " * " + newHeight);
            }
//            return closeToTargetSize(bitmap, width, height);
        }
        int x = 0;
//        int x = (newWidth - width) / 2;
        int y = (newHeight - height) / 2;
//        int x = 0;
//        int y = 0;
        Matrix matrix = new Matrix();
        matrix.setRotate(rotation);
        bitmap = Bitmap.createBitmap(bitmap, x, y, width, height, matrix, true);

        if (DebugConfig.DEBUG) {
            LogUtil.d(CLASS_TAG, "final bitmap size: " + bitmap.getWidth() + " * " + bitmap.getHeight());
        }
        return bitmap;
    }

    public static boolean saveToFile(Bitmap bitmap, String filePath) {
        FileOutputStream fos = null;
        try {
            File file = new File(filePath);
            if (file.exists()) {
                return false;
            }
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdir();
            }
            fos = new FileOutputStream(filePath);
            return bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            if (DebugConfig.DEBUG) {
                LogUtil.e(CLASS_TAG, "save to file failed " + e.toString());
            }
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private static Bitmap closeToTargetSize(Bitmap bitmap, int width, int height) {
        return bitmap;
    }

    /**
     * 以缩放方式解码图片
     *
     * @param data
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static Bitmap decodeToBitmap(byte[] data, int targetWidth, int targetHeight) {
        if (data == null) return null;
        Bitmap bitmap = null;
        try {
            bitmap = decodeScaledBitmap(data, targetWidth, targetHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap decodeToBitmap(String path, int targetWidth, int targetHeight) {
        if (TextUtils.isEmpty(path)) return null;
        Bitmap bitmap = null;
        try {
            bitmap = decodeScaledBitmap(path, targetWidth, targetHeight);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private static Bitmap decodeScaledBitmap(String path, int targetWidth, int targetHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int outHeight = options.outHeight;
        int outWidth = options.outWidth;

        int inSampleSize = calculateInSampleSize(outWidth, outHeight, targetWidth, targetHeight);
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    private static Bitmap decodeScaledBitmap(byte[] data, int targetWidth, int targetHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        int outHeight = options.outHeight;
        int outWidth = options.outWidth;

        int inSampleSize = calculateInSampleSize(outWidth, outHeight, targetWidth, targetHeight);
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    /**
     * 计算要缩放的倍数
     *
     * @param srcWidth     原始宽度
     * @param srcHeight    原始高度
     * @param targetWidth  目标宽度
     * @param targetHeight 目标高度
     * @return 采样率inSampleSize 2的指数倍
     */
    private static int calculateInSampleSize(int srcWidth, int srcHeight, int targetWidth, int targetHeight) {
        int inSampleSize = 1;
        if (srcHeight > targetHeight || srcWidth > targetWidth) {

            final int halfHeight = srcHeight / 2;
            final int halfWidth = srcWidth / 2;

            // 在保证解析出的bitmap宽高分别大于目标尺寸宽高的前提下，取可能的inSampleSize的最大值
            while ((halfHeight / inSampleSize) > targetHeight
                    && (halfWidth / inSampleSize) > targetWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

}
