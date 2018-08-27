package com.mdove.easycopy.history.model.handler;

import com.mdove.easycopy.history.model.vm.HistoryResultOCRModelVM;
import com.mdove.easycopy.history.presenter.HistoryResultOCRPresenter;

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
