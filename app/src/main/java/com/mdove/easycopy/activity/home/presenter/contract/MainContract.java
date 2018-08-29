package com.mdove.easycopy.activity.home.presenter.contract;


import com.mdove.easycopy.base.BasePresenter;
import com.mdove.easycopy.base.BaseView;

/**
 * Created by MDove on 2018/7/7.
 */

public interface MainContract {
    interface Presenter extends BasePresenter<MvpView> {
        void onClickTakePhoto();

        void onClickShowBall();

        void onClickOpenPhoto();

        void onClickHistory();

        void checkUpdate(String curVersion);

        void switchScreenShot(boolean isSelect);

        void switchScreenSilentShot(boolean isSelect);

        void onClickScreenShotServiceKnow();
        void onClickAllImages();
    }

    interface MvpView extends BaseView<Presenter> {
        void showLoading(String content);

        void dismissLoading();

        void showResultOCR(String content);

        void onClickShowBall();

    }
}
