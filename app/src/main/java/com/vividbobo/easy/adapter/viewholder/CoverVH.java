package com.vividbobo.easy.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.Resource;
import com.vividbobo.easy.utils.ResourceUtils;

public class CoverVH extends RecyclerView.ViewHolder {
    private ImageView coverIV;

    public CoverVH(@NonNull View itemView) {
        super(itemView);
        coverIV = itemView.findViewById(R.id.item_cover_iv);
    }

    public void bind(Context context, Resource resource) {
        ResourceUtils.bindImageDrawable(context, ResourceUtils.getDrawable(resource.getResName()))
                .centerCrop().into(coverIV);
    }
}
