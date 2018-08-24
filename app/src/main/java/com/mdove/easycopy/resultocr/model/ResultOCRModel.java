package com.mdove.easycopy.resultocr.model;

public class ResultOCRModel {
    public String mContent;
    public String mPath;

    public ResultOCRModel(String content, String path) {
        mPath = path;
        mContent = content;
    }
}