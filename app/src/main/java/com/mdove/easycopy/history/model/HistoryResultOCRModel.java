package com.mdove.easycopy.history.model;

import com.mdove.easycopy.greendao.entity.ResultOCR;

public class HistoryResultOCRModel {
    public long mResultOCRTime;
    public String mResultOCR;

    public HistoryResultOCRModel(ResultOCR resultOCR) {
        mResultOCR = resultOCR.mResultOCR;
        mResultOCRTime = resultOCR.mResultOCRTime;
    }
}
