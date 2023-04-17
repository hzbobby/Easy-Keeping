package com.vividbobo.easy.ui.settings;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.SettingItem;
import com.vividbobo.easy.ui.others.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class SettingsAdapter extends RecyclerView.Adapter {
    private static final String TAG = "SettingsAdapter";
    private static final int HEADER_ITEM = 0;
    private static final int NORMAL_ITEM = 1;

    private List<SettingItem> data;
    private String headerTitle = "默认标题";

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public SettingsAdapter() {
        data = new ArrayList<>();
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public void setData(List<SettingItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    private boolean openHeader;

    public void setOpenHeader(boolean openHeader) {
        this.openHeader = openHeader;
    }

    @Override
    public int getItemViewType(int position) {
        if (openHeader && position == 0) return HEADER_ITEM;
        return NORMAL_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == HEADER_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_setting_item, parent, false);
            return new HeaderViewHolder(view);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting, parent, false);
        return new ItemViewHolder(view);
    }

    private int getDataPosition(int position) {
        return openHeader ? position - 1 : position;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (getItemViewType(position) == HEADER_ITEM) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.title.setText(headerTitle);
        } else {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            SettingItem settingItem = data.get( getDataPosition(holder.getAdapterPosition()) );
            itemViewHolder.title.setText(settingItem.getTitle());
            itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        //这里的postion是指data中的position
                        onItemClickListener.onItemClick(view,settingItem, getDataPosition(position));
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: itemCount: " + (openHeader ? 1 + data.size() : data.size()));
        return openHeader ? 1 + data.size() : data.size();
    }


    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.header_setting_title);
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_setting_title);
        }
    }
}
