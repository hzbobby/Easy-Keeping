package com.vividbobo.easy.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.BillInfo;
import com.vividbobo.easy.utils.FormatUtils;

public class HomeBillHeaderVH extends RecyclerView.ViewHolder {
    private TextView dateText, weekText, incomeText, outcomeText;

    public HomeBillHeaderVH(View view) {
        super(view);
        dateText = view.findViewById(R.id.header_date_tv);
        weekText = view.findViewById(R.id.header_week_tv);
        incomeText = view.findViewById(R.id.header_label1_content_tv);
        outcomeText = view.findViewById(R.id.header_label2_content_tv);
    }

    public void bind(BillInfo billInfo) {
        dateText.setText(billInfo.getDate());
        weekText.setText(billInfo.getWeek());
        incomeText.setText(FormatUtils.getAmount(billInfo.getIncomeAmount()));
        outcomeText.setText(FormatUtils.getAmount(billInfo.getExpenditureAmount()));
    }
}
