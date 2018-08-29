package com.mdove.easycopy.resultocr.presenter.contract;


import com.mdove.easycopy.base.BasePresenter;
import com.mdove.easycopy.base.BaseView;
import com.mdove.easycopy.resultocr.model.ResultOCRModel;

/**
 * Created by MDove on 2018/7/7.
 */

public interface ResultOCRContract {
    interface Presenter extends BasePresenter<MvpView> {
        void startOCR(String path, int type);
    }

    interface MvpView extends BaseView<Presenter> {
        void showResult(ResultOCRModel model);

        void showLoading(String content);

        void dismissLoading();
    }
}
