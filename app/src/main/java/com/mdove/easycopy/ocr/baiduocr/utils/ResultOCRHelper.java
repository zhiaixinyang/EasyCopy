package com.mdove.easycopy.ocr.baiduocr.utils;

import com.mdove.easycopy.R;
import com.mdove.easycopy.ocr.baiduocr.model.RecognizeResultModel;
import com.mdove.easycopy.utils.StringUtil;

public class ResultOCRHelper {
    public static String getStringFromModel(RecognizeResultModel model) {
        String content = null;
        if (model != null) {
            if (model.mResultArr != null && model.mResultArr.size() > 0) {
                for (RecognizeResultModel.RealResultModel realResultModel : model.mResultArr) {
                    content += realResultModel.mWorlds;
                }
            } else {
                content = StringUtil.getString(R.string.string_result_ocr_error);
            }
        }
        return content;
    }
}
