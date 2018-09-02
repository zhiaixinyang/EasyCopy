package com.mdove.easycopy.activity.allimages.model.vm;

import android.databinding.ObservableField;
import android.graphics.Bitmap;

import com.mdove.easycopy.activity.allimages.model.ShowBitmap;

/**
 * Created by MBENBEN on 2018/9/2.
 */

public class ShowBitmapModelVM {
    public ObservableField<Bitmap> mBitmap = new ObservableField<>();

    public ShowBitmapModelVM(ShowBitmap showBitmap){
        mBitmap.set(showBitmap.mBitmap);
    }
}
