package com.mdove.easycopy.resultocr.presenter.contract;


import com.mdove.easycopy.base.BasePresenter;
import com.mdove.easycopy.base.BaseView;

/**
 * Created by MDove on 2018/7/7.
 */

public interface ResultOCRContract {
    interface Presenter extends BasePresenter<MvpView> {
        void startOCR(String path);
    }

    interface MvpView extends BaseView<Presenter> {
        void showResult(String content);

        void showLoading(String content);

        void dismissLoading();
    }
}
