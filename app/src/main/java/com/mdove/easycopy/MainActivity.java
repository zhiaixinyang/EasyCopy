package com.mdove.easycopy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.mdove.easycopy.ui.floatview.FloatWeatherWidget;
import com.mdove.easycopy.ui.floatview.WidgetBall;
import com.mdove.easycopy.utils.ToastHelper;
import com.mdove.easycopy.utils.permission.PermissionUtils;

public class MainActivity extends AppCompatActivity {
    private static final int OVERLAY_PERMISSION_REQUEST_CODE = 100;
    private FloatWeatherWidget mWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWidget = new FloatWeatherWidget(this, new WidgetBall(this), null);
        findViewById(R.id.btn_float).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initBall();
            }
        });
    }

    private void initBall() {
        boolean overlaysPermission = PermissionUtils.hasOverlaysPermission(this);
        if (overlaysPermission) {
            mWidget.show(0, 0);
        } else {
            checkOverlaysPermission(false);
        }
    }

    private void checkOverlaysPermission(boolean result) {
        boolean overlaysPermission = PermissionUtils.hasOverlaysPermission(this);
        if (!overlaysPermission && !result) {
            PermissionUtils.openOverlayPermissionActivity(this, getPackageName(),
                    OVERLAY_PERMISSION_REQUEST_CODE);
        } else if (!overlaysPermission) {
            ToastHelper.shortToast("未授予悬浮权限");
        } else if (result) {
            mWidget.show(0, 0);
        }
    }
}
