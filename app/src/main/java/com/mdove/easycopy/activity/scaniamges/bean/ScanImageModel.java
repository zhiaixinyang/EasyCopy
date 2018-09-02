package com.mdove.easycopy.activity.scaniamges.bean;

import java.util.List;

/**
 * Created by MDove on 2018/9/2.
 */

public class ScanImageModel {
    public List<ImageFloder> mImageFloders;
    public List<String> mCurAllImages;
    public int mTotalCount;

    public ScanImageModel(List<ImageFloder> imageFloders, List<String> curAllImages, int totalCount) {
        mImageFloders = imageFloders;
        mCurAllImages = curAllImages;
        mTotalCount = totalCount;
    }
}
