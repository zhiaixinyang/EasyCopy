package com.mdove.easycopy.activity.allimages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.mdove.easycopy.R;
import com.mdove.easycopy.activity.allimages.adapter.AllImageVpAdapter;
import com.mdove.easycopy.activity.allimages.presenter.AllImagesPresenter;
import com.mdove.easycopy.activity.allimages.presenter.contract.AllImagesContract;
import com.mdove.easycopy.activity.resultocr.model.ResultOCRModel;
import com.mdove.easycopy.activity.resultocr.model.ResultOCRModelVM;
import com.mdove.easycopy.base.BaseActivity;
import com.mdove.easycopy.loadimges.LocalMedia;
import com.mdove.easycopy.ui.coolviewpager.CoolViewPager;
import com.mdove.easycopy.ui.coolviewpager.transformer.DepthPageTransformer;
import com.mdove.easycopy.ui.coolviewpager.transformer.VerticalRotateDownTransformer;
import com.mdove.easycopy.ui.coolviewpager.transformer.VerticalRotateTransformer;
import com.mdove.easycopy.ui.coolviewpager.transformer.VerticalZoomInTransformer;

import java.util.List;

public class AllImagesActivity extends BaseActivity implements AllImagesContract.MvpView {
    private CoolViewPager mCVP;
    private AllImagesPresenter mPresenter;

    public static void start(Context context) {
        Intent intent = new Intent(context, AllImagesActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected boolean isNeedCustomLayout() {
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_images);
        mCVP = findViewById(R.id.cvp);
        mPresenter = new AllImagesPresenter();
        mPresenter.subscribe(this);
        intentHandle(getIntent());
        mPresenter.initImages();
    }

    private void intentHandle(Intent intent) {
        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            return;
        }
        switch (action) {
            default: {
                break;
            }
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showImages(List<LocalMedia> allImages) {
        AllImageVpAdapter adapter = new AllImageVpAdapter(this, allImages);
        mCVP.setScrollMode(CoolViewPager.ScrollMode.HORIZONTAL);
        mCVP.setPageTransformer(true, new DepthPageTransformer());
        mCVP.setAdapter(adapter);
    }
}
