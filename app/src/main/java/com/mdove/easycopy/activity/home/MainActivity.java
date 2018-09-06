package com.mdove.easycopy.activity.home;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.CompoundButton;

import com.mdove.easycopy.R;
import com.mdove.easycopy.activity.home.model.vm.MainStatisticsModelVM;
import com.mdove.easycopy.base.BaseActivity;
import com.mdove.easycopy.config.MainConfigSP;
import com.mdove.easycopy.databinding.ActivityMainBinding;
import com.mdove.easycopy.activity.home.model.handle.MainHandler;
import com.mdove.easycopy.activity.home.presenter.MainPresenter;
import com.mdove.easycopy.activity.home.presenter.contract.MainContract;
import com.mdove.easycopy.activity.resultocr.ResultOCRActivity;
import com.mdove.easycopy.mainservice.BallWidgetService;
import com.mdove.easycopy.utils.AppUtils;
import com.mdove.easycopy.utils.ToastHelper;
import com.mdove.easycopy.utils.anim.AnimUtils;
import com.mdove.easycopy.utils.permission.PermissionUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements MainContract.MvpView {
    private static final int OVERLAY_PERMISSION_REQUEST_CODE = 100;
    private ActivityMainBinding mBinding;
    private MainPresenter mPresenter;
    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mPresenter = new MainPresenter();
        mPresenter.subscribe(this);
        mPresenter.checkUpdate(AppUtils.getAPPVersionCodeFromAPP(this));

        mBinding.setHandler(new MainHandler(mPresenter));

        initView();
        initToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSwitch();

        if (mPresenter != null) {
            mPresenter.refreshStatistics();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    private void initView() {
        initSwitch();
        mSubscription = Observable.interval(3, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
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

    }

    private void initToolbar() {
        mBinding.toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(mBinding.toolbar);
    }

    private void initSwitch() {
        boolean isSelect = MainConfigSP.isScreenShotSelect();
        boolean isSilent = MainConfigSP.isScreenShotSilentSelect();

        mBinding.switchScreenShot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchStatus(true, false);
                }
                mPresenter.switchScreenShot(isChecked);
            }
        });
        mBinding.switchScreenShot.setChecked(isSelect);
        if (isSelect) {
            mPresenter.switchScreenShot(true);
        }

        mBinding.switchScreenSilentShot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchStatus(false, true);
                }
                mPresenter.switchScreenSilentShot(isChecked);
            }
        });
        mBinding.switchScreenSilentShot.setChecked(isSilent);
        if (isSilent) {
            mPresenter.switchScreenSilentShot(true);
        }
    }

    @Override
    protected boolean isNeedCustomLayout() {
        return true;
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
        ResultOCRActivity.start(this, content, ResultOCRActivity.INTENT_TYPE_RESULT_OCR);
    }

    @Override
    public void onClickShowBall() {
        initBall();
    }

    @Override
    public void refreshStatistics(MainStatisticsModelVM mainModelVM) {
        if (mBinding != null) {
            mBinding.setVm(mainModelVM);
        }
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

    private void switchStatus(boolean isSelect, boolean isSilent) {
        if (isSelect && isSilent) {
            return;
        }
        if (isSelect) {
            mBinding.switchScreenSilentShot.setChecked(false);
        }

        if (isSilent) {
            mBinding.switchScreenShot.setChecked(false);
        }
    }

}
