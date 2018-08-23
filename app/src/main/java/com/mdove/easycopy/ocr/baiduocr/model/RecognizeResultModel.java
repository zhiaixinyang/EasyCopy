package com.mdove.easycopy.ocr.baiduocr.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecognizeResultModel {
    @SerializedName("words_result_num")
    public float mWordsResultNum;
    @SerializedName("words_result")
    public List<RealResultModel> mResultArr;

    public class RealResultModel {
        @SerializedName("words")
        public String mWorlds;
    }

}
