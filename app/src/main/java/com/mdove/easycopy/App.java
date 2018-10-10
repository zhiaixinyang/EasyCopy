package com.mdove.easycopy;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.hwangjr.rxbus.RxBus;
import com.mdove.easycopy.config.MainConfigSP;
import com.mdove.easycopy.event.RegisterEvent;
import com.mdove.easycopy.greendao.DaoSession;
import com.mdove.easycopy.greendao.utils.DaoManager;
import com.mdove.easycopy.net.ApiServer;
import com.mdove.easycopy.net.RetrofitUtil;
import com.mdove.easycopy.net.UrlConstants;
import com.mdove.easycopy.ocr.baiduocr.PreOcrManager;
import com.mdove.easycopy.utils.NetUtil;
import com.mdove.easycopy.utils.ToastHelper;
import com.tencent.bugly.crashreport.CrashReport;

public class App extends Application {
    private static Context mContext;
    private static DaoSession mDaoSession;
    private DaoManager mDaoManager;
    private static final String APP_KEY = "pyCGpbqqDMSQt6AyUmAc4Tse";
    private static final String SECRET_KEY = "8ji4GQ9uKKOK8sCukXaPlkaGI3s1CCQL";
    private static App mInstance;
    private static ApiServer mApiServer;
    private boolean hasGotToken;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mInstance = this;

        mDaoManager = DaoManager.getInstance();

        initAccessToken();

        CrashReport.setIsDevelopmentDevice(this, true);
        CrashReport.initCrashReport(getApplicationContext(), "6b29b73dba", false);

        PreOcrManager.initAccessToken(this, null);

        mDaoManager.init(mContext);
        if (mDaoSession == null) {
            synchronized (App.class) {
                if (null == mDaoSession) {
                    mDaoSession = mDaoManager.getDaoMaster().newSession();
                }
            }
        }
    }

    public static synchronized App getInstance() {
        return mInstance;
    }

    /**
     * 以license文件方式初始化
     */
    private void initAccessToken() {
        OCR.getInstance(this).initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                MainConfigSP.setBaiduOcrToken(token);
                hasGotToken = true;
                ToastHelper.shortToast(R.string.string_register_ocr_suc);
                RxBus.get().post(new RegisterEvent());
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                RxBus.get().post(new RegisterEvent());

                MainConfigSP.setBaiduOcrToken(null);
                ToastHelper.shortToast("licence方式获取token失败" + error.getMessage());
            }
        }, getApplicationContext());
    }

    /**
     * 用明文ak，sk初始化
     */
    private void initAccessTokenWithAkSk() {
        if (NetUtil.isNetworkAvailable(this)) {
            OCR.getInstance(this).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
                @Override
                public void onResult(AccessToken result) {
                    RxBus.get().post(new RegisterEvent());

                    String token = result.getAccessToken();
                    MainConfigSP.setBaiduOcrToken(token);
                    hasGotToken = true;
                    ToastHelper.shortToast(R.string.string_register_ocr_suc);
                    RxBus.get().post(new RegisterEvent());
                }

                @Override
                public void onError(OCRError error) {
                    RxBus.get().post(new RegisterEvent());

                    MainConfigSP.setBaiduOcrToken(null);
                    error.printStackTrace();
                    ToastHelper.shortToast(R.string.string_register_ocr_error + ":" + error.getMessage());
                }
            }, getApplicationContext(), APP_KEY, SECRET_KEY);
        } else {
            ToastHelper.shortToast(R.string.string_register_ocr_no_network);
        }
    }

    public static DaoSession getDaoSession() {
        return mDaoSession;
    }

    public static Context getContext() {
        return mContext;
    }

    public static ApiServer getApiServer() {
        if (mApiServer == null) {
            mApiServer = RetrofitUtil.create(UrlConstants.HOST_API_ONLINE, ApiServer.class);
        }
        return mApiServer;
    }
}
