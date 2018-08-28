package com.mdove.easycopy.screenshot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.mdove.easycopy.resultocr.ResultOCRActivity;
import com.mdove.easycopy.ui.floatview.service.BallWidgetService;

public class ScreenshotReceiver extends BroadcastReceiver {
    public static final String ACTION_SCREEN_SHOT_HAS_NEW = "action_screen_shot_has_new";
    public static final String EXTRA_SCREEN_SHOT_HAS_NEW_PATH = "extra_screen_shot_has_new_path";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            return;
        }

        switch (action) {
            case ACTION_SCREEN_SHOT_HAS_NEW: {
                String path = intent.getStringExtra(EXTRA_SCREEN_SHOT_HAS_NEW_PATH);
                if (TextUtils.isEmpty(path) || !BallWidgetService.isNeedOCR) {
                    break;
                }
                ResultOCRActivity.start(context, path, ResultOCRActivity.INTENT_TYPE_START_OCR);
                break;
            }
            default: {
                break;
            }
        }
    }
}
