package com.mdove.easycopy.history.model;

public class HistoryResultOCRModel {
    public long mResultOCRTime;
    public String mResultOCR;

    public HistoryResultOCRModel(String resultOCR, long resultOCRTime) {
        mResultOCR = resultOCR;
        mResultOCRTime = resultOCRTime;
    }
}
