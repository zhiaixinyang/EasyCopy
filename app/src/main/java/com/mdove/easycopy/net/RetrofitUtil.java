package com.mdove.easycopy.net;

import com.mdove.easycopy.net.utils.RetrofitFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author MDove on 2018/8/28.
 */

public class RetrofitUtil extends RetrofitFactory {
    public static <T> T create(String baseApiUrl, Class<T> service) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        clientBuilder.connectTimeout(10000, TimeUnit.MILLISECONDS);
        clientBuilder.readTimeout(10000, TimeUnit.MILLISECONDS);
        clientBuilder.writeTimeout(50000, TimeUnit.MILLISECONDS);

//        clientBuilder.addInterceptor(getPublicParamsInterceptor(context));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseApiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(clientBuilder.build())
                .build();

        return retrofit.create(service);
    }
}
