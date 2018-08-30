package com.mdove.easycopy.activity.home.model.vm;

import android.databinding.ObservableField;

import com.mdove.easycopy.R;
import com.mdove.easycopy.utils.StringUtil;

public class MainStatisticsModelVM {
    public ObservableField<String> mOcrSucCount = new ObservableField<>();
    public ObservableField<String> mOcrWordsCount = new ObservableField<>();
    public ObservableField<String> mOcrCount = new ObservableField<>();

    public MainStatisticsModelVM(long ocrSucCount, long ocrWordsCount, long ocrCount){
        mOcrCount.set(String.format(StringUtil.getString(R.string.string_ocr_count),ocrCount));
        mOcrWordsCount.set(String.format(StringUtil.getString(R.string.string_ocr_words_count),ocrWordsCount));
        mOcrSucCount.set(String.format(StringUtil.getString(R.string.string_ocr_suc_count),ocrSucCount));
    }
}
