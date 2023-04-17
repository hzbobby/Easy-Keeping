package com.vividbobo.easy.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.utils.ResourceUtils;

public class BaseEntityFooterVH extends RecyclerView.ViewHolder {
    private ImageView iconIv;
    private TextView titleTv, descTv;

    public BaseEntityFooterVH(@NonNull View itemView) {
        super(itemView);
        iconIv = itemView.findViewById(R.id.item_icon_iv);
        titleTv = itemView.findViewById(R.id.item_title_tv);
        descTv = itemView.findViewById(R.id.item_desc_tv);
        descTv.setVisibility(View.GONE);
        titleTv.setText(R.string.add);
        ResourceUtils.bindImageDrawable(itemView.getContext(),
                ResourceUtils.getTintedDrawable(R.drawable.ic_add, ResourceUtils.getColor(R.color.black))).centerCrop().into(iconIv);
    }
}
