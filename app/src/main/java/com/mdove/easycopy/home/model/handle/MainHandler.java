package com.mdove.easycopy.home.model.handle;

import com.mdove.easycopy.home.presenter.MainPresenter;

public class MainHandler {
    private MainPresenter mPresenter;

    public MainHandler(MainPresenter presenter) {
        mPresenter = presenter;
    }

    public void onClickTakePhoto() {
        mPresenter.onClickTakePhoto();
    }

    public void onClickShowBall() {
        mPresenter.onClickShowBall();
    }

    public void onClickOpenPhoto() {
        mPresenter.onClickOpenPhoto();
    }

    public void onClickHistory() {
        mPresenter.onClickHistory();
    }
}
