package com.vividbobo.easy.adapter.viewholder;

import android.view.View;

import androidx.annotation.NonNull;

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
}
