package com.mdove.easycopy.activity.feedback.presenter.contract;


import com.mdove.easycopy.activity.history.model.HistoryResultOCRModel;
import com.mdove.easycopy.activity.history.model.vm.HistoryResultOCRModelVM;
import com.mdove.easycopy.base.BasePresenter;
import com.mdove.easycopy.base.BaseView;

import java.util.List;

/**
 * Created by MDove on 2018/9/3.
 */
public interface FeedBackContract {
    interface Presenter extends BasePresenter<MvpView> {
        void sendFeedBack(String content);

        void initTopBanner();
    }

    interface MvpView extends BaseView<Presenter> {
        void sendFeedBackReturn(int status);

        void initTopBanner(List<String> bannerList);
    }
}
