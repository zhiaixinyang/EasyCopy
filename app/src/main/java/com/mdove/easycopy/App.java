package com.mdove.easycopy;

import android.app.Application;
import android.content.Context;

import com.mdove.easycopy.greendao.DaoSession;
import com.mdove.easycopy.greendao.utils.DaoManager;

public class App extends Application {
    private static Context mContext;
    private static DaoSession mDaoSession;
    private DaoManager mDaoManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        mDaoManager = DaoManager.getInstance();

        mDaoManager.init(mContext);
        if (mDaoSession == null) {
            synchronized (App.class) {
                if (null == mDaoSession) {
                    mDaoSession = mDaoManager.getDaoMaster().newSession();
                }
            }
        }
    }

    public static DaoSession getDaoSession() {
        return mDaoSession;
    }

    public static Context getContext() {
        return mContext;
    }
}
