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

import com.mdove.easycopy.App;
import com.mdove.easycopy.R;
import com.mdove.easycopy.greendao.ResultOCRDao;
import com.mdove.easycopy.greendao.entity.ResultOCR;
import com.mdove.easycopy.ocr.baiduocr.PreOcrManager;
import com.mdove.easycopy.ocr.baiduocr.model.RecognizeResultModel;
import com.mdove.easycopy.ocr.baiduocr.utils.ResultOCRHelper;
import com.mdove.easycopy.activity.resultocr.model.ResultOCRModel;
import com.mdove.easycopy.utils.ClipboardUtils;
import com.mdove.easycopy.utils.StringUtil;
import com.mdove.easycopy.utils.ToastHelper;

public class ScreenshotObserverService extends Service {
    public static final String ACTION_START_SERVICE = "action_start_service";
    public static final String ACTION_START_SERVICE_SILENT_OCR = "action_start_service_silent_ocr";
    public static final String EXTRA_SERVICE_SILENT_OCR = "extra_service_silent_ocr";
    public static final String ACTION_STOP_SERVICE = "action_stop_service";
    private ScreenshotContentObserver mScreenObserver;
    private ScreenshotReceiver mReceiver;
    private ResultOCRDao mResultOCRDao;

    public static void start(Context context) {
        Intent intent = new Intent(context, ScreenshotObserverService.class);
        intent.setAction(ACTION_START_SERVICE);
        context.startService(intent);
    }

    public static void startSilent(Context context, String path) {
        Intent intent = new Intent(context, ScreenshotObserverService.class);
        intent.setAction(ACTION_START_SERVICE_SILENT_OCR);
        intent.putExtra(EXTRA_SERVICE_SILENT_OCR, path);
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
        mResultOCRDao = App.getDaoSession().getResultOCRDao();
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
                case ACTION_START_SERVICE_SILENT_OCR: {
                    final String path = intent.getStringExtra(EXTRA_SERVICE_SILENT_OCR);
                    if (TextUtils.isEmpty(path)) {
                        break;
                    }
                    PreOcrManager.baiduOcrFromPath(this, path, new PreOcrManager.RecognizeResultListener() {
                        @Override
                        public void onRecognizeResult(RecognizeResultModel model) {
                            boolean isCopy = true;
                            String content = ResultOCRHelper.getStringFromModel(model);

                            if (TextUtils.isEmpty(content)) {
                                isCopy = false;
                                content = "很抱歉,此图片无法识别并提取出文字。";
                            }
                            ResultOCRModel realModel = new ResultOCRModel(content, path);

                            ResultOCR resultOCR = new ResultOCR();
                            resultOCR.mResultOCR = content;
                            resultOCR.mResultOCRTime = System.currentTimeMillis();
                            resultOCR.mPath = path;
                            mResultOCRDao.insert(resultOCR);

                            if (isCopy) {
                                ClipboardUtils.copyToClipboard(ScreenshotObserverService.this, content);
                                ToastHelper.shortToast(StringUtil.getString(R.string.string_silent_ocr_suc));
                            }
                        }

                    });
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
