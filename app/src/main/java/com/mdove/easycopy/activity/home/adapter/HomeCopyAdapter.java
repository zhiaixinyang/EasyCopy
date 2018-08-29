package com.mdove.easycopy.activity.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.mdove.easycopy.R;
import com.mdove.easycopy.databinding.ItemHomeCopyBinding;
import com.mdove.easycopy.activity.home.model.CopyDataModel;
import com.mdove.easycopy.activity.home.model.vm.CopyDataModelVM;
import com.mdove.easycopy.utils.InflateUtils;

import java.util.List;

public class HomeCopyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CopyDataModel> mData;
    private Context mContext;

    public HomeCopyAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder((ItemHomeCopyBinding) InflateUtils.bindingInflate(parent, R.layout.item_home_copy));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CopyDataModel model = mData.get(position);
        ((ViewHolder) holder).bind(new CopyDataModelVM(model));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemHomeCopyBinding mBinding;

        public ViewHolder(ItemHomeCopyBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(CopyDataModelVM vm) {
            mBinding.setVm(vm);
        }
    }

    public void setData(List<CopyDataModel> data) {
        mData = data;
        notifyDataSetChanged();
    }
}

