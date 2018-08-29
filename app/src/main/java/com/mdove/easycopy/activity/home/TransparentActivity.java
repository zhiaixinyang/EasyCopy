package com.mdove.easycopy.activity.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mdove.easycopy.R;

/**
 * 透明Activity，用来处理系统相机Activity的业务，通过本地广播，把结果发送出去（在主module中的SystemCameraManager中处理）
 */
public class TransparentActivity extends AppCompatActivity {
    private FrameLayout mMainContainer;
    private ImageView mIvBg;

    public static void start(Context context) {
        Intent start = new Intent(context, TransparentActivity.class);
        if (!(context instanceof Activity)) {
            start.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(start);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent);

        mMainContainer = findViewById(R.id.main_container);
        mIvBg = findViewById(R.id.iv_bg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIvBg.postDelayed(new Runnable() {
            @Override
            public void run() {
//                View viewRoot = getWindow().getDecorView().getRootView();
//                viewRoot.setDrawingCacheEnabled(true);
//                Bitmap screenShotAsBitmap = Bitmap.createBitmap(viewRoot.getDrawingCache());
//                viewRoot.setDrawingCacheEnabled(false);
//                mIvBg.setImageBitmap(screenShotAsBitmap);

            }
        }, 1000);
    }
}
