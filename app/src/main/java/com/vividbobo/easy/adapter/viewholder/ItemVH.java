package com.vividbobo.easy.adapter.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.Itemzable;
import com.vividbobo.easy.utils.ResourceUtils;

public class ItemVH<T extends Itemzable> extends RecyclerView.ViewHolder {
    public ImageView iconIv;
    public TextView titleTv, descTv;
    private boolean enableIcon;


    public ItemVH(@NonNull View itemView) {
        super(itemView);
        iconIv = itemView.findViewById(R.id.item_icon_iv);
        titleTv = itemView.findViewById(R.id.item_title_tv);
        descTv = itemView.findViewById(R.id.item_desc_tv);
    }

    public void bind(Context context, T entity) {
        if (enableIcon) {
            iconIv.setVisibility(View.VISIBLE);
            if (entity.getItemIconResName() == null || entity.getItemIconResName().isEmpty()) {
                ResourceUtils.bindImageDrawable(context, ResourceUtils.getTextImageIcon(entity.getItemTitle(), ResourceUtils.getColor(R.color.black))).centerCrop().into(iconIv);
            } else {
                ResourceUtils.bindImageDrawable(context, ResourceUtils.getDrawable(entity.getItemIconResName())).centerCrop().into(iconIv);
            }
        } else {
            iconIv.setVisibility(View.GONE);
        }

        titleTv.setText(entity.getItemTitle());
        if (entity.getItemDesc().isEmpty()) {
            descTv.setVisibility(View.GONE);
        } else {
            descTv.setVisibility(View.VISIBLE);
            descTv.setText(entity.getItemDesc());
        }
    }

    public void setEnableIcon(boolean enableIcon) {
        this.enableIcon = enableIcon;
    }
}