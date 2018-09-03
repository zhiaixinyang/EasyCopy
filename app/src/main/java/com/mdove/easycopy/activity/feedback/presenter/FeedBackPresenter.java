package com.mdove.easycopy.activity.feedback.presenter;

import android.text.TextUtils;

import com.mdove.easycopy.App;
import com.mdove.easycopy.R;
import com.mdove.easycopy.activity.feedback.presenter.contract.FeedBackContract;
import com.mdove.easycopy.activity.history.model.HistoryResultOCRModel;
import com.mdove.easycopy.activity.history.model.vm.HistoryResultOCRModelVM;
import com.mdove.easycopy.greendao.ResultOCRDao;
import com.mdove.easycopy.greendao.entity.ResultOCR;
import com.mdove.easycopy.net.ApiServerImpl;
import com.mdove.easycopy.utils.ClipboardUtils;
import com.mdove.easycopy.utils.StringUtil;
import com.mdove.easycopy.utils.ToastHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by MDove on 2018/9/3.
 */
public class FeedBackPresenter implements FeedBackContract.Presenter {
    private FeedBackContract.MvpView mView;
    private Subscription mSubscription;

    public FeedBackPresenter() {
    }

    @Override
    public void subscribe(FeedBackContract.MvpView view) {
        mView = view;
    }

    @Override
    public void unSubscribe() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }


    @Override
    public void sendFeedBack(String content) {
        mSubscription = ApiServerImpl.feedBack(content).subscribe(new Action1<ResponseBody>() {
            @Override
            public void call(ResponseBody responseBody) {
                String result = "";
                try {
                    result = responseBody.string().trim();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (TextUtils.equals(result, "{result=1}")) {
                    mView.sendFeedBackReturn(1);
                } else {
                    mView.sendFeedBackReturn(0);
                }
            }
        });
    }
}