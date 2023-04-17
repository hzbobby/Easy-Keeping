package com.vividbobo.easy.ui.account;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.AccountTypeItem;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.ui.others.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class AccountTypeAdapter extends RecyclerView.Adapter<AccountTypeAdapter.AccountTypeViewHolder> {
    private List<AccountTypeItem> data;
    private int lastClickPos = -1;

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public AccountTypeItem getLastClickItem() {
        if (lastClickPos != -1) {
            return data.get(lastClickPos);
        }
        return null;
    }

    public AccountTypeAdapter() {
        data = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            AccountTypeItem accountTypeItem = new AccountTypeItem(
                    String.format("标题 #%d", i),
                    String.format("副标题 #%d", i)
            );
            if (i == 5) {
                accountTypeItem.setTitle("银行");
                accountTypeItem.setChildAccountType(AccountTypeItem.ACCOUNT_BANK);
            }
            data.add(accountTypeItem);
        }
    }

    public void setData(List<AccountTypeItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AccountTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account_type, parent, false);
        return new AccountTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountTypeViewHolder holder, int position) {
        AccountTypeItem item = data.get(position);
        holder.title.setText(item.getTitle());
        holder.subTitle.setText(item.getSubTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view,item, holder.getAdapterPosition());
                    lastClickPos=holder.getAdapterPosition();
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(item, holder.getAdapterPosition());
                    lastClickPos=holder.getAdapterPosition();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class AccountTypeViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title, subTitle;

        public AccountTypeViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.item_icon);
            title = itemView.findViewById(R.id.item_title);
            subTitle = itemView.findViewById(R.id.item_desc);
        }
    }
}
