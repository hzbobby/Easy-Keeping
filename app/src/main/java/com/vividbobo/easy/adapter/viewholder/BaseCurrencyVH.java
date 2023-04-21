package com.vividbobo.easy.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.Currency;

public class BaseCurrencyVH extends RecyclerView.ViewHolder {
    private TextView titleTv, contentTv;

    public BaseCurrencyVH(@NonNull View itemView) {
        super(itemView);
        titleTv = itemView.findViewById(R.id.item_title);
        contentTv = itemView.findViewById(R.id.item_content);
    }

    public void bind(Currency currency) {
        titleTv.setText("本位货币");
        contentTv.setText(String.format("%s - %s", currency.getTitle(), currency.getCode()));
    }
}
