package com.mdove.easycopy.history;

import android.content.Context;

import com.mdove.easycopy.base.BaseActivity;
import com.mdove.easycopy.history.presenter.contract.HistoryResultOCRContract;

public class HistoryResultOCRActivity extends BaseActivity implements HistoryResultOCRContract.MvpView {
    @Override
    protected boolean isNeedCustomLayout() {
        return true;
    }

    @Override
    public Context getContext() {
        return null;
    }
}
