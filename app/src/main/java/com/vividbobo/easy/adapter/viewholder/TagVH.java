package com.vividbobo.easy.adapter.viewholder;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.Tag;
import com.vividbobo.easy.utils.ResourceUtils;

public class TagVH extends RecyclerView.ViewHolder {
    private ImageView iconIv;
    private TextView titleTv;

    public TagVH(@NonNull View itemView) {
        super(itemView);
        iconIv = itemView.findViewById(R.id.item_icon_iv);
        titleTv = itemView.findViewById(R.id.item_title_tv);
    }

    public void bind(Context mContext, Tag tag) {
        ResourceUtils.bindImageDrawable(mContext, ResourceUtils.getTintedDrawable(R.drawable.ic_tag, tag.getHexCode())).fitCenter().into(iconIv);
        Log.d("TAG", "bind: " + tag.getTitle());
        titleTv.setText(tag.getTitle());
    }
}
