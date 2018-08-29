package com.mdove.easycopy.resultocr.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.mdove.easycopy.App;
import com.mdove.easycopy.R;
import com.mdove.easycopy.config.ImageConfig;
import com.mdove.easycopy.greendao.ResultOCRDao;
import com.mdove.easycopy.greendao.entity.ResultOCR;
import com.mdove.easycopy.ocr.baiduocr.PreOcrManager;
import com.mdove.easycopy.ocr.baiduocr.model.RecognizeResultModel;
import com.mdove.easycopy.ocr.baiduocr.utils.ResultOCRHelper;
import com.mdove.easycopy.resultocr.ResultOCRActivity;
import com.mdove.easycopy.resultocr.model.ResultOCRModel;
import com.mdove.easycopy.resultocr.presenter.contract.ResultOCRContract;
import com.mdove.easycopy.utils.BitmapUtil;
import com.mdove.easycopy.utils.ClipboardUtils;
import com.mdove.easycopy.utils.FileUtils;
import com.mdove.easycopy.utils.StringUtil;
import com.mdove.easycopy.utils.ToastHelper;

import java.io.File;
import java.util.Date;

import rx.Emitter;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ResultOCRPresenter implements ResultOCRContract.Presenter {
    private ResultOCRContract.MvpView mView;
    private ResultOCRDao mResultOCRDao;

    public ResultOCRPresenter() {
        mResultOCRDao = App.getDaoSession().getResultOCRDao();
    }

    @Override
    public void subscribe(ResultOCRContract.MvpView view) {
        mView = view;
    }

    @Override
    public void unSubscribe() {
    }

    @Override
    public void startOCR(final String path, final int type) {
        mView.showLoading(StringUtil.getString(R.string.string_start_ocr));
        PreOcrManager.baiduOcrFromPath(mView.getContext(), path, new PreOcrManager.RecognizeResultListener() {
            @Override
            public void onRecognizeResult(RecognizeResultModel model) {
                mView.dismissLoading();
                boolean isCopy = true;
                String content = ResultOCRHelper.getStringFromModel(model);

                if (TextUtils.isEmpty(content)) {
                    isCopy = false;
                    content = "很抱歉,此图片无法识别并提取出文字。";
                }
                ResultOCRModel realModel = new ResultOCRModel(content, path);

                ResultOCR resultOCR = new ResultOCR();
                resultOCR.mResultOCR = content;
                resultOCR.mResultOCRTime = System.currentTimeMillis();
                resultOCR.mPath = path;
                mResultOCRDao.insert(resultOCR);

                if (type == ResultOCRActivity.INTENT_TYPE_START_SILENT_OCR) {
                    if (isCopy) {
                        ClipboardUtils.copyToClipboard(mView.getContext(), content);
                        ToastHelper.shortToast(StringUtil.getString(R.string.string_silent_ocr_suc));
                    }
                }

                mView.showResult(realModel);
            }
        });
    }
}
