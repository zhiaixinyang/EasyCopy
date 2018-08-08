package com.mdove.easycopy.mainservice;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.mdove.easycopy.App;
import com.mdove.easycopy.greendao.CopyDataDao;
import com.mdove.easycopy.greendao.entity.CopyData;

import java.util.Date;

public class MainCopyService extends Service implements ClipboardManager.OnPrimaryClipChangedListener {
    private ClipboardManager mManager;
    private CopyDataDao mCopyDataDao;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initDB();
        registerClipEvents();
    }

    private void initDB() {
        mCopyDataDao = App.getDaoSession().getCopyDataDao();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mManager != null) {
            mManager.removePrimaryClipChangedListener(this);
        }
    }

    private void registerClipEvents() {
        mManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        mManager.addPrimaryClipChangedListener(this);
    }

    @Override
    public void onPrimaryClipChanged() {
        if (mManager.hasPrimaryClip() && mManager.getPrimaryClip().getItemCount() > 0) {
            CharSequence addedText = mManager.getPrimaryClip().getItemAt(0).getText();
            if (addedText != null) {
                CopyData copyData = new CopyData();
                copyData.copyContent = addedText.toString();
                copyData.copyTime = new Date().getTime();
                mCopyDataDao.insert(copyData);
            }
        }
    }
}
