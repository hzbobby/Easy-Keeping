package com.vividbobo.easy.adapter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.viewholder.AccountTypeVH;
import com.vividbobo.easy.database.model.AccountType;
import com.vividbobo.easy.ui.others.commonAdapter.CommonAdapter;

public class AccountTypeAdapter extends CommonAdapter<AccountType, RecyclerView.ViewHolder, AccountTypeVH, RecyclerView.ViewHolder> {
    private int lastClickPos = -1;

    public AccountTypeAdapter(Context mContext) {
        super(mContext);
    }

//    public AccountType getLastClickItem() {
//        if (lastClickPos != -1) {
//            return data.get(lastClickPos);
//        }
//        return null;
//    }


    @Override
    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateFooterViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected AccountTypeVH onCreateNormalViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2, parent, false);
        return new AccountTypeVH(view);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull AccountTypeVH holder, int position) {
        AccountType accountType = getItemByHolderPosition(position);
        holder.bind(mContext, accountType);
    }

    @Override
    protected void onBindHeaderViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    protected void onBindFooterViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }


}
