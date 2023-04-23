package com.vividbobo.easy.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.utils.ResourceUtils;

@Deprecated
public class BaseEntityFooterVH extends RecyclerView.ViewHolder {
    public ImageView iconIv;
    public TextView titleTv;

    public BaseEntityFooterVH(@NonNull View itemView) {
        super(itemView);
        iconIv = itemView.findViewById(R.id.item_icon_iv);
        titleTv = itemView.findViewById(R.id.item_title_tv);
        titleTv.setText(R.string.add);
        ResourceUtils.bindImageDrawable(itemView.getContext(),
                ResourceUtils.getTintedDrawable(R.drawable.ic_add, ResourceUtils.getColor(R.color.black))).centerCrop().into(iconIv);
    }
}
