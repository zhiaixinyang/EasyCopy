package com.mdove.easycopy.activity.allimages.model;

import android.graphics.Bitmap;

/**
 * Created by MDove on 2018/8/29.
 */

public class ShowBitmap {
    public String mPath;
    public Bitmap mBitmap;

    public ShowBitmap(String path, Bitmap bitmap) {
        mPath = path;
        mBitmap = bitmap;
    }
}
