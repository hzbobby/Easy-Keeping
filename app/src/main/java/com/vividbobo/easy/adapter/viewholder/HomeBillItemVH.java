package com.vividbobo.easy.adapter.viewholder;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.BillPresent;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.utils.CalendarUtils;
import com.vividbobo.easy.utils.FormatUtils;
import com.vividbobo.easy.utils.ResourceUtils;

public class HomeBillItemVH extends RecyclerView.ViewHolder {
    private ImageView iconIv;
    private TextView categoryTv, dateTv, otherTv, labelTv, amountTv, accountTv, roleTv, storeTv, refundTv, reimburseTv, sta1Tv, sta2Tv, imageTv;

    private LinearLayout line2, line3;

    public HomeBillItemVH(@NonNull View itemView) {
        super(itemView);
        iconIv = itemView.findViewById(R.id.bill_category_icon);
        categoryTv = itemView.findViewById(R.id.bill_category_text);
        dateTv = itemView.findViewById(R.id.bill_time_text);
        otherTv = itemView.findViewById(R.id.bill_other_text);
        labelTv = itemView.findViewById(R.id.bill_label_text);
        amountTv = itemView.findViewById(R.id.bill_money_text);
        accountTv = itemView.findViewById(R.id.bill_account_text);

        roleTv = itemView.findViewById(R.id.bill_role_text);
        storeTv = itemView.findViewById(R.id.bill_store_text);
        refundTv = itemView.findViewById(R.id.bill_refund_text);
        reimburseTv = itemView.findViewById(R.id.bill_reimburse_text);
        sta1Tv = itemView.findViewById(R.id.bill_sta1_text);
        sta2Tv = itemView.findViewById(R.id.bill_sta2_text);
        imageTv = itemView.findViewById(R.id.bill_images_text);
        line2 = itemView.findViewById(R.id.line2);
        line3 = itemView.findViewById(R.id.line3);
    }

    public void bind(Bill billPresent) {
        try {
            if (billPresent.getBillType() == Category.TYPE_EXPENDITURE) {
                ResourceUtils.bindImageDrawable(itemView.getContext(), ResourceUtils.getTintedDrawable(billPresent.getCategoryIconResName(), ResourceUtils.getColor(R.color.expenditure))).into(iconIv);
                amountTv.setTextColor(ResourceUtils.getColor(R.color.expenditure));
            } else {
                ResourceUtils.bindImageDrawable(itemView.getContext(), ResourceUtils.getTintedDrawable(billPresent.getCategoryIconResName(), ResourceUtils.getColor(R.color.income))).into(iconIv);
                amountTv.setTextColor(ResourceUtils.getColor(R.color.income));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        categoryTv.setText(billPresent.getCategoryTitle());
        dateTv.setText(CalendarUtils.getDateMMdd(billPresent.getDate()));
        amountTv.setText(FormatUtils.getAmount(billPresent.getAmount()));
        accountTv.setText(billPresent.getAccountTitle());

        boolean visibleLine2 = false, visibleLine3 = false;

        if (billPresent.getRemark() == null || billPresent.getRemark().isEmpty()) {
            otherTv.setVisibility(View.GONE);
        } else {
            otherTv.setVisibility(View.VISIBLE);
            otherTv.setText(billPresent.getRemark());
            visibleLine2 = true;
        }
        if (billPresent.getRoleTitle() == null || billPresent.getRoleTitle().isEmpty()) {
            roleTv.setVisibility(View.GONE);
        } else {
            roleTv.setVisibility(View.VISIBLE);
            roleTv.setText(billPresent.getRoleTitle());
        }
        if (billPresent.getStoreTitle() == null || billPresent.getStoreTitle().isEmpty()) {
            storeTv.setVisibility(View.GONE);
        } else {
            storeTv.setVisibility(View.VISIBLE);
            storeTv.setText(billPresent.getStoreTitle());
        }
        if (billPresent.getRefund() == null || !billPresent.getRefund()) {
            refundTv.setVisibility(View.GONE);
        } else {
            refundTv.setVisibility(View.VISIBLE);
            refundTv.setText("退款");
            visibleLine2 = true;
        }
        if (billPresent.getReimburse() == null || !billPresent.getReimburse()) {
            reimburseTv.setVisibility(View.GONE);
        } else {
            reimburseTv.setVisibility(View.VISIBLE);
            reimburseTv.setText("报销");
            visibleLine2 = true;
        }
        if (billPresent.getIncomeExpenditureIncluded() == null || !billPresent.getIncomeExpenditureIncluded()) {
            sta1Tv.setVisibility(View.GONE);
        } else {
            sta1Tv.setVisibility(View.VISIBLE);
            sta1Tv.setText(FormatUtils.getDeleteString("收支"));
            visibleLine3 = true;
        }
        if (billPresent.getBudgetIncluded() == null || !billPresent.getBudgetIncluded()) {
            sta2Tv.setVisibility(View.GONE);
        } else {
            sta2Tv.setVisibility(View.VISIBLE);
            sta2Tv.setText(FormatUtils.getDeleteString("预算"));
            visibleLine3 = true;
        }
        if (billPresent.getImagePaths() == null || billPresent.getImagePaths().isEmpty()) {
            imageTv.setVisibility(View.GONE);
        } else {
            imageTv.setVisibility(View.VISIBLE);
            imageTv.setText("图片");
        }


        if (billPresent.getTags() == null || billPresent.getTags().isEmpty()) {
            labelTv.setVisibility(View.GONE);
        } else {
            labelTv.setVisibility(View.VISIBLE);
            labelTv.setText(FormatUtils.getTagSpannableStringBuilder(billPresent.getTags()));
            visibleLine3 = true;
        }

        if (!visibleLine2) {
            line2.setVisibility(View.GONE);
        } else {
            line2.setVisibility(View.VISIBLE);
        }
        if (!visibleLine3) {
            line3.setVisibility(View.GONE);
        } else {
            line3.setVisibility(View.VISIBLE);
        }
    }
}
