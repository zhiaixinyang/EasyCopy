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
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, "_data desc");

        if (cursor != null) {

            while (cursor.moveToNext()) {
                String fileName = cursor.getString(cursor.getColumnIndex("_data"));
                if (mHandler != null) {
                    mHandler.obtainMessage(0x110, fileName).sendToTarget();
                }
                Intent intent = new Intent(ScreenshotReceiver.ACTION_SCREEN_SHOT_HAS_NEW);
                intent.putExtra(ScreenshotReceiver.EXTRA_SCREEN_SHOT_HAS_NEW_PATH, fileName);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            }
            cursor.close();

        }
    }
}