package com.vividbobo.easy.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.viewholder.BaseEntityFooterVH;
import com.vividbobo.easy.adapter.viewholder.BaseEntityItemVH;
import com.vividbobo.easy.adapter.viewholder.TagVH;
import com.vividbobo.easy.database.model.Tag;
import com.vividbobo.easy.ui.others.commonAdapter.CommonAdapter;
import com.vividbobo.easy.utils.ResourceUtils;

/**
 * adapter for TagFullDialog
 */
public class TagAdapter extends CommonAdapter<Tag, RecyclerView.ViewHolder, TagVH, RecyclerView.ViewHolder> {
    public TagAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateFooterViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item1, parent, false);
        return new BaseEntityFooterVH(v);
    }

    @Override
    protected TagVH onCreateNormalViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item1, parent, false);
        return new TagVH(view);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull TagVH holder, int position) {
        Tag tag = getItemByHolderPosition(position);
        holder.bind(mContext, tag);
    }

    @Override
    protected void onBindHeaderViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    protected void onBindFooterViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }
}
