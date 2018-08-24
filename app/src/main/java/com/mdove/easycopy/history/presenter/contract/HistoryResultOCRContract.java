package com.mdove.easycopy.history.presenter.contract;


import com.mdove.easycopy.base.BasePresenter;
import com.mdove.easycopy.base.BaseView;

/**
 * Created by MDove on 2018/7/7.
 */

public interface HistoryResultOCRContract {
    interface Presenter extends BasePresenter<MvpView> {
        void initData();
    }

    interface MvpView extends BaseView<Presenter> {
    }
}
