package com.vividbobo.easy.ui.common;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;

public abstract class BaseDialogPickerAdapter<T, VH extends RecyclerView.ViewHolder, H extends RecyclerView.ViewHolder, F extends RecyclerView.ViewHolder>
        extends BaseRecyclerViewAdapter<T, VH, H, F> {

    @Override
    protected Integer getNormalLayoutRes() {
        return R.layout.item_common;
    }

    @Override
    protected Integer getFooterLayoutRes() {
        return R.layout.footer_common_icon_title;
    }

    @Override
    protected Integer getHeaderLayoutRes() {
        return null;
    }

    @Override
    protected H getHeaderViewHolder(View view) {
        return null;
    }

    @Override
    protected void onBindHeaderViewHolder(@NonNull H holder, int position) {

    }


}