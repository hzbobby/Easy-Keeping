package com.vividbobo.easy.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.Itemzable;
import com.vividbobo.easy.database.model.CheckableItem;
import com.vividbobo.easy.utils.ResourceUtils;

public class CheckableItemVH<T extends Itemzable> extends RecyclerView.ViewHolder implements Checkable {
    public ImageView iconIv;
    public TextView titleTv, descTv;
    public MaterialCheckBox checkBox;
    private boolean enableIcon;

    public CheckableItemVH(@NonNull View itemView) {
        super(itemView);
        iconIv = itemView.findViewById(R.id.item_icon_iv);
        titleTv = itemView.findViewById(R.id.item_title_tv);
        descTv = itemView.findViewById(R.id.item_desc_tv);
        checkBox = itemView.findViewById(R.id.item_check_box);

    }


    public void bind(Context context, CheckableItem<T> checkableItem) {
        T entity = checkableItem.getData();
        checkBox.setChecked(checkableItem.isChecked());
        if (enableIcon) {
            iconIv.setVisibility(View.VISIBLE);
            if (entity.getItemIconResName() == null || entity.getItemIconResName().isEmpty()) {
                ResourceUtils.bindImageDrawable(context, ResourceUtils.getTextImageIcon(entity.getItemTitle(), ResourceUtils.getColor(R.color.black))).centerCrop().into(iconIv);
            } else {
                if (entity.getItemIconResName().contains("category")) {
                    ResourceUtils.bindImageDrawable(context, ResourceUtils.getTintedDrawable(entity.getItemIconResName(), ResourceUtils.getColor(R.color.black)
                    )).centerCrop().into(iconIv);
                } else {
                    ResourceUtils.bindImageDrawable(context, ResourceUtils.getDrawable(entity.getItemIconResName())).centerInside().into(iconIv);
                }
            }
        } else {
            iconIv.setVisibility(View.GONE);
        }

        titleTv.setText(entity.getItemTitle());
        if (entity.getItemDesc() == null || entity.getItemDesc().isEmpty()) {
            descTv.setVisibility(View.GONE);
        } else {
            descTv.setVisibility(View.VISIBLE);
            descTv.setText(entity.getItemDesc());
        }
    }

    public void setEnableIcon(boolean enableIcon) {
        this.enableIcon = enableIcon;
    }

    @Override
    public void setChecked(boolean checked) {
        if (checkBox == null) return;
        checkBox.setChecked(checked);
    }

    @Override
    public boolean isChecked() {
        return checkBox != null && checkBox.isChecked();
    }

    @Override
    public void toggle() {

    }
}