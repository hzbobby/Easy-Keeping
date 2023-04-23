package com.vividbobo.easy.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.DayBillInfo;
import com.vividbobo.easy.utils.FormatUtils;

public class HomeBillHeaderVH extends RecyclerView.ViewHolder {
    private TextView dateText, weekText, incomeText, outcomeText;

    public HomeBillHeaderVH(View view) {
        super(view);
        dateText = view.findViewById(R.id.bill_header_date_text);
        weekText = view.findViewById(R.id.bill_header_week_text);
        incomeText = view.findViewById(R.id.bill_header_income_text);
        outcomeText = view.findViewById(R.id.bill_header_outcome_text);
    }

    public void bind(DayBillInfo dayBillInfo) {
        dateText.setText(dayBillInfo.getDate());
        weekText.setText(dayBillInfo.getWeek());
        incomeText.setText(FormatUtils.getAmount(dayBillInfo.getIncomeAmount()));
        outcomeText.setText(FormatUtils.getAmount(dayBillInfo.getExpenditureAmount()));
    }
}
