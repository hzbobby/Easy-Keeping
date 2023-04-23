package com.vividbobo.easy.adapter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.viewholder.BaseEntityFooterVH;
import com.vividbobo.easy.adapter.viewholder.BaseEntityItemVH;
import com.vividbobo.easy.database.model.BaseEntity;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.ui.others.OnItemLongClickListener;
import com.vividbobo.easy.ui.others.commonAdapter.CommonAdapter;

/**
 * better to use ItemAdapter
 *
 * @param <T>
 */
@Deprecated
public class BaseEntityAdapter<T extends BaseEntity> extends CommonAdapter<T, RecyclerView.ViewHolder, BaseEntityItemVH, BaseEntityFooterVH> {
    public BaseEntityAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected BaseEntityFooterVH onCreateFooterViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2, parent, false);
        return new BaseEntityFooterVH(v);
    }

    @Override
    protected BaseEntityItemVH onCreateNormalViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2, parent, false);
        return new BaseEntityItemVH(v);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull BaseEntityItemVH holder, int position) {
        BaseEntity entity = getItemByHolderPosition(position);
        holder.bind(entity);
    }

    @Override
    protected void onBindHeaderViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    protected void onBindFooterViewHolder(@NonNull BaseEntityFooterVH holder, int position) {

    }
}
