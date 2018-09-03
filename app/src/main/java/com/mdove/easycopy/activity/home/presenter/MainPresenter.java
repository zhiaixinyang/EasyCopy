package com.mdove.easycopy.activity.home.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import com.mdove.easycopy.R;
import com.mdove.easycopy.activity.feedback.FeedBackActivity;
import com.mdove.easycopy.activity.home.model.vm.MainStatisticsModelVM;
import com.mdove.easycopy.activity.scaniamges.ScanImageActivity;
import com.mdove.easycopy.activity.scaniamges.adapter.ScanImagesAdapter;
import com.mdove.easycopy.config.ImageConfig;
import com.mdove.easycopy.config.MainConfigSP;
import com.mdove.easycopy.activity.crop.Crop;
import com.mdove.easycopy.activity.history.HistoryResultOCRActivity;
import com.mdove.easycopy.screenshot.ScreenshotObserverService;
import com.mdove.easycopy.update.UpdateDialog;
import com.mdove.easycopy.update.manager.UpdateStatusManager;
import com.mdove.easycopy.activity.home.model.AppUpdateModel;
import com.mdove.easycopy.activity.home.presenter.contract.MainContract;
import com.mdove.easycopy.net.ApiServerImpl;
import com.mdove.easycopy.activity.resultocr.ResultOCRActivity;
import com.mdove.easycopy.utils.FileUtils;
import com.mdove.easycopy.utils.IntentUtils;
import com.mdove.easycopy.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;

import rx.Subscriber;

public class MainPresenter implements MainContract.Presenter {
    public static final int TAKE_PHOTO_REQUEST_CODE = 111;
    private MainContract.MvpView mView;
    private File mImageFile;

    @Override
    public void subscribe(MainContract.MvpView view) {
        mView = view;

    }

    @Override
    public void unSubscribe() {

    }


    @Override
    public void onClickTakePhoto() {
        openSystemCamera(mView.getContext());
    }

    @Override
    public void refreshStatistics() {
        long ocrCount = MainConfigSP.getOCRCount();
        long ocrWordsCount = MainConfigSP.getOCRWordsCount();
        long ocrSucCount = MainConfigSP.getOCRSucCount();

        mView.refreshStatistics(new MainStatisticsModelVM(ocrSucCount, ocrWordsCount, ocrCount));
    }

    @Override
    public void onClickShowBall() {
        mView.onClickShowBall();
    }

    @Override
    public void onClickOpenPhoto() {
//        Crop.pickImage((Activity) mView.getContext());
    }

    @Override
    public void onClickFeedBack() {
        FeedBackActivity.start(mView.getContext());
    }

    @Override
    public void onClickHistory() {
        IntentUtils.startActivity(mView.getContext(), HistoryResultOCRActivity.class);
    }

    @Override
    public void checkUpdate(String curVersion) {
        ApiServerImpl.checkUpdate(curVersion).subscribe(new Subscriber<AppUpdateModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(AppUpdateModel realUpdate) {
                switch (realUpdate.check) {
                    case "true": {
                        if (UpdateStatusManager.isShowUpdateDialog()) {
                            showUpgradeDialog(realUpdate);
                        }
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void switchScreenShot(boolean isSelect) {
        boolean isPreSelect = MainConfigSP.isScreenShotSelect();
        if (isSelect) {
            ScreenshotObserverService.start(mView.getContext());
            if (isPreSelect) {
                return;
            }
            MainConfigSP.setIsScreenShotSelect(true);
        } else {
            if (!isPreSelect) {
                return;
            }
            MainConfigSP.setIsScreenShotSelect(false);
        }
    }

    @Override
    public void switchScreenSilentShot(boolean isSelect) {
        boolean isPreSelect = MainConfigSP.isScreenShotSilentSelect();
        if (isSelect) {
            ScreenshotObserverService.start(mView.getContext());
            if (isPreSelect) {
                return;
            }
            MainConfigSP.setIsScreenShotSilentSelect(true);
        } else {
            if (!isPreSelect) {
                return;
            }
            MainConfigSP.setIsScreenShotSilentSelect(false);
        }
    }

    @Override
    public void onClickScreenShotServiceKnow() {
        new AlertDialog.Builder(mView.getContext())
                .setTitle(StringUtil.getString(R.string.string_screenshot_service_know_title))
                .setMessage(StringUtil.getString(R.string.string_screenshot_service_know))
                .setPositiveButton(StringUtil.getString(R.string.string_screenshot_service_know_positive), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onClickScreenShotSilentServiceKnow() {
        new AlertDialog.Builder(mView.getContext())
                .setTitle(StringUtil.getString(R.string.string_screenshot_service_know_title))
                .setMessage(StringUtil.getString(R.string.string_screenshot_silent_service_know))
                .setPositiveButton(StringUtil.getString(R.string.string_screenshot_service_know_positive), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onClickAllImages() {
        Intent toScan = new Intent(mView.getContext(), ScanImageActivity.class);
        ((Activity) mView.getContext()).startActivityForResult(toScan, ScanImageActivity.REQUEST_CODE_SELECT_IAMGES);
    }

    private void showUpgradeDialog(final AppUpdateModel result) {
        new UpdateDialog(mView.getContext(), result.src).show();
    }

    private void openSystemCamera(Context context) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        mImageFile = FileUtils.mkImageFile(ImageConfig.CONSTANT_IMAGE_PATH + System.currentTimeMillis() + ".jpg");

        Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", mImageFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        ((Activity) context).startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            switch (requestCode) {
                case TAKE_PHOTO_REQUEST_CODE: {
                    ResultOCRActivity.start(mView.getContext(), mImageFile.getPath(), ResultOCRActivity.INTENT_TYPE_START_OCR);
                    break;
                }
                case Crop.REQUEST_PICK: {
                    String path = data.getData().getPath();
                    path = path.replace("/raw/", "");
                    if (!TextUtils.isEmpty(path)) {
                        ResultOCRActivity.start(mView.getContext(), path, ResultOCRActivity.INTENT_TYPE_START_OCR);
                    }
                    break;
                }
                case ScanImageActivity.REQUEST_CODE_SELECT_IAMGES: {
                    ArrayList<String> stringArrayList = data.getStringArrayListExtra(ScanImageActivity.EXTRA_SELECT_IMAGES);
                    if (stringArrayList != null) {
                        Intent ocr = new Intent(mView.getContext(), ResultOCRActivity.class);
                        ocr.setAction(ResultOCRActivity.ACTION_START_OCR_SELECT_IMAGES);
                        ocr.putExtra(ResultOCRActivity.EXTRA_INTENT_TYPE, ResultOCRActivity.INTENT_TYPE_START_OCR);
                        ocr.putStringArrayListExtra(ResultOCRActivity.EXTRA_START_OCR_SELECT_IMAGES_PATH, stringArrayList);
                        mView.getContext().startActivity(ocr);
                    }
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }
}