package com.mdove.easycopy.update;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.mdove.easycopy.R;
import com.mdove.easycopy.databinding.DialogUpdateBinding;
import com.mdove.easycopy.utils.SystemUtils;


/**
 * Created by MDove on 2018/8/28.
 */
public class UpdateDialog extends AppCompatDialog {
    private DialogUpdateBinding mBinding;
    private String mAppUrl;
    private float star = 0;
    private Context mContext;

    public UpdateDialog(Context context, String appUrl) {
        super(context, R.style.UpgradeDialog);
        mContext = context;
        mAppUrl = appUrl;
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_update,
                null, false);
        setContentView(mBinding.getRoot());
        WindowManager.LayoutParams paramsWindow = getWindow().getAttributes();
        paramsWindow.width = getWindowWidth();
        setCancelable(true);
        setCanceledOnTouchOutside(false);
    }

    protected int getWindowWidth() {
        float percent = 0.9f;
        WindowManager wm = this.getWindow().getWindowManager();
        int screenWidth = SystemUtils.getScreenWidth(wm);
        int screenHeight = SystemUtils.getScreenHeight(wm);
        return (int) (screenWidth > screenHeight
                ? screenHeight * percent
                : screenWidth * percent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                UpdateAppService.start(mContext, mAppUrl);
            }
        });
    }
}
