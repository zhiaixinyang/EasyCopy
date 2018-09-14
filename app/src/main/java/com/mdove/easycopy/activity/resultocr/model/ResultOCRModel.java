package com.mdove.easycopy.activity.resultocr.model;

public class ResultOCRModel {
    public long mId;
    public String mContent;
    public String mPath;

    public ResultOCRModel(String content, String path) {
        mPath = path;
        mContent = content;
    }
}