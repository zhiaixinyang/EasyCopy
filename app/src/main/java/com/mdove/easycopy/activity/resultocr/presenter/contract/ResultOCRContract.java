package com.mdove.easycopy.activity.resultocr.presenter.contract;


import com.mdove.easycopy.activity.resultocr.model.ResultOCRModelVM;
import com.mdove.easycopy.base.BasePresenter;
import com.mdove.easycopy.base.BaseView;
import com.mdove.easycopy.activity.resultocr.model.ResultOCRModel;
import com.mdove.easycopy.greendao.entity.ResultOCR;

import java.util.List;

/**
 * Created by MDove on 2018/7/7.
 */

public interface ResultOCRContract {
    interface Presenter extends BasePresenter<MvpView> {
        void startOCR(String path, int type);

        void startOCRForList(List<String> paths);

        void onClickCopy(ResultOCRModelVM vm);

        void updateHistoryOCR(ResultOCRModelVM vm);
    }

    interface MvpView extends BaseView<Presenter> {
        void showResult(ResultOCRModel model);

        void showLoading(String content);

        void registerOcrError(String content);

        void dismissLoading();
    }
}
