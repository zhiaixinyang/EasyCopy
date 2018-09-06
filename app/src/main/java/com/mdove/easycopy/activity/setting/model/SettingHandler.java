package com.mdove.easycopy.activity.setting.model;

import com.mdove.easycopy.activity.setting.presenter.SettingPresenter;

public class SettingHandler {
    private SettingPresenter mPresenter;

    public SettingHandler(SettingPresenter presenter) {
        mPresenter = presenter;
    }

    public void onClickClear() {
        mPresenter.clear();
    }

}
