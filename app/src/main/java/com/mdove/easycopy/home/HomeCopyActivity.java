package com.mdove.easycopy.home;

import android.content.Context;

import com.mdove.easycopy.base.BaseActivity;
import com.mdove.easycopy.home.model.CopyDataModel;
import com.mdove.easycopy.home.presenter.contract.HomeCopyContract;

import java.util.List;

public class HomeCopyActivity extends BaseActivity implements HomeCopyContract.MvpView{
    @Override
    protected boolean isNeedCustomLayout() {
        return false;
    }

    @Override
    public void initData(List<CopyDataModel> data) {

    }

    @Override
    public Context getContext() {
        return null;
    }
}
