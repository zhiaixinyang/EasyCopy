package com.mdove.easycopy.activity.allimages.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.Log;

import com.gerenvip.filescaner.FileInfo;
import com.gerenvip.filescaner.FileScanner;
import com.gerenvip.filescaner.FileScannerAdapterListener;
import com.mdove.easycopy.App;
import com.mdove.easycopy.R;
import com.mdove.easycopy.activity.allimages.model.ShowBitmap;
import com.mdove.easycopy.activity.allimages.presenter.contract.AllImagesContract;
import com.mdove.easycopy.config.ImageConfig;
import com.mdove.easycopy.greendao.AllImagesDao;
import com.mdove.easycopy.greendao.entity.AllImages;
import com.mdove.easycopy.loadimges.LocalMedia;
import com.mdove.easycopy.utils.ImageUtil;
import com.mdove.easycopy.utils.StringUtil;
import com.mdove.easycopy.utils.ToastHelper;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class AllImagesPresenter implements AllImagesContract.Presenter {
    private AllImagesContract.MvpView mView;
    private Subscription mSubscription;
    private List<LocalMedia> mAllImagesOld;
    private List<FileInfo> mAllImages;
    private int mCurIndexStart = 0;
    private static final int COUNT_IMAGES = 10;
    private AllImagesDao mAllImagesDao;

    public AllImagesPresenter() {
        mAllImagesDao = App.getDaoSession().getAllImagesDao();
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
        FileScanner fileScanner = new FileScanner(mView.getContext(), FileScanner.SUPPORT_FILE_TYPE_IMAGE);
        fileScanner.setFileScannerListener(new FileScannerAdapterListener() {

            @Override
            public void onScanEnd(@Nullable List<FileInfo> allFiles) {
                mAllImages = allFiles;
                preLoadImages();
            }
        });
        fileScanner.scan(false);
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

    private Observable<List<ShowBitmap>> createBitmaps(List<FileInfo> datas) {
        return Observable.just(datas)
                .subscribeOn(Schedulers.io())
                .map(new Func1<List<FileInfo>, List<ShowBitmap>>() {
                    @Override
                    public List<ShowBitmap> call(List<FileInfo> localMedias) {
                        List<ShowBitmap> data = new ArrayList<>();
                        for (FileInfo fileInfo : localMedias) {
                            String path = fileInfo.getFilePath();
                            Bitmap bitmap = ImageUtil.decodeFile(path);
                            String[] names = path.split("/");
                            String tempPath = ImageConfig.CONSTANT_IMAGE_PATH + "copress_temp_" + names[names.length - 1];
                            Bitmap compressBitmap;
                            boolean isCompressSuc = false;
                            if (!isExistCompressImage(tempPath)) {
                                isCompressSuc = ImageUtil.compressImage(bitmap, 65, tempPath, true);
                                bitmap.recycle();
                                if (!isCompressSuc) {
                                    compressBitmap = BitmapFactory.decodeFile(path);
                                } else {
                                    compressBitmap = BitmapFactory.decodeFile(tempPath);

                                    AllImages allImages = new AllImages();
                                    allImages.mPath = tempPath;
                                    allImages.mHeight = compressBitmap.getHeight();
                                    allImages.mWidth = compressBitmap.getWidth();
                                    mAllImagesDao.insert(allImages);
                                }
                            } else {
                                compressBitmap = BitmapFactory.decodeFile(tempPath);
                            }

                            data.add(new ShowBitmap(path, compressBitmap));
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

    private boolean isExistCompressImage(String tempPath) {
        AllImages allImages = mAllImagesDao.queryBuilder().where(AllImagesDao.Properties.MPath.eq(tempPath)).unique();
        if (allImages == null) {
            return false;
        } else {
            return true;
        }
    }
}
