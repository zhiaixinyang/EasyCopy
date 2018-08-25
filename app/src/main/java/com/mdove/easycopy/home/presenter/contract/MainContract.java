package com.mdove.easycopy.home.presenter.contract;


import com.mdove.easycopy.base.BasePresenter;
import com.mdove.easycopy.base.BaseView;
import com.mdove.easycopy.home.model.CopyDataModel;

import java.util.List;

/**
 * Created by MDove on 2018/7/7.
 */

public interface MainContract {
    interface Presenter extends BasePresenter<MvpView> {
        void onClickTakePhoto();

        void onClickShowBall();

        void onClickOpenPhoto();

        void onClickHistory();
    }

    interface MvpView extends BaseView<Presenter> {
        void showLoading(String content);

        void dismissLoading();

        void showResultOCR(String content);

        void onClickShowBall();
    }
}
