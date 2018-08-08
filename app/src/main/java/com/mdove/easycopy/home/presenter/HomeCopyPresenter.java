package com.mdove.easycopy.home.presenter;

import com.mdove.easycopy.App;
import com.mdove.easycopy.greendao.CopyDataDao;
import com.mdove.easycopy.greendao.entity.CopyData;
import com.mdove.easycopy.home.model.CopyDataModel;
import com.mdove.easycopy.home.presenter.contract.HomeCopyContract;

import java.util.ArrayList;
import java.util.List;

public class HomeCopyPresenter implements HomeCopyContract.Presenter {
    private HomeCopyContract.MvpView mView;
    private List<CopyDataModel> mData;
    private CopyDataDao mCopyDataDao;

    public HomeCopyPresenter() {
        mCopyDataDao = App.getDaoSession().getCopyDataDao();
    }

    @Override
    public void initData() {
        mData = new ArrayList<>();
        List<CopyData> data = mCopyDataDao.loadAll();
        for (CopyData copyData : data) {
            mData.add(new CopyDataModel(copyData));
        }

        mView.initData(mData);
    }

    @Override
    public void subscribe(HomeCopyContract.MvpView view) {
        mView = view;
    }

    @Override
    public void unSubscribe() {

    }
}
