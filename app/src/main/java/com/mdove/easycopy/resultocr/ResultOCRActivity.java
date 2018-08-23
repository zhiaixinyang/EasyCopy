package com.mdove.easycopy.resultocr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.mdove.easycopy.R;
import com.mdove.easycopy.base.BaseActivity;
import com.mdove.easycopy.databinding.ActivityResultOcrBinding;
import com.mdove.easycopy.resultocr.model.ResultOCRModel;
import com.mdove.easycopy.resultocr.model.ResultOCRModelVM;
import com.mdove.easycopy.resultocr.presenter.ResultOCRPresenter;
import com.mdove.easycopy.resultocr.presenter.contract.ResultOCRContract;

public class ResultOCRActivity extends BaseActivity implements ResultOCRContract.MvpView {
    public static final int INTENT_TYPE_RESULT_OCR = 1;
    public static final int INTENT_TYPE_START_OCR = 2;
    public static final String ACTION_RESULT_OCR_CONTENT = "action_result_ocr_content";
    public static final String ACTION_START_OCR_IMAGE_PATH = "action_start_ocr_image_path";
    public static final String EXTRA_RESULT_OCR_CONTENT = "extra_result_ocr_content";
    public static final String EXTRA_START_OCR_IMAGE_PATH = "extra_start_ocr_image_path";

    private ActivityResultOcrBinding mBinding;
    private ResultOCRPresenter mPresenter;

    public static void start(Context context, String content, int intentType) {
        Intent intent = new Intent(context, ResultOCRActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        switch (intentType) {
            case INTENT_TYPE_RESULT_OCR: {
                intent.setAction(ACTION_RESULT_OCR_CONTENT);
                intent.putExtra(EXTRA_RESULT_OCR_CONTENT, content);
                break;
            }
            case INTENT_TYPE_START_OCR: {
                intent.setAction(ACTION_START_OCR_IMAGE_PATH);
                intent.putExtra(EXTRA_START_OCR_IMAGE_PATH, content);
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
        mPresenter = new ResultOCRPresenter();
        mPresenter.subscribe(this);
        intentHandle(getIntent());
    }

    private void intentHandle(Intent intent) {
        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        switch (action) {
            case ACTION_RESULT_OCR_CONTENT: {
                String content = intent.getStringExtra(EXTRA_RESULT_OCR_CONTENT);
                if (!TextUtils.isEmpty(content)) {
                    mBinding.setVm(new ResultOCRModelVM(new ResultOCRModel(content)));
                }
                break;
            }
            case ACTION_START_OCR_IMAGE_PATH: {
                String path = intent.getStringExtra(EXTRA_START_OCR_IMAGE_PATH);
                mPresenter.startOCR(path);
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    public void showResult(String content) {
        mBinding.setVm(new ResultOCRModelVM(new ResultOCRModel(content)));
    }

    @Override
    public Context getContext() {
        return this;
    }
}
