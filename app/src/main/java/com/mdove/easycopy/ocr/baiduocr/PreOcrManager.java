package com.mdove.easycopy.ocr.baiduocr;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.mdove.easycopy.config.MainConfigSP;
import com.mdove.easycopy.ocr.baiduocr.model.RecognizeResultModel;
import com.mdove.easycopy.ocr.baiduocr.utils.ResultOCRHelper;
import com.mdove.easycopy.utils.BitmapUtil;
import com.mdove.easycopy.utils.JsonUtil;

public class PreOcrManager {
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

    private static void configStatistics(RecognizeResultModel model) {
        MainConfigSP.addOCRCount();
        if (model != null) {
            MainConfigSP.addOCRSucCount();
            String content = ResultOCRHelper.getStringFromModel(model);
            MainConfigSP.addOCRWordsCount(content.length());
        }
    }

    public interface RecognizeResultListener {
        void onRecognizeResult(RecognizeResultModel model);
    }
}
