package com.mdove.easycopy.activity.setting.presenter;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.mdove.easycopy.R;
import com.mdove.easycopy.activity.setting.presenter.contract.SettingContract;
import com.mdove.easycopy.config.ImageConfig;
import com.mdove.easycopy.utils.FileUtils;
import com.mdove.easycopy.utils.StringUtil;
import com.mdove.easycopy.utils.ToastHelper;

import java.io.File;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.observables.SyncOnSubscribe;
import rx.schedulers.Schedulers;

public class SettingPresenter implements SettingContract.Presenter {
    private SettingContract.MvpView mView;
    private Subscription mSubscribe;

    public SettingPresenter() {
    }

    @Override
    public void subscribe(SettingContract.MvpView view) {
        mView = view;
    }

    @Override
    public void unSubscribe() {
        mSubscribe.isUnsubscribed();
    }


    @Override
    public void clear() {
        deleteFile(new File(ImageConfig.CONSTANT_IMAGE_PATH));
    }

    private void deleteFile(final File file) {
        new AlertDialog.Builder(mView.getContext())
                .setTitle(R.string.string_delete_file_title)
                .setMessage(R.string.string_delete_file_content)
                .setPositiveButton(R.string.string_delete_file_btn_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mSubscribe = Observable.create(new SyncOnSubscribe<String, String>() {
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
                }).setNegativeButton(R.string.string_delete_file_btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

}
