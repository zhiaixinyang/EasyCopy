package com.mdove.easycopy.activity.history.model.handler;

import com.mdove.easycopy.activity.history.model.vm.HistoryResultOCRModelVM;
import com.mdove.easycopy.activity.history.presenter.HistoryResultOCRPresenter;

public class HistoryResultOCRHandler {
    private HistoryResultOCRPresenter mPresenter;

    public HistoryResultOCRHandler(HistoryResultOCRPresenter presenter) {
        mPresenter = presenter;
    }

    public void onClickDelete(HistoryResultOCRModelVM vm) {
        mPresenter.onClickDelete(vm);
    }

    public void onClickCopy(HistoryResultOCRModelVM vm) {
        mPresenter.onClickCopy(vm);
    }
}
