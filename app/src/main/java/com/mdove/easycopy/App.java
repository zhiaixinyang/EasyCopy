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
import com.mdove.easycopy.config.MainConfigSP;
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

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mInstance = this;

        mDaoManager = DaoManager.getInstance();

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
