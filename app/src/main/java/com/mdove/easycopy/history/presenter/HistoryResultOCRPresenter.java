package com.mdove.easycopy.history.presenter;

import com.mdove.easycopy.App;
import com.mdove.easycopy.greendao.ResultOCRDao;
import com.mdove.easycopy.greendao.entity.ResultOCR;
import com.mdove.easycopy.history.model.HistoryResultOCRModel;
import com.mdove.easycopy.history.presenter.contract.HistoryResultOCRContract;

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
}