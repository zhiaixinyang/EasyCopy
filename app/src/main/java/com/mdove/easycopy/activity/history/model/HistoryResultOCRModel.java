package com.mdove.easycopy.activity.history.model;

import com.mdove.easycopy.greendao.entity.ResultOCR;

public class HistoryResultOCRModel {
    public long mId;
    public long mResultOCRTime;
    public String mResultOCR;

    public HistoryResultOCRModel(ResultOCR resultOCR) {
        mId = resultOCR.id;
        mResultOCR = resultOCR.mResultOCR;
        mResultOCRTime = resultOCR.mResultOCRTime;
    }
}
