package com.mdove.easycopy.activity.allimages.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mdove.easycopy.loadimges.LocalMedia;
import com.mdove.easycopy.ui.coolviewpager.CoolViewPager;
import com.mdove.easycopy.utils.ImageUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllImageVpAdapter extends PagerAdapter {
    private List<LocalMedia> mData;
    private List<View> mViews;
    private Context mContext;
    private Map<String, Bitmap> mImageCaches = new HashMap<String, Bitmap>();

    public AllImageVpAdapter(Context context, List<LocalMedia> items) {
        mData = items.subList(0, 5);
        mContext = context;
        initView(mData);
    }

    private void initView(List<LocalMedia> items) {
        mViews = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            mViews.add(createImageView(items.get(i).getPath()));
        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //必须要先判断,否则报错:java.lang.IllegalStateException: The specified child already has a parent
        //ViewGroup的addView（）方法不能添加一个已存在父控件的视图,所以在执行前需要判断要添加的View实例是不是存在父控件.
        //如果存在父控件,需要其父控件先将该View移除掉,再执行addView
        if (mViews.get(position).getParent() != null) {
            ((ViewGroup) mViews.get(position).getParent()).removeView(mViews.get(position));
        }
        container.addView(mViews.get(position));
        return mViews.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public View createImageView(String path) {
        ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(new CoolViewPager.LayoutParams());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        if (mImageCaches.get(path) != null) {
            imageView.setImageBitmap(mImageCaches.get(path));
        } else {
            Bitmap bitmap = ImageUtil.decodeFile(path);
            mImageCaches.put(path, bitmap);
            imageView.setImageBitmap(bitmap);
        }
        return imageView;
    }
}
