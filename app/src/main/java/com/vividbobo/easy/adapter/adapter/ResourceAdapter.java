package com.vividbobo.easy.adapter.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.Itemzable;
import com.vividbobo.easy.adapter.viewholder.GridItemVH;
import com.vividbobo.easy.ui.account.ResourceBottomSheet;
import com.vividbobo.easy.ui.others.commonAdapter.CommonAdapter;

public class ResourceAdapter<T extends Itemzable> extends CommonAdapter<T, RecyclerView.ViewHolder, GridItemVH<T>, RecyclerView.ViewHolder> {

    @LayoutRes
    private int itemLayoutRes = 0;

    public ResourceAdapter(Context mContext) {
        super(mContext);
    }

    public ResourceAdapter(Context mContext, @LayoutRes int itemLayoutRes) {
        super(mContext);
        this.itemLayoutRes = itemLayoutRes;
    }


    @Override
    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateFooterViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected GridItemVH<T> onCreateNormalViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (itemLayoutRes == 0) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item1, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(itemLayoutRes, parent, false);
        }
        return new GridItemVH<>(v);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull GridItemVH<T> holder, int position) {
        Log.d("TAG", "onBindNormalViewHolder: bind grid bottom sheet");
        T item = getItemByHolderPosition(position);
        holder.bind(mContext, item);
    }

    @Override
    protected void onBindHeaderViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    protected void onBindFooterViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }
}
