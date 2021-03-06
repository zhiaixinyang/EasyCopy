package com.mdove.easycopy.activity.resultocr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;

import com.mdove.easycopy.R;
import com.mdove.easycopy.activity.feedback.FeedBackActivity;
import com.mdove.easycopy.activity.resultocr.model.handler.ResultOCRHandler;
import com.mdove.easycopy.base.BaseActivity;
import com.mdove.easycopy.activity.crop.Crop;
import com.mdove.easycopy.databinding.ActivityResultOcrBinding;
import com.mdove.easycopy.activity.resultocr.model.ResultOCRModel;
import com.mdove.easycopy.activity.resultocr.model.ResultOCRModelVM;
import com.mdove.easycopy.activity.resultocr.presenter.ResultOCRPresenter;
import com.mdove.easycopy.activity.resultocr.presenter.contract.ResultOCRContract;
import com.mdove.easycopy.utils.ClipboardUtils;
import com.mdove.easycopy.utils.StringUtil;
import com.mdove.easycopy.utils.ToastHelper;

import java.io.File;
import java.util.List;

public class ResultOCRActivity extends BaseActivity implements ResultOCRContract.MvpView {
    public static final int INTENT_TYPE_RESULT_OCR = 1;
    public static final int INTENT_TYPE_START_OCR = 2;
    public static final String ACTION_RESULT_OCR_CONTENT = "action_result_ocr_content";
    public static final String ACTION_START_OCR = "action_start_ocr";
    public static final String ACTION_START_OCR_SELECT_IMAGES = "action_start_ocr_select_images";
    public static final String ACTION_START_SILENT_OCR = "action_start_silent_ocr";
    public static final String EXTRA_RESULT_OCR_CONTENT = "extra_result_ocr_content";
    public static final String EXTRA_START_OCR_IMAGE_PATH = "extra_start_ocr_image_path";
    public static final String EXTRA_START_OCR_SELECT_IMAGES_PATH = "extra_start_ocr_select_images_path";
    public static final String EXTRA_INTENT_TYPE = "extra_intent_type";

    private ActivityResultOcrBinding mBinding;
    private ResultOCRPresenter mPresenter;
    private int mIntentType = -1;

    public static void start(Context context, String content, int intentType) {
        Intent intent = new Intent(context, ResultOCRActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        switch (intentType) {
            case INTENT_TYPE_RESULT_OCR: {
                intent.setAction(ACTION_RESULT_OCR_CONTENT);
                intent.putExtra(EXTRA_RESULT_OCR_CONTENT, content);
                intent.putExtra(EXTRA_INTENT_TYPE, intentType);
                break;
            }
            case INTENT_TYPE_START_OCR: {
                intent.setAction(ACTION_START_OCR);
                intent.putExtra(EXTRA_START_OCR_IMAGE_PATH, content);
                intent.putExtra(EXTRA_INTENT_TYPE, intentType);
                break;
            }
            default: {
                break;
            }
        }
        context.startActivity(intent);
    }

    @Override
    protected boolean isNeedCustomLayout() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_result_ocr);
        initToolbar();
        mPresenter = new ResultOCRPresenter();
        mPresenter.subscribe(this);
        mBinding.setHandler(new ResultOCRHandler(mPresenter));
        intentHandle(getIntent());
    }

    private void initToolbar() {
        mBinding.toolbar.setTitle(getString(R.string.string_activity_result_ocr_title));
        setSupportActionBar(mBinding.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void intentHandle(Intent intent) {
        if (intent == null) {
            return;
        }
        mIntentType = intent.getIntExtra(EXTRA_INTENT_TYPE, -1);
        String action = intent.getAction();
        switch (action) {
            case ACTION_RESULT_OCR_CONTENT: {
                String content = intent.getStringExtra(EXTRA_RESULT_OCR_CONTENT);
                if (!TextUtils.isEmpty(content)) {
                    mBinding.setVm(new ResultOCRModelVM(new ResultOCRModel(content, "")));
                }
                break;
            }
            case ACTION_START_OCR: {
                String path = intent.getStringExtra(EXTRA_START_OCR_IMAGE_PATH);
                beginCrop(Uri.fromFile(new File(path)));
                break;
            }
            case ACTION_START_OCR_SELECT_IMAGES: {
                List<String> selectImages = intent.getStringArrayListExtra(EXTRA_START_OCR_SELECT_IMAGES_PATH);
                if (selectImages != null) {
                    mPresenter.startOCRForList(selectImages);
                }
                break;
            }
            default: {

                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped.png"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            mPresenter.startOCR(Crop.getOutput(result).getPath(), mIntentType);
        } else if (resultCode == Crop.RESULT_ERROR) {
            ToastHelper.shortToast(Crop.getError(result).getMessage());
        } else {
            finish();
        }
    }

    @Override
    public void showResult(ResultOCRModel model) {
        mBinding.setVm(new ResultOCRModelVM(model));
    }

    @Override
    public void registerOcrError(final String content) {
        Snackbar.make(mBinding.getRoot(), content, Snackbar.LENGTH_LONG).setAction("反馈", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardUtils.copyToClipboard(ResultOCRActivity.this, content);
                FeedBackActivity.start(ResultOCRActivity.this);
                ToastHelper.showToastCenter(StringUtil.getString(R.string.string_error_register_ocr));
                finish();
            }
        }).addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                finish();
            }
        }).show();
    }

    @Override
    public Context getContext() {
        return this;
    }
}
