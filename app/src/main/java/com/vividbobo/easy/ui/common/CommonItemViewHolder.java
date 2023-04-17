package com.vividbobo.easy.ui.common;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;

public abstract class CommonItemViewHolder<T> extends RecyclerView.ViewHolder {
    protected ImageView icon;
    protected TextView title, desc;

    public CommonItemViewHolder(@NonNull View itemView) {
        super(itemView);
        icon = itemView.findViewById(R.id.item_icon);
        title = itemView.findViewById(R.id.item_title);
        desc = itemView.findViewById(R.id.item_desc);
    }

    public abstract void bind(T item);

}