package com.vividbobo.easy.ui.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.ui.others.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class HomeBillAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "HomeBillAdapter";

    //normal item data
    private List<Object> data = new ArrayList<>();

    public HomeBillAdapter() {
        for (int i = 0; i < 15; i++) {
            data.add(1);
        }
    }

    //click listener
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //item type
    static final int ITEM_HEADER = 0;
    static final int ITEM_NORMAL = 1;
    static final int ITEM_FOOTER = 2;
    private boolean openHeader = false;
    private boolean openFooter = false;

    private static final int DEFAULT_MAX_TO_SHOW = 10;
    private int max2show = DEFAULT_MAX_TO_SHOW;

    private boolean dataToShowLimit = false;

    public void setData(List<Object> data) {
        this.data = data;
    }

    public void setOpenFooter(boolean openFooter) {
        this.openFooter = openFooter;
    }

    public void setOpenHeader(boolean openHeader) {
        this.openHeader = openHeader;
    }

    public void setDataToShowLimit(boolean dataToShowLimit) {
        this.dataToShowLimit = dataToShowLimit;
    }

    public void setMax2show(int max2show) {
        this.max2show = max2show;
    }

    @Override
    public int getItemViewType(int position) {
        //如果启用header且该位置是0，则类型为HEADER
        //如果启用footer,且该位置是最后一个,则为FOOTER
        //其余为NORMAL
        if (openHeader && position == 0) return ITEM_HEADER;
        else if (openFooter && position == getItemCount() - 1)
            return ITEM_FOOTER;
        else return ITEM_NORMAL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);
        if (viewType == ITEM_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_item_bill, parent, false);
            return new HomeBillAdapter.HeaderBillViewHolder(view);
        } else if (viewType == ITEM_FOOTER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_item_bill, parent, false);
            return new HomeBillAdapter.FooterBillViewHolder(view);
        }
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + String.format("position: %d ; total cnt: %d", position, getItemCount()));
        if (onItemClickListener != null) {
            onItemClickListener.OnItemClick(null, position);
        }
    }

    @Override
    public int getItemCount() {
        //如果有显示限制，则只显示最大个数，若没有限制，即显示全部条目;
        //若有header,总数+1,否则+0
        //若有footer,总数+1,否则+0
        int normalCnt = dataToShowLimit && max2show < data.size()?max2show:data.size();

        return normalCnt + (openHeader ? 1 : 0) + (openFooter ? 1 : 0);
    }

    public class BillViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryIcon;
        TextView categoryText, timeText, otherText, labelText, moneyText, accountText;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryIcon = itemView.findViewById(R.id.bill_category_icon);
            categoryText = itemView.findViewById(R.id.bill_category_text);
            timeText = itemView.findViewById(R.id.bill_time_text);
            otherText = itemView.findViewById(R.id.bill_other_text);
            labelText = itemView.findViewById(R.id.bill_label_text);
            moneyText = itemView.findViewById(R.id.bill_money_text);
            accountText = itemView.findViewById(R.id.bill_account_text);
        }
    }

    public class HeaderBillViewHolder extends RecyclerView.ViewHolder {
        TextView weekText, incomeText, outcomeText;

        public HeaderBillViewHolder(View view) {
            super(view);

            weekText = view.findViewById(R.id.bill_header_week_text);
            incomeText = view.findViewById(R.id.bill_header_income_text);
            outcomeText = view.findViewById(R.id.bill_header_outcome_text);
        }

    }

    public class FooterBillViewHolder extends RecyclerView.ViewHolder {
        public FooterBillViewHolder(View view) {
            super(view);
        }
    }
}
