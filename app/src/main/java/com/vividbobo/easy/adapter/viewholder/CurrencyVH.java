package com.vividbobo.easy.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.Currency;
import com.vividbobo.easy.utils.ResourceUtils;

public class CurrencyVH extends RecyclerView.ViewHolder {
    public TextView titleTv, codeTv, rateTv, updateTv;
    public MaterialSwitch enableSwitch;

    public CurrencyVH(@NonNull View itemView) {
        super(itemView);
        titleTv = itemView.findViewById(R.id.item_currency_cn_tv);
        codeTv = itemView.findViewById(R.id.item_currency_code_tv);
        rateTv = itemView.findViewById(R.id.item_currency_rate_tv);
        updateTv = itemView.findViewById(R.id.item_currency_update_tv);
        enableSwitch = itemView.findViewById(R.id.item_currency_switch);
    }

    public void bind(Currency currency) {
        titleTv.setText(currency.getTitle());
        codeTv.setText(currency.getCode());
        if (currency.isAutoUpdate()) {
            rateTv.setText(String.format("%.4f", currency.getRate()));
        } else {
            rateTv.setText(String.format("%.4f", currency.getLocalRate()));
        }

        enableSwitch.setChecked(currency.isEnable());
        if (!currency.isAutoUpdate()) {
            updateTv.setVisibility(View.VISIBLE);
        } else {
            updateTv.setVisibility(View.GONE);
        }

    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        enableSwitch.setOnCheckedChangeListener(listener);
    }


}
