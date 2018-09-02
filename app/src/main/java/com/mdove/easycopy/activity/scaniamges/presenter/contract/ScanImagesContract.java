package com.mdove.easycopy.activity.scaniamges.presenter.contract;


import com.mdove.easycopy.activity.allimages.model.ShowBitmap;
import com.mdove.easycopy.activity.scaniamges.bean.ImageFloder;
import com.mdove.easycopy.base.BasePresenter;
import com.mdove.easycopy.base.BaseView;

import java.util.List;

/**
 * Created by MDove on 2018/7/7.
 */

public interface ScanImagesContract {
    interface Presenter extends BasePresenter<MvpView> {
        void initImages();

    }

    interface MvpView extends BaseView<Presenter> {
        void showImages(List<ShowBitmap> allImages);
        void showImagesCount(int maxCount);
        void initImageFloders(List<ImageFloder> allImageFloder);

        void showLoading(String content);

        void dismissLoading();
    }
}
