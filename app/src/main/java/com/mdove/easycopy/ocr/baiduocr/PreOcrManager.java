package com.mdove.easycopy.ocr.baiduocr;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.mdove.easycopy.ocr.baiduocr.model.RecognizeResultModel;
import com.mdove.easycopy.utils.BitmapUtil;
import com.mdove.easycopy.utils.JsonUtil;

public class PreOcrManager {
    public static void baiduOcrFromPath(Context context, String path, final RecognizeResultListener listener) {
        RecognizeManager.recGeneralBasic(context, path,
                new RecognizeManager.ServiceListener() {
                    @Override
                    public void onResult(String result) {
                        RecognizeResultModel model = JsonUtil.decode(result, RecognizeResultModel.class);
                        if (listener != null) {
                            listener.onRecognizeResult(model);
                        }
                    }
                });
    }

    public interface RecognizeResultListener {
        void onRecognizeResult(RecognizeResultModel model);
    }
}
