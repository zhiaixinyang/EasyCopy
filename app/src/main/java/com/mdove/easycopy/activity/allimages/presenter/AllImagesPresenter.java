package com.mdove.easycopy.activity.allimages.presenter;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.mdove.easycopy.App;
import com.mdove.easycopy.R;
import com.mdove.easycopy.activity.allimages.model.ShowBitmap;
import com.mdove.easycopy.activity.allimages.presenter.contract.AllImagesContract;
import com.mdove.easycopy.activity.resultocr.model.ResultOCRModel;
import com.mdove.easycopy.config.ImageConfig;
import com.mdove.easycopy.greendao.ResultOCRDao;
import com.mdove.easycopy.greendao.entity.ResultOCR;
import com.mdove.easycopy.loadimges.LocalMedia;
import com.mdove.easycopy.loadimges.LocalMediaFolder;
import com.mdove.easycopy.loadimges.LocalMediaLoader;
import com.mdove.easycopy.loadimges.PictureConfig;
import com.mdove.easycopy.ocr.baiduocr.PreOcrManager;
import com.mdove.easycopy.ocr.baiduocr.model.RecognizeResultModel;
import com.mdove.easycopy.ocr.baiduocr.utils.ResultOCRHelper;
import com.mdove.easycopy.utils.ImageUtil;
import com.mdove.easycopy.utils.StringUtil;
import com.mdove.easycopy.utils.ToastHelper;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class AllImagesPresenter implements AllImagesContract.Presenter {
    private AllImagesContract.MvpView mView;
    private Subscription mSubscription;
    private List<LocalMedia> mAllImages;
    private int mCurIndexStart = 0;
    private static final int COUNT_IMAGES = 10;

    public AllImagesPresenter() {
    }

    @Override
    public void subscribe(AllImagesContract.MvpView view) {
        mView = view;
    }

    @Override
    public void unSubscribe() {
        mSubscription.unsubscribe();
    }

    @Override
    public void initImages() {
        showLoading();
        LocalMediaLoader loader = new LocalMediaLoader((FragmentActivity) mView.getContext(), PictureConfig.TYPE_IMAGE, false, 0, 0);
        loader.loadAllMedia(new LocalMediaLoader.LocalMediaLoadListener() {
            @Override
            public void loadComplete(List<LocalMediaFolder> folders) {
                mAllImages = new ArrayList<>();
                for (LocalMediaFolder localMediaFolder : folders) {
                    mAllImages.addAll(localMediaFolder.getImages());
                }
                preLoadImages();
            }
        });
    }

    @Override
    public void loadMore(boolean isLast) {
        showLoading();
        if (isLast) {
            mCurIndexStart++;
            int remainIndex = mAllImages.size() - mCurIndexStart * COUNT_IMAGES;
            if (remainIndex > 0) {
                int endIndex = 0;
                if (remainIndex >= 10) {
                    endIndex = 10;
                } else {
                    endIndex = remainIndex;
                }
                mSubscription = createBitmaps(mAllImages.subList(mCurIndexStart * COUNT_IMAGES, mCurIndexStart * COUNT_IMAGES + endIndex))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<List<ShowBitmap>>() {
                            @Override
                            public void call(List<ShowBitmap> bitmaps) {
                                mView.showMoreImages(bitmaps);
                                mView.dismissLoading();
                            }
                        });
            } else {
                mView.dismissLoading();
                ToastHelper.shortToast(StringUtil.getString(R.string.string_no_more_images_err));
            }
        } else {
            if (mCurIndexStart > 0) {
                mCurIndexStart--;

                mSubscription = createBitmaps(mAllImages.subList(mCurIndexStart * COUNT_IMAGES, (mCurIndexStart + 1) * COUNT_IMAGES))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<List<ShowBitmap>>() {
                            @Override
                            public void call(List<ShowBitmap> bitmaps) {
                                mView.showMoreImages(bitmaps);
                                mView.dismissLoading();
                            }
                        });
            } else {
                mView.dismissLoading();
                ToastHelper.shortToast(StringUtil.getString(R.string.string_is_first_images_err));
            }
        }
    }

    private void showLoading() {
        mView.showLoading(StringUtil.getString(R.string.string_load_all_images));
    }

    private void preLoadImages() {
        int endIndex = 0;
        if (mAllImages.size() >= 10) {
            endIndex = 10;
        } else {
            endIndex = mAllImages.size();
        }
        mSubscription = createBitmaps(mAllImages.subList(0, endIndex))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ShowBitmap>>() {
                    @Override
                    public void call(List<ShowBitmap> bitmaps) {
                        mView.showImages(bitmaps);
                        mView.dismissLoading();
                    }
                });
    }

    private Observable<List<ShowBitmap>> createBitmaps(List<LocalMedia> datas) {
        return Observable.just(datas)
                .subscribeOn(Schedulers.io())
                .map(new Func1<List<LocalMedia>, List<ShowBitmap>>() {
                    @Override
                    public List<ShowBitmap> call(List<LocalMedia> localMedias) {
                        List<ShowBitmap> data = new ArrayList<>();
                        for (LocalMedia localMedia : localMedias) {
                            Bitmap bitmap = ImageUtil.decodeFile(localMedia.getPath());
                            String path = ImageConfig.CONSTANT_IMAGE_PATH + System.currentTimeMillis() + "copress_temp.png";
                            ImageUtil.compressImage(bitmap, 65, path, true);
                            data.add(new ShowBitmap(path, ImageUtil.decodeFile(path)));
                        }
                        return data;
                    }
                })
                .flatMap(new Func1<List<ShowBitmap>, Observable<List<ShowBitmap>>>() {
                    @Override
                    public Observable<List<ShowBitmap>> call(List<ShowBitmap> bitmaps) {
                        return Observable.just(bitmaps);
                    }
                });
    }
}
