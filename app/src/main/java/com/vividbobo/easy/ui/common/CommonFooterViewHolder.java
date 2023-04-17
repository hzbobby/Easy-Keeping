package com.vividbobo.easy.ui.common;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;

public class CommonFooterViewHolder extends RecyclerView.ViewHolder {
    private TextView title;

    public CommonFooterViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.footer_common_title);
    }

    public void setTitle(@StringRes int resId) {
        title.setText(resId);
    }
}
