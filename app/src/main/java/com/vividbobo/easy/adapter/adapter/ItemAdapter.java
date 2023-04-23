package com.vividbobo.easy.adapter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.Itemzable;
import com.vividbobo.easy.adapter.viewholder.BaseEntityFooterVH;
import com.vividbobo.easy.adapter.viewholder.BaseEntityItemVH;
import com.vividbobo.easy.adapter.viewholder.FooterAddVH;
import com.vividbobo.easy.adapter.viewholder.ItemVH;
import com.vividbobo.easy.database.model.BaseEntity;
import com.vividbobo.easy.ui.account.ResourceBottomSheet;
import com.vividbobo.easy.ui.others.commonAdapter.CommonAdapter;


/**
 * @param <T> the type of items
 */
public class ItemAdapter<T extends Itemzable> extends CommonAdapter<T, RecyclerView.ViewHolder, ItemVH<T>, FooterAddVH> {
    private boolean enableIcon;

    @LayoutRes
    private int itemLayoutRes = 0;

    public ItemAdapter(Context mContext) {
        super(mContext);
    }

    public ItemAdapter(Context mContext, @LayoutRes int itemLayoutRes) {
        super(mContext);
        this.itemLayoutRes = itemLayoutRes;
    }

    public void setEnableIcon(boolean enableIcon) {
        this.enableIcon = enableIcon;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected FooterAddVH onCreateFooterViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item1, parent, false);
        return new FooterAddVH(v);
    }

    @Override
    protected ItemVH<T> onCreateNormalViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (itemLayoutRes == 0) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(itemLayoutRes, parent, false);
        }
        return new ItemVH<T>(v);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull ItemVH<T> holder, int position) {
        T entity = getItemByHolderPosition(position);
        holder.setEnableIcon(enableIcon);
        holder.bind(mContext, entity);
    }

    @Override
    protected void onBindHeaderViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    protected void onBindFooterViewHolder(@NonNull FooterAddVH holder, int position) {
        if (getFooterItem() != null) {
            holder.bind(mContext, getFooterItem().toString());
        }
    }
}