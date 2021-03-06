package com.mdove.easycopy.net;

import com.mdove.easycopy.App;
import com.mdove.easycopy.activity.home.model.AppUpdateModel;
import com.mdove.easycopy.utils.NetUtil;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author MDove on 2018/8/28.
 */

public class ApiServerImpl {
    private static <T> Observable<T> wrapper(Observable<T> observable) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<AppUpdateModel> checkUpdate(String version) {
        return wrapper(App.getApiServer().checkUpdate(version));
    }

    public static Observable<ResponseBody> feedBack(String version) {
        return wrapper(App.getApiServer().feedBack(version));
    }
}
