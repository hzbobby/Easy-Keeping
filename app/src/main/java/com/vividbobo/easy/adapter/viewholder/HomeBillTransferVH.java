package com.vividbobo.easy.adapter.viewholder;

import android.content.Context;
import android.text.style.DrawableMarginSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.BillPresent;
import com.vividbobo.easy.utils.CalendarUtils;
import com.vividbobo.easy.utils.FormatUtils;
import com.vividbobo.easy.utils.ResourceUtils;

import java.io.ObjectStreamClass;
import java.util.Objects;

public class HomeBillTransferVH extends RecyclerView.ViewHolder {
    private ImageView srcIconIv, tarIconIv;
    private TextView srcAccountTv, tarAccountTv, dateTv, roleTv, amountTv, imageTv, remarkTv, tagTv;
    private LinearLayout labelLine1Layout, labelLine2Layout;

    public HomeBillTransferVH(@NonNull View itemView) {
        super(itemView);
        srcIconIv = itemView.findViewById(R.id.src_account_icon_iv);
        tarIconIv = itemView.findViewById(R.id.tar_account_icon_iv);
        srcAccountTv = itemView.findViewById(R.id.src_label_tv);
        tarAccountTv = itemView.findViewById(R.id.tar_label_tv);
        dateTv = itemView.findViewById(R.id.date_label_tv);
        roleTv = itemView.findViewById(R.id.role_label_tv);
        amountTv = itemView.findViewById(R.id.amount_label_tv);
        imageTv = itemView.findViewById(R.id.image_label_tv);
        remarkTv = itemView.findViewById(R.id.remark_label_tv);
        tagTv = itemView.findViewById(R.id.tag_label_tv);
        labelLine1Layout = itemView.findViewById(R.id.label_line1);
        labelLine2Layout = itemView.findViewById(R.id.label_line2);
    }

    public void bind(Context context, Bill bill) {
        if (Objects.isNull(bill.getAccountIconResName()) || bill.getAccountIconResName().isEmpty()) {
            //图标绑定标题
            String title = bill.getAccountTitle();
            if (title == null || title.isEmpty()) {
                title = "无";
            }
            ResourceUtils.bindImageDrawable(context, ResourceUtils.getTextImageIcon(title,
                    ResourceUtils.getColor(R.color.black))).centerCrop().into(srcIconIv);

        } else {
            //绑定账户图标
            ResourceUtils.bindImageDrawable(context, ResourceUtils.getDrawable(bill.getAccountIconResName()))
                    .centerCrop().into(srcIconIv);
        }
        if (Objects.isNull(bill.getTarAccountIconResName()) || bill.getTarAccountIconResName().isEmpty()) {
            //图标绑定标题
            String title = bill.getTarAccountTitle();
            if (title == null || title.isEmpty()) {
                title = "无";
            }
            ResourceUtils.bindImageDrawable(context, ResourceUtils.getTextImageIcon(title,
                    ResourceUtils.getColor(R.color.black))).centerCrop().into(srcIconIv);

        } else {
            //绑定账户图标
            ResourceUtils.bindImageDrawable(context, ResourceUtils.getDrawable(bill.getTarAccountIconResName()))
                    .centerCrop().into(tarIconIv);
        }
        srcAccountTv.setText(bill.getAccountTitle());
        tarAccountTv.setText(bill.getTarAccountTitle());
        amountTv.setText(FormatUtils.getAmount(bill.getAmount()));
        boolean labelLine1 = false, labelLine2 = false;

        if (Objects.isNull(bill.getDate())) {
            dateTv.setVisibility(View.GONE);
        } else {
            dateTv.setVisibility(View.VISIBLE);
            dateTv.setText(CalendarUtils.getDateMMdd(bill.getDate()));
            labelLine1 = true;
        }
        if (Objects.isNull(bill.getRoleTitle()) || bill.getRoleTitle().isEmpty() || bill.getRoleTitle().equals("自己")) {
            roleTv.setVisibility(View.GONE);
        } else {
            roleTv.setVisibility(View.VISIBLE);
            roleTv.setText(bill.getRoleTitle());
            labelLine2 = true;
        }
        if (Objects.isNull(bill.getImagePaths()) || bill.getImagePaths().isEmpty()) {
            imageTv.setVisibility(View.GONE);
        } else {
            imageTv.setVisibility(View.VISIBLE);
            imageTv.setText("图片");
            labelLine2 = true;
        }

        if (Objects.isNull(bill.getRemark()) || bill.getRemark().isEmpty()) {
            remarkTv.setVisibility(View.GONE);
        } else {
            remarkTv.setVisibility(View.VISIBLE);
            remarkTv.setText(bill.getRemark());
            labelLine2 = true;
        }
        if (Objects.isNull(bill.getTags()) || bill.getTags().isEmpty()) {
            tagTv.setVisibility(View.GONE);
        } else {
            tagTv.setVisibility(View.VISIBLE);
            tagTv.setText(FormatUtils.getTagSpannableStringBuilder(bill.getTags()));
            labelLine2 = true;
        }

        if (labelLine1) {
            labelLine1Layout.setVisibility(View.VISIBLE);
        } else {
            labelLine1Layout.setVisibility(View.GONE);
        }
        if (labelLine2) {
            labelLine2Layout.setVisibility(View.VISIBLE);
        } else {
            labelLine2Layout.setVisibility(View.GONE);
        }
    }
}
