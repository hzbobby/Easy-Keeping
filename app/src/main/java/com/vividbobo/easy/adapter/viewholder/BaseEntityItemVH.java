package com.vividbobo.easy.adapter.viewholder;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.BaseEntity;
import com.vividbobo.easy.utils.ResourceUtils;

public class BaseEntityItemVH extends RecyclerView.ViewHolder {
    private ImageView iconIv;
    private TextView titleTv, descTv;

    public BaseEntityItemVH(@NonNull View itemView) {
        super(itemView);
        iconIv = itemView.findViewById(R.id.item_icon_iv);
        titleTv = itemView.findViewById(R.id.item_title_tv);
        descTv = itemView.findViewById(R.id.item_desc_tv);
    }

    public void bind(BaseEntity entity) {
        if (entity.getIconResName() == null || entity.getIconResName().isEmpty()) {
            iconIv.setImageDrawable(ResourceUtils.getTextImageIcon(entity.getTitle(), Color.BLACK));
        } else {
            iconIv.setImageDrawable(ResourceUtils.getDrawable(entity.getIconResName()));
        }
        titleTv.setText(entity.getTitle());
        if (entity.getDesc().isEmpty()) {
            descTv.setVisibility(View.GONE);
        } else {
            descTv.setVisibility(View.VISIBLE);
            descTv.setText(entity.getDesc());
        }
    }

}
