package com.mdove.easycopy.activity.allimages.presenter.contract;


import android.graphics.Bitmap;

import com.mdove.easycopy.activity.allimages.model.ShowBitmap;
import com.mdove.easycopy.activity.resultocr.model.ResultOCRModel;
import com.mdove.easycopy.base.BasePresenter;
import com.mdove.easycopy.base.BaseView;
import com.mdove.easycopy.loadimges.LocalMedia;

import java.util.List;

/**
 * Created by MDove on 2018/7/7.
 */

public interface AllImagesContract {
    interface Presenter extends BasePresenter<MvpView> {
        void initImages();

        void loadMore(boolean isLast);
    }

    interface MvpView extends BaseView<Presenter> {
        void showImages(List<ShowBitmap> allImages);

        void showMoreImages(List<ShowBitmap> allImages);

        void showLoading(String content);

        void dismissLoading();
    }
}
