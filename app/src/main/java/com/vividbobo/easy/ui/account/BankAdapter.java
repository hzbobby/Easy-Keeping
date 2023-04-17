package com.vividbobo.easy.ui.account;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.BankItem;
import com.vividbobo.easy.ui.others.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.BankViewHolder> {
    private List<BankItem> data;
    private OnItemClickListener onItemClickListener;

    public BankAdapter() {
        data = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            String title = String.format("标题#%d", i);
            data.add(new BankItem(title));
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setData(List<BankItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BankAdapter.BankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bank, parent, false);

        return new BankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BankAdapter.BankViewHolder holder, int position) {
        BankItem item = data.get(position);

//        holder.icon

        holder.title.setText(item.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view,item, holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class BankViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView title;

        public BankViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.item_icon);
            title = itemView.findViewById(R.id.item_title);
            itemView.findViewById(R.id.item_desc).setVisibility(View.GONE);

        }
    }
}
