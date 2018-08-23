package com.mdove.easycopy.home.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.mdove.easycopy.R;
import com.mdove.easycopy.config.ImageConfig;
import com.mdove.easycopy.home.presenter.contract.MainContract;
import com.mdove.easycopy.ocr.baiduocr.PreOcrManager;
import com.mdove.easycopy.ocr.baiduocr.model.RecognizeResultModel;
import com.mdove.easycopy.ocr.baiduocr.utils.ResultOCRHelper;
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
        mView.onClickOpenPhoto();
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
        if (resultCode == -1 && requestCode == TAKE_PHOTO_REQUEST_CODE) {
            mView.showLoading(StringUtil.getString(R.string.string_start_ocr));
            Observable.create(new Action1<Emitter<String>>() {
                @Override
                public void call(Emitter<String> emitter) {
                    BitmapUtil.saveToFile(BitmapUtil.decodeToBitmap(mImageFile.getPath(), 1440, 960), mImageFile.getPath());
                    emitter.onNext(mImageFile.getPath());
                }
            }, Emitter.BackpressureMode.BUFFER).subscribeOn(Schedulers.io())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            PreOcrManager.baiduOcrFromPath(mView.getContext(), mImageFile.getPath(), new PreOcrManager.RecognizeResultListener() {
                                @Override
                                public void onRecognizeResult(RecognizeResultModel model) {
                                    mView.dismissLoading();
                                    mView.showResultOCR(ResultOCRHelper.getStringFromModel(model));
                                }
                            });
                        }
                    });
        }
    }
}
