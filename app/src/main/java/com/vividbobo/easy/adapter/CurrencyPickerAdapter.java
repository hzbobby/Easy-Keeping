package com.vividbobo.easy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.viewholder.BaseEntityFooterVH;
import com.vividbobo.easy.adapter.viewholder.BaseEntityItemVH;
import com.vividbobo.easy.database.model.Currency;
import com.vividbobo.easy.ui.others.commonAdapter.CommonAdapter;
import com.vividbobo.easy.utils.ResourceUtils;

public class CurrencyPickerAdapter extends CommonAdapter<Currency, RecyclerView.ViewHolder, BaseEntityItemVH, BaseEntityFooterVH> {
    public CurrencyPickerAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected BaseEntityFooterVH onCreateFooterViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item1, parent, false);
        return new BaseEntityFooterVH(view);
    }

    @Override
    protected BaseEntityItemVH onCreateNormalViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2, parent, false);
        return new BaseEntityItemVH(view);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull BaseEntityItemVH holder, int position) {
        Currency currency = getItemByHolderPosition(position);
        holder.titleTv.setText(currency.getTitle());
        holder.descTv.setText(currency.getCode());
        ResourceUtils.bindImageDrawable(mContext, ResourceUtils.getTextImageIcon(currency.getTitle(), ResourceUtils.getColor(R.color.black)))
                .fitCenter().into(holder.iconIv);
    }

    @Override
    protected void onBindHeaderViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    protected void onBindFooterViewHolder(@NonNull BaseEntityFooterVH holder, int position) {
        //do nothing
    }
}
