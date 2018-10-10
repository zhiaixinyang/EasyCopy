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
import com.mdove.easycopy.utils.NetUtil;
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
        if (NetUtil.isNetworkAvailable(mView.getContext())) {
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
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    ToastHelper.shortToast(R.string.string_feedback_err);
                }
            });
        } else {
            ToastHelper.shortToast(R.string.string_no_network);
        }
    }

    @Override
    public void initTopBanner() {
        List<String> arr = new ArrayList<>();
        arr.add("这是一个工具类的应用~");
        arr.add("这个应用是我们工作之余开发的~");
        arr.add("更多的是为了解决日常中的需要~");
        arr.add("欢迎反馈：任何问题、吐槽、建议~");
        arr.add("我们会不断改进哒~");
        mView.initTopBanner(arr);
    }
}