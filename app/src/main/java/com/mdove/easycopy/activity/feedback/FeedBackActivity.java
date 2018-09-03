package com.mdove.easycopy.activity.feedback;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mdove.easycopy.R;
import com.mdove.easycopy.activity.feedback.presenter.FeedBackPresenter;
import com.mdove.easycopy.activity.feedback.presenter.contract.FeedBackContract;
import com.mdove.easycopy.activity.scaniamges.ScanImageActivity;
import com.mdove.easycopy.base.BaseActivity;
import com.mdove.easycopy.utils.ToastHelper;

public class FeedBackActivity extends BaseActivity implements FeedBackContract.MvpView {
    private TextView mBtnOk;
    private EditText mEtContent;
    private FeedBackPresenter mPresenter;

    public static void start(Context context) {
        Intent intent = new Intent(context, FeedBackActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected boolean isNeedCustomLayout() {
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mBtnOk = findViewById(R.id.btn_ok);
        mEtContent = findViewById(R.id.et_content);

        mPresenter = new FeedBackPresenter();
        mPresenter.subscribe(this);

        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mEtContent.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    mPresenter.sendFeedBack(content);
                } else {
                    ToastHelper.shortToast(getString(R.string.string_sendback_content_is_empty));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }

    @Override
    public void sendFeedBackReturn(int status) {
        if (status == 1) {
            mEtContent.setText("");
            ToastHelper.shortToast(getString(R.string.string_feedback_suc));
        } else if (status == 0) {
            ToastHelper.shortToast(getString(R.string.string_feedback_err));
        }
    }

    @Override
    public Context getContext() {
        return this;
    }
}
