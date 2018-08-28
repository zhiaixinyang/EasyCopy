package com.mdove.easycopy.net;

import com.mdove.easycopy.App;
import com.mdove.easycopy.home.model.AppUpdateModel;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author MDove on 2018/8/28.
 */

public class ApiServerImpl {
    private static  <T> Observable<T> wrapper(Observable<T> observable) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<AppUpdateModel> checkUpdate(String version) {
        return wrapper(App.getApiServer().checkUpdate(version));
    }
}
