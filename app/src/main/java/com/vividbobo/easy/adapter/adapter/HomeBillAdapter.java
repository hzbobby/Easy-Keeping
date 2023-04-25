package com.vividbobo.easy.adapter.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.viewholder.HomeBillFooterVH;
import com.vividbobo.easy.adapter.viewholder.HomeBillHeaderVH;
import com.vividbobo.easy.adapter.viewholder.HomeBillItemVH;
import com.vividbobo.easy.adapter.viewholder.HomeBillTransferVH;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.BillInfo;
import com.vividbobo.easy.database.model.BillPresent;
import com.vividbobo.easy.ui.others.commonAdapter.CommonAdapter;

public class HomeBillAdapter extends CommonAdapter<Bill, HomeBillHeaderVH, HomeBillItemVH, HomeBillFooterVH> {
    private static final int ITEM_TYPE_TRANSFER = 102;

    public HomeBillAdapter(Context mContext) {
        super(mContext);
        setEnableHeader(true);
        setEnableFooter(true);
        setEnableMaxCount(true);
        setMaxCount(10);
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = super.getItemViewType(position);
        Log.d("TAG", "getItemViewType: super viewType: " + viewType);
        if (viewType == CommonAdapter.ITEM_TYPE_NORMAL) {
            Bill bill = getItemByHolderPosition(position);
            if (bill.getBillType() == Bill.TRANSFER) {
                //需要用新的VH
                viewType = ITEM_TYPE_TRANSFER;
            }
        }
        Log.d("TAG", "getItemViewType: final viewType" + viewType);
        return viewType;
    }

    @Override
    protected HomeBillHeaderVH onCreateHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_item_bill2, parent, false);
        return new HomeBillHeaderVH(v);
    }

    @Override
    protected HomeBillFooterVH onCreateFooterViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_item_bill, parent, false);
        return new HomeBillFooterVH(v);
    }

    @Override
    protected HomeBillItemVH onCreateNormalViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill2, parent, false);
        return new HomeBillItemVH(v);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateOtherViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill_transfer2, parent, false);
        return new HomeBillTransferVH(v);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull HomeBillItemVH holder, int position) {
        Bill billPresent = getItemByHolderPosition(position);
        HomeBillItemVH vh = (HomeBillItemVH) holder;
        vh.bind(mContext, billPresent);
    }

    @Override
    protected void onBindOtherViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindOtherViewHolder(holder, position);
        Bill billPresent = getItemByHolderPosition(position);
        HomeBillTransferVH vh = (HomeBillTransferVH) holder;
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, billPresent, vh.getAdapterPosition());
                }
            }
        });
        vh.bind(mContext, billPresent);
    }

    @Override
    protected void onBindHeaderViewHolder(@NonNull HomeBillHeaderVH holder, int position) {
        if (getHeaderItem() != null)
            holder.bind((BillInfo) getHeaderItem());
    }

    @Override
    protected void onBindFooterViewHolder(@NonNull HomeBillFooterVH holder, int position) {

    }
}
