package com.mdove.easycopy.activity.setting.presenter;

import android.util.Log;

import com.mdove.easycopy.R;
import com.mdove.easycopy.activity.setting.presenter.contract.SettingContract;
import com.mdove.easycopy.config.ImageConfig;
import com.mdove.easycopy.utils.FileUtils;
import com.mdove.easycopy.utils.StringUtil;
import com.mdove.easycopy.utils.ToastHelper;

import java.io.File;

import rx.Emitter;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.observables.AsyncOnSubscribe;
import rx.observables.SyncOnSubscribe;
import rx.schedulers.Schedulers;

import static com.mdove.easycopy.utils.FileUtils.deleteFile;

public class SettingPresenter implements SettingContract.Presenter {
    private SettingContract.MvpView mView;

    public SettingPresenter() {
    }

    @Override
    public void subscribe(SettingContract.MvpView view) {
        mView = view;
    }

    @Override
    public void unSubscribe() {
    }


    @Override
    public void clear() {
        deleteFile(new File(ImageConfig.CONSTANT_IMAGE_PATH));
    }

    private void deleteFile(final File file) {
        Subscription subscribe = Observable.create(new SyncOnSubscribe<String, String>() {
            @Override
            protected String generateState() {
                return null;
            }

            @Override
            protected String next(String state, Observer<? super String> observer) {
                FileUtils.deleteFile(file);
                observer.onNext(StringUtil.getString(R.string.string_clear_suc));
                observer.onCompleted();
                return null;
            }

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String integer) {
                        ToastHelper.shortToast(integer);
                    }
                });
    }

}
