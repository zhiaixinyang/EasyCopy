package com.mdove.easycopy.home;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.mdove.easycopy.R;
import com.mdove.easycopy.base.BaseActivity;
import com.mdove.easycopy.databinding.ActivityMainBinding;
import com.mdove.easycopy.home.model.AppUpdateModel;
import com.mdove.easycopy.home.model.handle.MainHandler;
import com.mdove.easycopy.home.presenter.MainPresenter;
import com.mdove.easycopy.home.presenter.contract.MainContract;
import com.mdove.easycopy.resultocr.ResultOCRActivity;
import com.mdove.easycopy.ui.ResultOCRDialog;
import com.mdove.easycopy.ui.floatview.service.BallWidgetService;
import com.mdove.easycopy.utils.AppUtils;
import com.mdove.easycopy.utils.DensityUtil;
import com.mdove.easycopy.utils.ToastHelper;
import com.mdove.easycopy.utils.anim.AnimUtils;
import com.mdove.easycopy.utils.permission.PermissionUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements MainContract.MvpView {
    private static final int OVERLAY_PERMISSION_REQUEST_CODE = 100;
    private ActivityMainBinding mBinding;
    private MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initCircleWaveView();

        mPresenter = new MainPresenter();
        mPresenter.subscribe(this);
        mPresenter.checkUpdate(AppUtils.getAPPVersionCodeFromAPP(this));

        mBinding.setHandler(new MainHandler(mPresenter));
    }

    private void initCircleWaveView() {
        Observable.interval(3, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if ((aLong % 3) != 0) {
                            return;
                        }
                        int result = (int) (aLong / 3);

                        if (result % 2 == 0) {
                            AnimUtils.flipAnimatorXViewShow(mBinding.viewTakePhotoFirst, mBinding.viewTakePhotoLast, 500);
                        } else {
                            AnimUtils.flipAnimatorXViewShow(mBinding.viewTakePhotoLast, mBinding.viewTakePhotoFirst, 500);
                        }
                    }
                });
//        mBinding.cwv.setInitialRadius(DensityUtil.dip2px(this, 88f))
//                .setMaxRadius(DensityUtil.getScreenWidth(this) / 2)
//                .setDuration(2000)
//                .setSpeed(500)
//                .setStyle(Paint.Style.FILL)
//                .setColor(Color.WHITE)
//                .setInitialAlpha(102)
//                .setInterpolator(new AccelerateDecelerateInterpolator())
//                .start();
    }

    @Override
    protected boolean isNeedCustomLayout() {
        return true;
    }

    private void initBall() {
        boolean overlaysPermission = PermissionUtils.hasOverlaysPermission(this);
        if (overlaysPermission) {
            BallWidgetService.showWeatherBall(this);
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
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void showResultOCR(String content) {
//        ResultOCRDialog.show(this, content);
        ResultOCRActivity.start(this, content, ResultOCRActivity.INTENT_TYPE_RESULT_OCR);
    }

    @Override
    public void onClickShowBall() {
        initBall();
    }

}
