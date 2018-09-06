package com.mdove.easycopy.activity.setting.presenter.contract;


import com.mdove.easycopy.activity.resultocr.model.ResultOCRModel;
import com.mdove.easycopy.base.BasePresenter;
import com.mdove.easycopy.base.BaseView;

import java.util.List;

/**
 * Created by MDove on 2018/7/7.
 */

public interface SettingContract {
    interface Presenter extends BasePresenter<MvpView> {
        void clear();
    }

    interface MvpView extends BaseView<Presenter> {
    }
}
