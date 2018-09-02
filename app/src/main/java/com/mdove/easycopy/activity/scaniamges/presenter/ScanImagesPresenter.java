package com.mdove.easycopy.activity.scaniamges.presenter;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;

import com.gerenvip.filescaner.FileInfo;
import com.gerenvip.filescaner.FileScanner;
import com.gerenvip.filescaner.FileScannerAdapterListener;
import com.mdove.easycopy.App;
import com.mdove.easycopy.R;
import com.mdove.easycopy.activity.allimages.model.ShowBitmap;
import com.mdove.easycopy.activity.scaniamges.ScanImageActivity;
import com.mdove.easycopy.activity.scaniamges.bean.ImageFloder;
import com.mdove.easycopy.activity.scaniamges.bean.ScanImageModel;
import com.mdove.easycopy.activity.scaniamges.presenter.contract.ScanImagesContract;
import com.mdove.easycopy.config.ImageConfig;
import com.mdove.easycopy.greendao.AllImagesDao;
import com.mdove.easycopy.greendao.entity.AllImages;
import com.mdove.easycopy.loadimges.LocalMedia;
import com.mdove.easycopy.utils.ImageUtil;
import com.mdove.easycopy.utils.StringUtil;
import com.mdove.easycopy.utils.ToastHelper;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import rx.Emitter;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.SyncOnSubscribe;
import rx.schedulers.Schedulers;

public class ScanImagesPresenter implements ScanImagesContract.Presenter {
    private ScanImagesContract.MvpView mView;
    private Subscription mSubscription;
    private AllImagesDao mAllImagesDao;

    public ScanImagesPresenter() {
        mAllImagesDao = App.getDaoSession().getAllImagesDao();
    }

    @Override
    public void subscribe(ScanImagesContract.MvpView view) {
        mView = view;
    }

    @Override
    public void unSubscribe() {
        mSubscription.unsubscribe();
    }

    @Override
    public void initImages() {
        showLoading();
        Observable.create(new SyncOnSubscribe<Integer, ScanImageModel>() {
            @Override
            protected Integer generateState() {
                return null;
            }

            @Override
            protected Integer next(Integer state, Observer<? super ScanImageModel> observer) {
                HashSet<String> dirPaths = new HashSet<String>();
                List<ImageFloder> imageFloders = new ArrayList<ImageFloder>();
                int picsSize = 0;
                int totalCount = 0;
                File imgDir = null;

                String firstImage = null;
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = mView.getContext().getContentResolver();

                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png", "image/jpg"},
                        MediaStore.Images.Media.DATE_MODIFIED);

                Log.e("TAG", mCursor.getCount() + "");
                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));

                    Log.e("TAG", path);
                    // 拿到第一张图片的路径
                    if (firstImage == null)
                        firstImage = path;
                    // 获取该图片的父路径名
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;
                    String dirPath = parentFile.getAbsolutePath();
                    ImageFloder imageFloder = null;
                    // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                    if (dirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        dirPaths.add(dirPath);
                        // 初始化imageFloder
                        imageFloder = new ImageFloder();
                        imageFloder.setDir(dirPath);
                        imageFloder.setFirstImagePath(path);
                    }

                    String[] arr = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg")
                                    || filename.endsWith(".png")
                                    || filename.endsWith(".jpeg"))
                                return true;
                            return false;
                        }
                    });

                    if (arr == null) {
                        continue;
                    }

                    int picSize = arr.length;
                    totalCount += picSize;

                    imageFloder.setCount(picSize);
                    imageFloders.add(imageFloder);

                    if (picSize > picsSize) {
                        picsSize = picSize;
                        imgDir = parentFile;
                    }
                }
                mCursor.close();

                observer.onNext(new ScanImageModel(imageFloders, Arrays.asList(imgDir.list()),totalCount));
                return 1;
            }
        }).map(new Func1<ScanImageModel, List<String>>() {
            @Override
            public List<String> call(ScanImageModel scanImageModel) {
                mView.showImagesCount(scanImageModel.mTotalCount);
                mView.initImageFloders(scanImageModel.mImageFloders);
                return null;
            }
        }).flatMap(new Func1<List<String>, Observable<List<ShowBitmap>>>() {
            @Override
            public Observable<List<ShowBitmap>> call(List<String> strings) {
                return createBitmaps(strings);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ShowBitmap>>() {
                    @Override
                    public void call(List<ShowBitmap> showBitmaps) {
                        mView.showImages(showBitmaps);
                        mView.dismissLoading();
                    }
                });
    }

    private void showLoading() {
        mView.showLoading(StringUtil.getString(R.string.string_load_all_images));
    }

    private Observable<List<ShowBitmap>> createBitmaps(List<String> datas) {
        return Observable.just(datas)
                .subscribeOn(Schedulers.io())
                .map(new Func1<List<String>, List<ShowBitmap>>() {
                    @Override
                    public List<ShowBitmap> call(List<String> localMedias) {
                        List<ShowBitmap> data = new ArrayList<>();
                        for (String path : localMedias) {
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
