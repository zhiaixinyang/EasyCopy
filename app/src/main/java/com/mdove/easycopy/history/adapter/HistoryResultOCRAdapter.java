package com.mdove.easycopy.history.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.mdove.easycopy.R;
import com.mdove.easycopy.databinding.ItemHistoryResultOcrBinding;
import com.mdove.easycopy.history.model.HistoryResultOCRModel;
import com.mdove.easycopy.history.model.vm.HistoryResultOCRModelVM;
import com.mdove.easycopy.utils.InflateUtils;

import java.util.List;

public class HistoryResultOCRAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<HistoryResultOCRModel> mData;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder((ItemHistoryResultOcrBinding) InflateUtils.bindingInflate(parent, R.layout.item_history_result_ocr));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).bind(mData.get(position));
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
        }
    }
}