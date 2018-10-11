package com.mdove.easycopy.activity.history;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.mdove.easycopy.R;
import com.mdove.easycopy.base.BaseActivity;
import com.mdove.easycopy.databinding.ActivityHistoryResultOcrBinding;
import com.mdove.easycopy.activity.history.adapter.HistoryResultOCRAdapter;
import com.mdove.easycopy.activity.history.model.HistoryResultOCRModel;
import com.mdove.easycopy.activity.history.presenter.HistoryResultOCRPresenter;
import com.mdove.easycopy.activity.history.presenter.contract.HistoryResultOCRContract;
import com.mdove.easycopy.ui.swipeback.SwipeBackActivity;

import java.util.List;

public class HistoryResultOCRActivity extends SwipeBackActivity implements HistoryResultOCRContract.MvpView {
    private ActivityHistoryResultOcrBinding mBinding;
    private HistoryResultOCRAdapter mAdapter;
    private HistoryResultOCRPresenter mPresenter;

    @Override
    protected boolean isNeedCustomLayout() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_history_result_ocr);
        initToolbar();
        mPresenter = new HistoryResultOCRPresenter();
        mPresenter.subscribe(this);
        mAdapter = new HistoryResultOCRAdapter(mPresenter);

        mBinding.rlv.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rlv.setAdapter(mAdapter);
        mPresenter.initData();
    }

    private void initToolbar() {
        mBinding.toolbar.setTitle(R.string.string_activity_history_result_ocr_title);
        setSupportActionBar(mBinding.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showData(List<HistoryResultOCRModel> data) {
        mAdapter.setData(data);
    }

    @Override
    public void onDeleteItemId(long id) {
        mAdapter.notifyPositionForId(id);
    }
}
