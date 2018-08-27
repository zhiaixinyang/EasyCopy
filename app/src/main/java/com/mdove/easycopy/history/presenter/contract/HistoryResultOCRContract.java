package com.mdove.easycopy.history.presenter.contract;


import com.mdove.easycopy.base.BasePresenter;
import com.mdove.easycopy.base.BaseView;
import com.mdove.easycopy.history.model.HistoryResultOCRModel;
import com.mdove.easycopy.history.model.vm.HistoryResultOCRModelVM;

import java.util.List;

/**
 * Created by MDove on 2018/7/7.
 */

public interface HistoryResultOCRContract {
    interface Presenter extends BasePresenter<MvpView> {
        void initData();

        void onClickDelete(HistoryResultOCRModelVM vm);

        void onClickCopy(HistoryResultOCRModelVM vm);
    }

    interface MvpView extends BaseView<Presenter> {
        void showData(List<HistoryResultOCRModel> data);

        void onDeleteItemId(long id);
    }
}
