package com.mdove.easycopy.activity.allimages.presenter;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.mdove.easycopy.App;
import com.mdove.easycopy.R;
import com.mdove.easycopy.activity.allimages.presenter.contract.AllImagesContract;
import com.mdove.easycopy.activity.resultocr.model.ResultOCRModel;
import com.mdove.easycopy.greendao.ResultOCRDao;
import com.mdove.easycopy.greendao.entity.ResultOCR;
import com.mdove.easycopy.loadimges.LocalMedia;
import com.mdove.easycopy.loadimges.LocalMediaFolder;
import com.mdove.easycopy.loadimges.LocalMediaLoader;
import com.mdove.easycopy.loadimges.PictureConfig;
import com.mdove.easycopy.ocr.baiduocr.PreOcrManager;
import com.mdove.easycopy.ocr.baiduocr.model.RecognizeResultModel;
import com.mdove.easycopy.ocr.baiduocr.utils.ResultOCRHelper;
import com.mdove.easycopy.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class AllImagesPresenter implements AllImagesContract.Presenter {
    private AllImagesContract.MvpView mView;

    public AllImagesPresenter() {
    }

    @Override
    public void subscribe(AllImagesContract.MvpView view) {
        mView = view;
    }

    @Override
    public void unSubscribe() {
    }

    @Override
    public void initImages() {
        LocalMediaLoader loader = new LocalMediaLoader((FragmentActivity) mView.getContext(), PictureConfig.TYPE_IMAGE, false, 0, 0);
        loader.loadAllMedia(new LocalMediaLoader.LocalMediaLoadListener() {
            @Override
            public void loadComplete(List<LocalMediaFolder> folders) {
                List<LocalMedia> allImages = new ArrayList<>();
                if (folders != null && folders.size() > 0) {
                    for (LocalMediaFolder localMediaFolder : folders) {
                        allImages.addAll(localMediaFolder.getImages());
                    }
                }
                mView.showImages(allImages);
            }
        });
    }
}
