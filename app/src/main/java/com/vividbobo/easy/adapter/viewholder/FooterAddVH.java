package com.vividbobo.easy.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.utils.ResourceUtils;

public class FooterAddVH extends RecyclerView.ViewHolder {
    public ImageView iconIv;
    public TextView titleTv;

    public FooterAddVH(@NonNull View itemView) {
        super(itemView);
        iconIv = itemView.findViewById(R.id.item_icon_iv);
        titleTv = itemView.findViewById(R.id.item_title_tv);
    }

    public void bind(Context context, String title) {
        ResourceUtils.bindImageDrawable(context, ResourceUtils.getDrawable(R.drawable.ic_add))
                .centerInside().into(iconIv);
        titleTv.setText(title);
    }
}
