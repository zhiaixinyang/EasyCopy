package com.mdove.easycopy.home.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.mdove.easycopy.R;
import com.mdove.easycopy.config.ImageConfig;
import com.mdove.easycopy.crop.Crop;
import com.mdove.easycopy.home.TransparentActivity;
import com.mdove.easycopy.home.presenter.contract.MainContract;
import com.mdove.easycopy.ocr.baiduocr.PreOcrManager;
import com.mdove.easycopy.ocr.baiduocr.model.RecognizeResultModel;
import com.mdove.easycopy.ocr.baiduocr.utils.ResultOCRHelper;
import com.mdove.easycopy.resultocr.ResultOCRActivity;
import com.mdove.easycopy.utils.BitmapUtil;
import com.mdove.easycopy.utils.FileUtils;
import com.mdove.easycopy.utils.StringUtil;

import java.io.File;

import rx.Emitter;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainPresenter implements MainContract.Presenter {
    public static final int TAKE_PHOTO_REQUEST_CODE = 111;
    private MainContract.MvpView mView;
    private File mImageFile;

    @Override
    public void subscribe(MainContract.MvpView view) {
        mView = view;
    }

    @Override
    public void unSubscribe() {

    }


    @Override
    public void onClickTakePhoto() {
        openSystemCamera(mView.getContext());
    }

    @Override
    public void onClickShowBall() {
        mView.onClickShowBall();
    }

    @Override
    public void onClickOpenPhoto() {
        Crop.pickImage((Activity) mView.getContext());
    }

    private void openSystemCamera(Context context) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        mImageFile = FileUtils.mkImageFile(ImageConfig.CONSTANT_IMAGE_PATH + System.currentTimeMillis() + ".jpg");

        Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", mImageFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        ((Activity) context).startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            switch (requestCode) {
                case TAKE_PHOTO_REQUEST_CODE: {
                    ResultOCRActivity.start(mView.getContext(), mImageFile.getPath(), ResultOCRActivity.INTENT_TYPE_START_OCR);
                    break;
                }
                case Crop.REQUEST_PICK: {
                    String path = data.getData().getPath();
                    path = path.replace("/raw/", "");
                    if (!TextUtils.isEmpty(path)) {
                        ResultOCRActivity.start(mView.getContext(), path, ResultOCRActivity.INTENT_TYPE_START_OCR);
                    }
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }
}