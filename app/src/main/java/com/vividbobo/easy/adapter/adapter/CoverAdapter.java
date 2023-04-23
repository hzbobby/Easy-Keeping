package com.vividbobo.easy.adapter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.viewholder.CoverVH;
import com.vividbobo.easy.database.model.Resource;
import com.vividbobo.easy.ui.others.commonAdapter.CommonAdapter;

public class CoverAdapter extends CommonAdapter<Resource, RecyclerView.ViewHolder, CoverVH, RecyclerView.ViewHolder> {
    public CoverAdapter(Context mContext) {
        super(mContext);
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
    protected CoverVH onCreateNormalViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cover, parent, false);
        return new CoverVH(view);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull CoverVH holder, int position) {
        Resource resource = getItemByHolderPosition(position);
        holder.bind(mContext, resource);
    }

    @Override
    protected void onBindHeaderViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    protected void onBindFooterViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }
}
