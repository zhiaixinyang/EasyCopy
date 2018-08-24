package com.mdove.easycopy.history.model.vm;

import android.databinding.ObservableField;

import com.mdove.easycopy.history.model.HistoryResultOCRModel;

public class HistoryResultOCRModelVM {
    public ObservableField<String> mResultOCR = new ObservableField<>();
    public ObservableField<String> mResultOCRTime = new ObservableField<>();

    public HistoryResultOCRModelVM(HistoryResultOCRModel model) {
        mResultOCR.set(model.mResultOCR);
        mResultOCRTime.set("");
    }
}
