package com.mdove.easycopy.activity.scaniamges;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mdove.easycopy.R;
import com.mdove.easycopy.activity.allimages.model.ShowBitmap;
import com.mdove.easycopy.activity.scaniamges.adapter.ScanImagesAdapter;
import com.mdove.easycopy.activity.scaniamges.bean.ImageFloder;
import com.mdove.easycopy.activity.scaniamges.presenter.contract.ScanImagesContract;
import com.mdove.easycopy.activity.scaniamges.utils.ListImageDirPopupWindow;
import com.mdove.easycopy.base.BaseActivity;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

//TODO
public class ScanImageActivity_ extends BaseActivity implements ScanImagesContract.MvpView, ListImageDirPopupWindow.OnImageDirSelected {
    private ProgressDialog mProgressDialog;

    /**
     * 存储文件夹中的图片数量
     */
    private int mPicsSize;
    /**
     * 图片数量最多的文件夹
     */
    private File mImgDir;
    /**
     * 所有的图片
     */
    private List<String> mImgs;

    private RecyclerView mRLV;
    private ScanImagesAdapter mAdapter;
    private RelativeLayout mBottomLy;

    private TextView mChooseDir;
    private TextView mImageCount;
    int totalCount = 0;

    private int mScreenHeight;

    private ListImageDirPopupWindow mListImageDirPopupWindow;

    public static void start(Context context) {
        Intent intent = new Intent(context, ScanImageActivity_.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * 为View绑定数据
     */
    private void data2View() {
        if (mImgDir == null) {
            Toast.makeText(getApplicationContext(), "擦，一张图片没扫描到",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        mImgs = Arrays.asList(mImgDir.list());
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        mAdapter = new ScanImagesAdapter(getApplicationContext(), mImgs,
                R.layout.item_grid_scan_image, mImgDir.getAbsolutePath());
//        mRLV.setAdapter(mAdapter);
    }

    private void initListDirPopupWindw(List<ImageFloder> imageFloders) {
        mListImageDirPopupWindow = new ListImageDirPopupWindow(
                LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.7),
                imageFloders, LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.list_dir, null));

        mListImageDirPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        // 设置选择文件夹的回调
        mListImageDirPopupWindow.setOnImageDirSelected(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_images);

        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;

        initView();
        initEvent();
    }

    @Override
    protected boolean isNeedCustomLayout() {
        return false;
    }

    /**
     * 初始化View
     */
    private void initView() {
        mRLV = (RecyclerView) findViewById(R.id.id_gridView);
        mChooseDir = (TextView) findViewById(R.id.id_choose_dir);
        mImageCount = (TextView) findViewById(R.id.id_total_count);

        mBottomLy = (RelativeLayout) findViewById(R.id.id_bottom_ly);
    }

    private void initEvent() {
        /**
         * 为底部的布局设置点击事件，弹出popupWindow
         */
        mBottomLy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListImageDirPopupWindow
                        .setAnimationStyle(R.style.anim_popup_dir);
                mListImageDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);

                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = .3f;
                getWindow().setAttributes(lp);
            }
        });
    }

    @Override
    public void selected(ImageFloder floder) {
        mImgDir = new File(floder.getDir());
        mImgs = Arrays.asList(mImgDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg") || filename.endsWith(".png")
                        || filename.endsWith(".jpeg"))
                    return true;
                return false;
            }
        }));
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        mAdapter = new ScanImagesAdapter(getApplicationContext(), mImgs,
                R.layout.item_grid_scan_image, mImgDir.getAbsolutePath());
//        mRLV.setAdapter(mAdapter);
        // mAdapter.notifyDataSetChanged();
        mImageCount.setText(floder.getCount() + "张");
        mChooseDir.setText(floder.getName());
        mListImageDirPopupWindow.dismiss();
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void showImages(List<ShowBitmap> allImages) {

    }

    @Override
    public void showImagesCount(int maxCount) {
        mImageCount.setText(totalCount + "张");
    }

    @Override
    public void initImageFloders(List<ImageFloder> allImageFloder) {
        initListDirPopupWindw(allImageFloder);
    }
}
