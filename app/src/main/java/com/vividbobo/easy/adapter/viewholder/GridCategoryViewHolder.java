package com.vividbobo.easy.adapter.viewholder;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.utils.ResourceUtils;

public class GridCategoryViewHolder extends GridCategoryPresentViewHolder {

    public GridCategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        moreIv.setVisibility(View.GONE);
    }

    public void bind(Category category) {
        iconIv.setImageDrawable(ResourceUtils.getDrawable(category.getIconResName()));
        titleTv.setText(category.getTitle());
    }

    public void bind(Context context, Category category) {
        ResourceUtils.bindImageDrawable(context,
                        ResourceUtils.getTintedDrawable(category.getIconResName(), ResourceUtils.getColor(R.color.black)))
                .centerInside().into(iconIv);
        titleTv.setText(category.getTitle());
    }
}
