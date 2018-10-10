package com.mdove.easycopy.activity.resultocr.presenter;

import android.text.TextUtils;

import com.mdove.easycopy.App;
import com.mdove.easycopy.R;
import com.mdove.easycopy.activity.resultocr.model.ResultOCRModelVM;
import com.mdove.easycopy.greendao.ResultOCRDao;
import com.mdove.easycopy.greendao.entity.ResultOCR;
import com.mdove.easycopy.ocr.baiduocr.PreOcrManager;
import com.mdove.easycopy.ocr.baiduocr.model.RecognizeResultModel;
import com.mdove.easycopy.ocr.baiduocr.utils.ResultOCRHelper;
import com.mdove.easycopy.activity.resultocr.model.ResultOCRModel;
import com.mdove.easycopy.activity.resultocr.presenter.contract.ResultOCRContract;
import com.mdove.easycopy.utils.ClipboardUtils;
import com.mdove.easycopy.utils.JsonUtil;
import com.mdove.easycopy.utils.StringUtil;
import com.mdove.easycopy.utils.ToastHelper;

import java.util.ArrayList;
import java.util.List;

public class ResultOCRPresenter implements ResultOCRContract.Presenter {
    private ResultOCRContract.MvpView mView;
    private ResultOCRDao mResultOCRDao;
    private List<OCRImages> mOCRImages;
    private List<String> mImagePaths;

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
                boolean isSuc = true;

                if (TextUtils.isEmpty(content) || TextUtils.equals(content, StringUtil.getString(R.string.string_result_ocr_error))) {
                    content = "很抱歉,此图片无法识别并提取出文字。";
                    isSuc = false;
                }
                ResultOCRModel realModel = new ResultOCRModel(content, path);

                ResultOCR resultOCR = new ResultOCR();
                resultOCR.mResultOCR = content;
                resultOCR.mResultOCRTime = System.currentTimeMillis();
                resultOCR.mPath = path;
                realModel.mId = mResultOCRDao.insert(resultOCR);

                if (isSuc) {
                    ToastHelper.shortToast(R.string.string_toast_ocr_suc);
                }

                mView.showResult(realModel);
            }

            @Override
            public void onRegisterOCR(String path, final int type) {
                mView.dismissLoading();
                startOCR(path, type);
            }

            @Override
            public void onRegisterOCRError(String error) {
                mView.dismissLoading();
                mView.registerOcrError(error);
            }
        });
    }

    @Override
    public void startOCRForList(List<String> paths) {
//        mView.showLoading(StringUtil.getString(R.string.string_start_ocr));
//        mOCRImages = new ArrayList<>();
//        mImagePaths = paths;
//        final List<String> mTempPaths = paths;
//        startOCRForPath(mImagePaths.get(0), new OnSucListener() {
//            @Override
//            public void onSucOCRPaths() {
//                String content = "";
//                for (OCRImages ocrImages : mOCRImages) {
//                    content += ocrImages.mResult + "\n";
//                }
//                ResultOCR resultOCR = new ResultOCR();
//                resultOCR.mResultOCR = content;
//                resultOCR.mResultOCRTime = System.currentTimeMillis();
//                resultOCR.mPaths = JsonUtil.encode(mTempPaths);
//                mResultOCRDao.insert(resultOCR);
//
//                ResultOCRModel realModel = new ResultOCRModel(content, mImagePaths.get(0));
//
//                mView.dismissLoading();
//                mView.showResult(realModel);
//            }
//
//            @Override
//            public void onErrOCRPaths() {
//                mView.dismissLoading();
//
//            }
//        });
    }

    @Override
    public void onClickCopy(ResultOCRModelVM vm) {
        ClipboardUtils.copyToClipboard(mView.getContext(), vm.mContent.get());
        ToastHelper.shortToast(StringUtil.getString(R.string.string_copy_suc));
    }

    @Override
    public void updateHistoryOCR(ResultOCRModelVM vm) {
        ResultOCR resultOCR = mResultOCRDao.queryBuilder().where(ResultOCRDao.Properties.Id.eq(vm.mId.get())).unique();
        if (resultOCR != null) {
            resultOCR.mResultOCR = vm.mContent.get();
            mResultOCRDao.update(resultOCR);
            ToastHelper.shortToast(R.string.string_toast_update_history_result_ocr);
        }
    }

    private void startOCRForPath(final String path, final OnSucListener listener) {
//        PreOcrManager.baiduOcrFromPath(mView.getContext(), path, new PreOcrManager.RecognizeResultListener() {
//            @Override
//            public void onRecognizeResult(RecognizeResultModel model) {
//                String content = ResultOCRHelper.getStringFromModel(model);
//
//                if (TextUtils.isEmpty(content)) {
//                    content = "很抱歉,此图片无法识别并提取出文字。";
//                }
//                mOCRImages.add(new OCRImages(path, content));
//
//                if (mImagePaths.size() >= 2) {
//                    mImagePaths.remove(0);
//                    startOCRForPath(mImagePaths.get(0), listener);
//                } else {
//                    if (listener != null) {
//                        listener.onSucOCRPaths();
//                    }
//                }
//            }
//
//            @Override
//            public void onRegisterOCR(String path, final int type) {
//
//            }
//        });
    }

    private interface OnSucListener {
        void onSucOCRPaths();

        void onErrOCRPaths();
    }

    private class OCRImages {
        public String mPath;
        public String mResult;

        public OCRImages(String path, String result) {
            mPath = path;
            mResult = result;
        }
    }
}
