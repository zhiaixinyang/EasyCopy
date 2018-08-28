package com.mdove.easycopy.screenshot;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

public class ScreenshotObserverService extends Service {
    public static final String ACTION_START_SERVICE = "action_start_service";
    public static final String ACTION_STOP_SERVICE = "action_stop_service";
    private ScreenshotContentObserver mScreenObserver;
    private ScreenshotReceiver mReceiver;

    public static void start(Context context) {
        Intent intent = new Intent(context, ScreenshotObserverService.class);
        intent.setAction(ACTION_START_SERVICE);
        context.startService(intent);
    }

    public static void stop(Context context) {
        Intent intent = new Intent(context, ScreenshotObserverService.class);
        intent.setAction(ACTION_STOP_SERVICE);
        context.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        listenScreenshot();
        registerReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (!TextUtils.isEmpty(action)) {
            switch (action) {
                case ACTION_START_SERVICE: {
                    break;
                }
                case ACTION_STOP_SERVICE: {
                    ScreenshotObserverService.this.stopSelf();
                    break;
                }
                default: {
                    break;
                }
            }
        }
        return START_STICKY;
    }

    private void registerReceiver() {
        if (mReceiver == null) {
            mReceiver = new ScreenshotReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ScreenshotReceiver.ACTION_SCREEN_SHOT_HAS_NEW);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, filter);
    }

    private void listenScreenshot() {
        mScreenObserver = new ScreenshotContentObserver(this);
        registerContentObserver();
    }

    private void registerContentObserver() {
        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        getContentResolver().registerContentObserver(imageUri, false, mScreenObserver);
    }
}
