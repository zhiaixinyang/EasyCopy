package com.mdove.easycopy.activity.history.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.mdove.easycopy.R;
import com.mdove.easycopy.databinding.ItemHistoryResultOcrBinding;
import com.mdove.easycopy.activity.history.model.HistoryResultOCRModel;
import com.mdove.easycopy.activity.history.model.handler.HistoryResultOCRHandler;
import com.mdove.easycopy.activity.history.model.vm.HistoryResultOCRModelVM;
import com.mdove.easycopy.activity.history.presenter.HistoryResultOCRPresenter;
import com.mdove.easycopy.utils.InflateUtils;

import java.util.List;

public class HistoryResultOCRAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<HistoryResultOCRModel> mData;
    private HistoryResultOCRPresenter mPresenter;

    public HistoryResultOCRAdapter(HistoryResultOCRPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder((ItemHistoryResultOcrBinding) InflateUtils.bindingInflate(parent, R.layout.item_history_result_ocr));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setData(List<HistoryResultOCRModel> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemHistoryResultOcrBinding mBinding;

        public ViewHolder(ItemHistoryResultOcrBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(HistoryResultOCRModel model) {
            mBinding.setVm(new HistoryResultOCRModelVM(model));
            mBinding.setHandler(new HistoryResultOCRHandler(mPresenter));
        }
    }

    public void notifyPositionForId(long id) {
        int position = -1;
        for (HistoryResultOCRModel model : mData) {
            if (model.mId == id) {
                position = mData.indexOf(model);
            }
        }
        if (position != -1) {
            deleteNotifyPosition(position);
        }
    }

    private void deleteNotifyPosition(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }
}
