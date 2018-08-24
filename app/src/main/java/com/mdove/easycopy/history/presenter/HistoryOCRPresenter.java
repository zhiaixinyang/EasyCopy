package com.mdove.easycopy.history.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.mdove.easycopy.App;
import com.mdove.easycopy.config.ImageConfig;
import com.mdove.easycopy.crop.Crop;
import com.mdove.easycopy.greendao.ResultOCRDao;
import com.mdove.easycopy.history.presenter.contract.HistoryResultOCRContract;
import com.mdove.easycopy.resultocr.ResultOCRActivity;
import com.mdove.easycopy.utils.FileUtils;

import java.io.File;

public class HistoryOCRPresenter implements HistoryResultOCRContract.Presenter {
    private HistoryResultOCRContract.MvpView mView;
    private ResultOCRDao mResultOCRDao;

    public HistoryOCRPresenter() {
        mResultOCRDao = App.getDaoSession().getResultOCRDao();
    }

    @Override
    public void subscribe(HistoryResultOCRContract.MvpView view) {
        mView = view;
    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void initData() {

    }
}