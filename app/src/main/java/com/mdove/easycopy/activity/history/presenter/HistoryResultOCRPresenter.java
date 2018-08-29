package com.mdove.easycopy.activity.history.presenter;

import com.mdove.easycopy.App;
import com.mdove.easycopy.R;
import com.mdove.easycopy.greendao.ResultOCRDao;
import com.mdove.easycopy.greendao.entity.ResultOCR;
import com.mdove.easycopy.activity.history.model.HistoryResultOCRModel;
import com.mdove.easycopy.activity.history.model.vm.HistoryResultOCRModelVM;
import com.mdove.easycopy.activity.history.presenter.contract.HistoryResultOCRContract;
import com.mdove.easycopy.utils.ClipboardUtils;
import com.mdove.easycopy.utils.StringUtil;
import com.mdove.easycopy.utils.ToastHelper;

import java.util.ArrayList;
import java.util.List;

public class HistoryResultOCRPresenter implements HistoryResultOCRContract.Presenter {
    private HistoryResultOCRContract.MvpView mView;
    private ResultOCRDao mResultOCRDao;

    public HistoryResultOCRPresenter() {
        mResultOCRDao = App.getDaoSession().getResultOCRDao();
    }

    @Override
    public void subscribe(HistoryResultOCRContract.MvpView view) {
        mView = view;
    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void initData() {
        List<HistoryResultOCRModel> data = new ArrayList<>();
        List<ResultOCR> resultOCRList = mResultOCRDao.loadAll();
        for (ResultOCR resultOCR : resultOCRList) {
            data.add(new HistoryResultOCRModel(resultOCR));
        }
        mView.showData(data);
    }

    @Override
    public void onClickDelete(HistoryResultOCRModelVM vm) {
        ResultOCR resultOCR = mResultOCRDao.queryBuilder().where(ResultOCRDao.Properties.Id.eq(vm.mId)).unique();
        if (resultOCR != null) {
            mResultOCRDao.delete(resultOCR);
            mView.onDeleteItemId(vm.mId);
        }
    }

    @Override
    public void onClickCopy(HistoryResultOCRModelVM vm) {
        ClipboardUtils.copyToClipboard(mView.getContext(), vm.mResultOCR.get());
        ToastHelper.shortToast(StringUtil.getString(R.string.string_copy_suc));
    }
}