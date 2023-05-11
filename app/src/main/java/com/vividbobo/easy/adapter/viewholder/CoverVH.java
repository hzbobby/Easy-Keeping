package com.vividbobo.easy.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.Resource;
import com.vividbobo.easy.utils.GlideUtils;
import com.vividbobo.easy.utils.ResourceUtils;

public class CoverVH extends RecyclerView.ViewHolder {
    private ImageView coverIV;

    public CoverVH(@NonNull View itemView) {
        super(itemView);
        coverIV = itemView.findViewById(R.id.item_cover_iv);
    }

    public void bind(Context context, Resource resource) {
        GlideUtils.bindLegerCover(context,resource.getResName(),resource.getResType()).centerCrop().into(coverIV);
//        if (resource.getResType().equals(Resource.ResourceType.USER_COVER)) {
//            Glide.with(context).load(resource.getResName()).centerCrop().into(coverIV);
//        } else {
//            ResourceUtils.bindImageDrawable(context, ResourceUtils.getDrawable(resource.getResName()))
//                    .centerCrop().into(coverIV);
//        }
    }
}
