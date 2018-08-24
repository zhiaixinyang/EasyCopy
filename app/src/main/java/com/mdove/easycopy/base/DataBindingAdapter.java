package com.mdove.easycopy.base;

import android.databinding.BindingAdapter;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

public class DataBindingAdapter {
    @BindingAdapter("loadBgFormPath")
    public static void loadBgFormPath(ImageView imageView, String path) {
        if (TextUtils.isEmpty(path)) {
            imageView.setVisibility(View.GONE);
            return;
        }
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageBitmap(BitmapFactory.decodeFile(path));
    }
}
