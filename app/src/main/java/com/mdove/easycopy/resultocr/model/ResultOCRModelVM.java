package com.mdove.easycopy.resultocr.model;

import android.databinding.ObservableField;

public class ResultOCRModelVM {
    public ObservableField<String> mContent = new ObservableField<>();

    public ResultOCRModelVM(ResultOCRModel model) {
        mContent.set(model.mContent);
    }
}
