package com.mdove.easycopy.activity.resultocr.presenter;

import android.text.TextUtils;

import com.mdove.easycopy.App;
import com.mdove.easycopy.R;
import com.mdove.easycopy.greendao.ResultOCRDao;
import com.mdove.easycopy.greendao.entity.ResultOCR;
import com.mdove.easycopy.ocr.baiduocr.PreOcrManager;
import com.mdove.easycopy.ocr.baiduocr.model.RecognizeResultModel;
import com.mdove.easycopy.ocr.baiduocr.utils.ResultOCRHelper;
import com.mdove.easycopy.activity.resultocr.model.ResultOCRModel;
import com.mdove.easycopy.activity.resultocr.presenter.contract.ResultOCRContract;
import com.mdove.easycopy.utils.StringUtil;

import java.util.List;

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
                String content = ResultOCRHelper.getStringFromModel(model);

                if (TextUtils.isEmpty(content)) {
                    content = "很抱歉,此图片无法识别并提取出文字。";
                }
                ResultOCRModel realModel = new ResultOCRModel(content, path);

                ResultOCR resultOCR = new ResultOCR();
                resultOCR.mResultOCR = content;
                resultOCR.mResultOCRTime = System.currentTimeMillis();
                resultOCR.mPath = path;
                mResultOCRDao.insert(resultOCR);

                mView.showResult(realModel);
            }
        });
    }

    @Override
    public void startOCRForList(List<String> paths) {
        mView.showLoading(StringUtil.getString(R.string.string_start_ocr));
        PreOcrManager.baiduOcrFromPaths(mView.getContext(), paths, new PreOcrManager.ResultStringListener() {
            @Override
            public void onResultString(String content) {
                mView.dismissLoading();

                if (TextUtils.isEmpty(content)) {
                    content = "很抱歉,此图片无法识别并提取出文字。";
                }
                ResultOCRModel realModel = new ResultOCRModel(content, "");

                ResultOCR resultOCR = new ResultOCR();
                resultOCR.mResultOCR = content;
                resultOCR.mResultOCRTime = System.currentTimeMillis();
                resultOCR.mPath = "";
                mResultOCRDao.insert(resultOCR);

                mView.showResult(realModel);
            }
        });
    }
}
