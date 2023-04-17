package com.vividbobo.easy.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;

public class GridCategoryPresentViewHolder extends RecyclerView.ViewHolder {
    protected ImageView iconIv, moreIv;
    protected TextView titleTv;

    public GridCategoryPresentViewHolder(@NonNull View itemView) {
        super(itemView);
        iconIv = itemView.findViewById(R.id.grid_item_icon_iv);
        titleTv = itemView.findViewById(R.id.grid_item_title_tv);
        moreIv = itemView.findViewById(R.id.grid_item_more_iv);
    }

}
