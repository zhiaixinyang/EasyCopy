package com.mdove.easycopy.activity.resultocr.model;

import android.databinding.ObservableField;

public class ResultOCRModelVM {
    public ObservableField<String> mContent = new ObservableField<>();
    public ObservableField<String> mPath = new ObservableField<>();
    public ObservableField<Long> mId = new ObservableField<>();

    public ResultOCRModelVM(ResultOCRModel model) {
        mContent.set(model.mContent);
        mPath.set(model.mPath);
        mId.set(model.mId);
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mContent.set(s.toString());
    }
}
