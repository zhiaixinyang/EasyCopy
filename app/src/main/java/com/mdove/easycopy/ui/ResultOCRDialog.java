package com.mdove.easycopy.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.mdove.easycopy.R;
import com.mdove.easycopy.utils.ScreenUtils;

/**
 * @author MDove on 2018/1/28.
 */
public class ResultOCRDialog extends BottomSheetDialog {
    private TextView mTvContent;
    private Context mCxt;
    private String mContent;

    public static void show(Context context, String content) {
        new ResultOCRDialog(context, content).show();
    }

    public ResultOCRDialog(@NonNull Context context, String content) {
        super(context, R.style.ResultOCRDialog);
        mCxt = context;
        mContent = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_result_ocr);
        mTvContent = findViewById(R.id.tv_content);
        int screenHeight = ScreenUtils.getScreenHeight();
        int statusBarHeight = ScreenUtils.getStatusHeight();
        int dialogHeight = screenHeight - statusBarHeight;
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);

        mTvContent.setText(mContent);
    }
}
