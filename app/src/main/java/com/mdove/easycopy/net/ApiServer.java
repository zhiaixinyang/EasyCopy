package com.mdove.easycopy.net;

import com.mdove.easycopy.activity.home.model.AppUpdateModel;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author MDove on 2018/8/28.
 */
public interface ApiServer {
    @FormUrlEncoded
    @POST("checkUpdate")
    Observable<AppUpdateModel> checkUpdate(@Field("version") String version);
}
