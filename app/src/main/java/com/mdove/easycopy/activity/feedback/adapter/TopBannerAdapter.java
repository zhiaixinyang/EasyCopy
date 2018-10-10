package com.mdove.easycopy.activity.feedback.adapter;

import android.content.Context;
import android.widget.TextView;

import com.mdove.easycopy.R;
import com.mdove.easycopy.ui.marqueeview.base.CommonAdapter;
import com.mdove.easycopy.ui.marqueeview.base.ItemViewDelegate;
import com.mdove.easycopy.ui.marqueeview.base.ViewHolder;

import java.util.List;

/**
 * Created by MDove on 2018/9/16.
 */

public class TopBannerAdapter extends CommonAdapter<String> {

    public TopBannerAdapter(Context context, List<String> datas) {
        super(context, R.layout.item_feedback_top_banner, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, String item, int position) {
        TextView tv = viewHolder.getView(R.id.tv_content);
        tv.setText(item);
    }

}
