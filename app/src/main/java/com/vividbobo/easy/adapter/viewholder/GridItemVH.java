package com.vividbobo.easy.adapter.viewholder;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.Itemzable;
import com.vividbobo.easy.ui.account.ResourceBottomSheet;
import com.vividbobo.easy.utils.ResourceUtils;

public class GridItemVH<T extends Itemzable> extends RecyclerView.ViewHolder {
    private TextView titleTv;
    private ImageView iconIv;


    public GridItemVH(@NonNull View itemView) {
        super(itemView);
        titleTv = itemView.findViewById(R.id.grid_item_title_tv);
        iconIv = itemView.findViewById(R.id.grid_item_icon_iv);
    }

    public void bind(Context context, T item) {
        if (titleTv != null)
            titleTv.setText(item.getItemTitle());
        if (iconIv != null) {
            ResourceUtils.bindImageDrawable(context, ResourceUtils.getDrawable(item.getItemIconResName()))
                    .centerInside().into(iconIv);
        }
    }
}
