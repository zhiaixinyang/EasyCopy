package com.mdove.easycopy.ocr.baiduocr;

import android.content.Context;

import com.mdove.easycopy.config.MainConfigSP;
import com.mdove.easycopy.ocr.baiduocr.model.RecognizeResultModel;
import com.mdove.easycopy.ocr.baiduocr.utils.ResultOCRHelper;
import com.mdove.easycopy.utils.JsonUtil;

import java.util.List;

public class PreOcrManager {
    private static String mContent = "";

    public static void baiduOcrFromPath(Context context, String path, final RecognizeResultListener listener) {
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
    }

    public static void baiduOcrFromPaths(Context context, List<String> paths, final ResultStringListener listener) {
        mContent = "";
        for (String path : paths) {
            RecognizeManager.recGeneralBasic(context, path,
                    new RecognizeManager.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            RecognizeResultModel model = JsonUtil.decode(result, RecognizeResultModel.class);
                            boolean isAdd = configStatistics(model);
                            if (isAdd) {
                                mContent += ResultOCRHelper.getStringFromModel(model) + "\n";
                            }
                        }
                    });
        }
        if (listener != null) {
            listener.onResultString(mContent);
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

    public interface RecognizeResultListener {
        void onRecognizeResult(RecognizeResultModel model);
    }

    public interface ResultStringListener {
        void onResultString(String content);
    }
}
