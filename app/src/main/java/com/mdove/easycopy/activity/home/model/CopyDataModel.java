package com.mdove.easycopy.activity.home.model;

import com.mdove.easycopy.greendao.entity.CopyData;

public class CopyDataModel {
    public long mCopyTime;
    public String mCopyContent;

    public CopyDataModel(CopyData copyData) {
        mCopyContent = copyData.copyContent;
        mCopyTime = copyData.copyTime;
    }
}
