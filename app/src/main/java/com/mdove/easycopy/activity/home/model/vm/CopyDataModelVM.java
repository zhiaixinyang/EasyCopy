package com.mdove.easycopy.activity.home.model.vm;

import android.databinding.ObservableField;

import com.mdove.easycopy.activity.home.model.CopyDataModel;
import com.mdove.easycopy.utils.DateUtils;

public class CopyDataModelVM {
    public ObservableField<String> mCopyContent = new ObservableField<>();
    public ObservableField<String> mCopyTime = new ObservableField<>();

    public CopyDataModelVM(CopyDataModel copyDataModel) {
        mCopyContent.set(copyDataModel.mCopyContent);
        mCopyTime.set(DateUtils.getDateChineseYMD(copyDataModel.mCopyTime));
    }
}
