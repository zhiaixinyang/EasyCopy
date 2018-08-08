package com.mdove.easycopy.home.model;

import com.mdove.easycopy.greendao.entity.CopyData;

public class CopyDataModel {
    public long mCopyTime;
    public String mCopyContent;

    public CopyDataModel(CopyData copyData) {
        mCopyContent = copyData.copyContent;
        mCopyTime = copyData.copyTime;
    }
}
