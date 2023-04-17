package com.vividbobo.easy.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.DayBillPresent;

public class HomeBillHeaderVH extends RecyclerView.ViewHolder {
    private TextView dateText, weekText, incomeText, outcomeText;

    public HomeBillHeaderVH(View view) {
        super(view);
        dateText = view.findViewById(R.id.bill_header_date_text);
        weekText = view.findViewById(R.id.bill_header_week_text);
        incomeText = view.findViewById(R.id.bill_header_income_text);
        outcomeText = view.findViewById(R.id.bill_header_outcome_text);
    }

    public void bind(DayBillPresent dayBillPresent) {
        dateText.setText(dayBillPresent.getDate());
        weekText.setText(dayBillPresent.getWeek());
        incomeText.setText(String.format("%.2f", (double) dayBillPresent.getIncomeAmount() / 2));
        outcomeText.setText(String.format("%.2f", (double) dayBillPresent.getExpenditureAmount() / 2));
    }
}
