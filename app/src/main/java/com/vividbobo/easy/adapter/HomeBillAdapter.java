package com.vividbobo.easy.adapter;

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
import com.vividbobo.easy.database.model.BillPresent;
import com.vividbobo.easy.database.model.DayBillPresent;
import com.vividbobo.easy.ui.others.commonAdapter.CommonAdapter;

public class HomeBillAdapter extends CommonAdapter<BillPresent, HomeBillHeaderVH, RecyclerView.ViewHolder, HomeBillFooterVH> {
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
        Log.d("TAG", "getItemViewType: " + String.format("position:%d viewType:%d", position, viewType));
        if (viewType == CommonAdapter.ITEM_TYPE_NORMAL) {
            BillPresent billPresent = getItemByHolderPosition(position);
            if (billPresent.getBillType() == BillPresent.TYPE_TRANSFER) {
                //需要用新的VH
                return ITEM_TYPE_TRANSFER;
            }
        }
        return viewType;
    }

    @Override
    protected HomeBillHeaderVH onCreateHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_item_bill, parent, false);
        return new HomeBillHeaderVH(v);
    }

    @Override
    protected HomeBillFooterVH onCreateFooterViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_item_bill, parent, false);
        return new HomeBillFooterVH(v);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateNormalViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_TRANSFER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill_transfer, parent, false);
            return new HomeBillTransferVH(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);
            return new HomeBillItemVH(v);
        }
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BillPresent billPresent = getItemByHolderPosition(position);
        if (getItemViewType(position) == ITEM_TYPE_TRANSFER) {
            HomeBillTransferVH vh = (HomeBillTransferVH) holder;
            vh.bind(billPresent);
        } else {
            HomeBillItemVH vh = (HomeBillItemVH) holder;
            vh.bind(billPresent);
        }
    }

    @Override
    protected void onBindHeaderViewHolder(@NonNull HomeBillHeaderVH holder, int position) {
        if (getHeaderItem() != null)
            holder.bind((DayBillPresent) getHeaderItem());
    }

    @Override
    protected void onBindFooterViewHolder(@NonNull HomeBillFooterVH holder, int position) {

    }
}
