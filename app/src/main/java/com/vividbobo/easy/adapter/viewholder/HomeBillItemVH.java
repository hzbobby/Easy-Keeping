package com.vividbobo.easy.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.BillPresent;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.utils.CalendarUtils;
import com.vividbobo.easy.utils.FormatUtils;
import com.vividbobo.easy.utils.ResourceUtils;

public class HomeBillItemVH extends RecyclerView.ViewHolder {
    private ImageView iconIv;
    private TextView categoryTv, dateTv, otherTv, labelTv, amountTv, accountTv;

    public HomeBillItemVH(@NonNull View itemView) {
        super(itemView);
        iconIv = itemView.findViewById(R.id.bill_category_icon);
        categoryTv = itemView.findViewById(R.id.bill_category_text);
        dateTv = itemView.findViewById(R.id.bill_time_text);
        otherTv = itemView.findViewById(R.id.bill_other_text);
        labelTv = itemView.findViewById(R.id.bill_label_text);
        amountTv = itemView.findViewById(R.id.bill_money_text);
        accountTv = itemView.findViewById(R.id.bill_account_text);
    }

    public void bind(BillPresent billPresent) {
        if (billPresent.getBillType() == Category.TYPE_EXPENDITURE) {
            ResourceUtils.bindImageDrawable(itemView.getContext(), ResourceUtils.getTintedDrawable(billPresent.getIconResName(),
                    ResourceUtils.getColor(R.color.expenditure))).into(iconIv);
            amountTv.setTextColor(ResourceUtils.getColor(R.color.expenditure));
        } else {
            ResourceUtils.bindImageDrawable(itemView.getContext(), ResourceUtils.getTintedDrawable(billPresent.getIconResName(),
                    ResourceUtils.getColor(R.color.income))).into(iconIv);
            amountTv.setTextColor(ResourceUtils.getColor(R.color.income));
        }
        categoryTv.setText(billPresent.getTitle());
        otherTv.setText(billPresent.getDesc());
        dateTv.setText(CalendarUtils.getDateMMdd(billPresent.getDate()));
        amountTv.setText(FormatUtils.getAmount(billPresent.getAmount()));
        accountTv.setText(billPresent.getSrcAccount());
        labelTv.setText(FormatUtils.getTagSpannableStringBuilder(billPresent.getTags()));
    }
}
