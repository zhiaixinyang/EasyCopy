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
import com.mdove.easycopy.utils.NetUtil;
import com.mdove.easycopy.utils.ToastHelper;
import com.tencent.bugly.crashreport.CrashReport;

public class App extends Application {
    private static Context mContext;
    private static DaoSession mDaoSession;
    private DaoManager mDaoManager;
    private boolean hasGotToken;
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

        registerNetwork();
        initAccessTokenWithAkSk();

        CrashReport.setIsDevelopmentDevice(this, true);
        CrashReport.initCrashReport(getApplicationContext(), "6b29b73dba", false);

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
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
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

    public void registerNetwork() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            // 请注意这里会有一个版本适配bug，所以请在这里添加非空判断
            if (connectivityManager != null) {
                connectivityManager.requestNetwork(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {

                    /**
                     * 网络可用的回调
                     */
                    @Override
                    public void onAvailable(Network network) {
                        super.onAvailable(network);
                        initAccessTokenWithAkSk();
                    }

                    /**
                     * 网络丢失的回调
                     */
                    @Override
                    public void onLost(Network network) {
                        super.onLost(network);
                        ToastHelper.shortToast(R.string.string_register_ocr_no_network);
                    }

                    /**
                     * 当建立网络连接时，回调连接的属性
                     */
                    @Override
                    public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
                        super.onLinkPropertiesChanged(network, linkProperties);
                    }

                    /**
                     * 按照官方的字面意思是，当我们的网络的某个能力发生了变化回调，那么也就是说可能会回调多次
                     * <p>
                     * 之后在仔细的研究
                     */
                    @Override
                    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                        super.onCapabilitiesChanged(network, networkCapabilities);
                    }

                    /**
                     * 在网络失去连接的时候回调，但是如果是一个生硬的断开，他可能不回调
                     */
                    @Override
                    public void onLosing(Network network, int maxMsToLive) {
                        super.onLosing(network, maxMsToLive);
                    }

                    /**
                     * 按照官方注释的解释，是指如果在超时时间内都没有找到可用的网络时进行回调
                     */
                    @Override
                    public void onUnavailable() {
                        super.onUnavailable();
                    }
                });
            }
        }
    }
}
