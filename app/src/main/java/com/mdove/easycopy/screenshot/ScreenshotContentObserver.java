package com.mdove.easycopy.screenshot;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class ScreenshotContentObserver extends ContentObserver {
    private Context mContext;
    private Handler mHandler;
    private static final String[] MEDIA_PROJECTIONS = {
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.DATE_TAKEN
    };

    public ScreenshotContentObserver(Context context) {
        super(new Handler());
        mContext = context;
    }

    public ScreenshotContentObserver(Context context, Handler handler) {
        super(handler);
        mContext = context;
        mHandler = handler;
    }

    /**
     * 主要在onChange中响应数据库变化，并进行相应处理
     */
    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = mContext.getContentResolver().query(uri, MEDIA_PROJECTIONS, null, null,
                MediaStore.Images.ImageColumns.DATE_ADDED + " desc limit 1");

        if (cursor == null) {
            return;
        }

        if (!cursor.moveToFirst()) {
            return;
        }

        int dataIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        int dateTakenIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN);
        String data = cursor.getString(dataIndex);
        long dateTaken = cursor.getLong(dateTakenIndex);

        if (mHandler != null) {
            mHandler.obtainMessage(0x110, data).sendToTarget();
        }
        Intent intent = new Intent(ScreenshotReceiver.ACTION_SCREEN_SHOT_HAS_NEW);
        intent.putExtra(ScreenshotReceiver.EXTRA_SCREEN_SHOT_HAS_NEW_PATH, data);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        cursor.close();

    }
}