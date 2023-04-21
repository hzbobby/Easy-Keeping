package com.vividbobo.easy.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.database.model.Account;

public class AccountAdapter extends ExpandableAdapter<Account> {
    private boolean enableAmount;

    public void setEnableAmount(boolean enableAmount) {
        this.enableAmount = enableAmount;
    }

    public AccountAdapter(Context mContext, RecyclerView.LayoutManager childLayoutManager, int childLayoutRes) {
        super(mContext, childLayoutManager, childLayoutRes);
    }

}
