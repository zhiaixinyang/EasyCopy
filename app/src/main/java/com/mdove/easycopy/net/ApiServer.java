package com.mdove.easycopy.net;

import com.mdove.easycopy.activity.feedback.model.FeedBackModel;
import com.mdove.easycopy.activity.home.model.AppUpdateModel;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author MDove on 2018/8/28.
 */
public interface ApiServer {
    @FormUrlEncoded
    @POST("PasswordGuard/checkUpdate")
    Observable<AppUpdateModel> checkUpdate(@Field("version") String version);

    //    @FormUrlEncoded
//    @POST("ZJ/servlet/appContent")
//    Observable<FeedBackModel> feedBack(@Field("content") String content);
    @FormUrlEncoded
    @POST("ZJ/servlet/appContent")
    Observable<ResponseBody> feedBack(@Field("content") String content);
}
