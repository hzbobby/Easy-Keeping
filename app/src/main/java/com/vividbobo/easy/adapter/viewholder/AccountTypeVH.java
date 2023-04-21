package com.vividbobo.easy.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.AccountType;
import com.vividbobo.easy.utils.ResourceUtils;

public class AccountTypeVH extends RecyclerView.ViewHolder {
    ImageView icon;
    TextView title, desc;

    public AccountTypeVH(@NonNull View itemView) {
        super(itemView);

        icon = itemView.findViewById(R.id.item_icon_iv);
        title = itemView.findViewById(R.id.item_title_tv);
        desc = itemView.findViewById(R.id.item_desc_tv);
    }

    public void bind(Context context, AccountType accountType) {
        ResourceUtils.bindImageDrawable(context, ResourceUtils.getDrawable(accountType.getIconResName())).fitCenter().into(icon);
        title.setText(accountType.getTitle());
        desc.setText(accountType.getDesc());
    }
}