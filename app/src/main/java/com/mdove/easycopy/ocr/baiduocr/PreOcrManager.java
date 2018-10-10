package com.mdove.easycopy.ocr.baiduocr;

import android.content.Context;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.mdove.easycopy.R;
import com.mdove.easycopy.config.MainConfigSP;
import com.mdove.easycopy.ocr.baiduocr.model.RecognizeResultModel;
import com.mdove.easycopy.ocr.baiduocr.utils.ResultOCRHelper;
import com.mdove.easycopy.utils.JsonUtil;
import com.mdove.easycopy.utils.NetUtil;
import com.mdove.easycopy.utils.ToastHelper;

import java.util.List;

public class PreOcrManager {
    public static boolean hasRegisterOCR = false;

    public static void baiduOcrFromPath(Context context, final String path, final RecognizeResultListener listener) {
        if (hasRegisterOCR) {
            RecognizeManager.recGeneralBasic(context, path,
                    new RecognizeManager.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            RecognizeResultModel model = JsonUtil.decode(result, RecognizeResultModel.class);
                            configStatistics(model);
                            if (listener != null) {
                                listener.onRecognizeResult(model);
                            }
                        }
                    });
        } else {
            PreOcrManager.initAccessToken(context, new RegisterResultListener() {
                @Override
                public void onRegisterSuc() {
                    if (listener != null) {
                        listener.onRegisterOCR(path, -1);
                    }
                }

                @Override
                public void onRegisterError(String content) {
                    if (listener != null) {
                        listener.onRegisterOCRError("注册图像识别服务失败：" + content);
                    }
                }
            });
        }
    }

    private static boolean configStatistics(RecognizeResultModel model) {
        boolean isAdd = false;
        MainConfigSP.addOCRCount();
        if (model != null) {
            isAdd = true;
            MainConfigSP.addOCRSucCount();
            String content = ResultOCRHelper.getStringFromModel(model);
            MainConfigSP.addOCRWordsCount(content.length());
        }
        return isAdd;
    }

    /**
     * 以license文件方式初始化
     */
    public static void initAccessToken(Context context, final RegisterResultListener listener) {
        if (NetUtil.isNetworkAvailable(context)) {
            OCR.getInstance(context).initAccessToken(new OnResultListener<AccessToken>() {
                @Override
                public void onResult(AccessToken accessToken) {
                    String token = accessToken.getAccessToken();
                    PreOcrManager.hasRegisterOCR = true;
                    MainConfigSP.setBaiduOcrToken(token);
                    ToastHelper.shortToast(R.string.string_register_ocr_suc);
                    if (listener != null) {
                        listener.onRegisterSuc();
                    }
                }

                @Override
                public void onError(OCRError error) {
                    error.printStackTrace();
                    PreOcrManager.hasRegisterOCR = false;
                    if (listener != null) {
                        listener.onRegisterError("图片识别服务注册失败："+error.getMessage());
                    }
                }
            }, context);
        }
    }

    public interface RecognizeResultListener {
        void onRecognizeResult(RecognizeResultModel model);

        void onRegisterOCR(String path, final int type);

        void onRegisterOCRError(String error);
    }

    public interface RegisterResultListener {
        void onRegisterSuc();

        void onRegisterError(String content);
    }
}
