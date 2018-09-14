package com.mdove.easycopy.activity.resultocr.model.handler;

import com.mdove.easycopy.activity.resultocr.model.ResultOCRModelVM;
import com.mdove.easycopy.activity.resultocr.presenter.ResultOCRPresenter;
import com.mdove.easycopy.greendao.entity.ResultOCR;

public class ResultOCRHandler {
    private ResultOCRPresenter mPresenter;

    public ResultOCRHandler(ResultOCRPresenter presenter) {
        mPresenter = presenter;
    }

    public void onClickCopy(ResultOCRModelVM vm) {
        mPresenter.onClickCopy(vm);
    }

    public void onClickUpdate(ResultOCRModelVM vm) {
        mPresenter.updateHistoryOCR(vm);
    }
}
