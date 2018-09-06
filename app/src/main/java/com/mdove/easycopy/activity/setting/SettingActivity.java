package com.mdove.easycopy.activity.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.mdove.easycopy.R;
import com.mdove.easycopy.activity.setting.model.SettingHandler;
import com.mdove.easycopy.activity.setting.presenter.SettingPresenter;
import com.mdove.easycopy.activity.setting.presenter.contract.SettingContract;
import com.mdove.easycopy.base.BaseActivity;
import com.mdove.easycopy.databinding.ActivitySettingBinding;

public class SettingActivity extends BaseActivity implements SettingContract.MvpView {
    private ActivitySettingBinding mBinding;
    private SettingPresenter mPresenter;

    public static void start(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected boolean isNeedCustomLayout() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        initToolbar();
        mPresenter = new SettingPresenter();
        mPresenter.subscribe(this);

        mBinding.setHandler(new SettingHandler(mPresenter));
    }

    private void initToolbar() {
        mBinding.toolbar.setTitle(getString(R.string.string_activity_setting));
        setSupportActionBar(mBinding.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public Context getContext() {
        return this;
    }
}
