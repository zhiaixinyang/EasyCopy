package com.mdove.easycopy.activity.home.model.handle;

import com.mdove.easycopy.activity.home.presenter.MainPresenter;

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

    public void onClickScreenShotServiceKnow() {
        mPresenter.onClickScreenShotServiceKnow();
    }

    public void onClickScreenShotSilentServiceKnow() {
        mPresenter.onClickScreenShotSilentServiceKnow();
    }

    public void onClickAllImages() {
        mPresenter.onClickAllImages();
    }
}
