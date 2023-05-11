package com.vividbobo.easy.adapter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.Itemzable;
import com.vividbobo.easy.adapter.viewholder.CheckableItemVH;
import com.vividbobo.easy.adapter.viewholder.FooterAddVH;
import com.vividbobo.easy.adapter.viewholder.ItemVH;
import com.vividbobo.easy.database.model.CheckableItem;
import com.vividbobo.easy.ui.others.commonAdapter.CommonAdapter;


/**
 * @param <T> the type of items
 */
public class CheckableAdapter<T extends Itemzable> extends CommonAdapter<CheckableItem<T>, RecyclerView.ViewHolder, CheckableItemVH<T>, FooterAddVH> {
    private boolean enableIcon;

    public CheckableAdapter(Context mContext) {
        super(mContext);
    }


    public void setEnableIcon(boolean enableIcon) {
        this.enableIcon = enableIcon;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected FooterAddVH onCreateFooterViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item1, parent, false);
        return new FooterAddVH(v);
    }

    @Override
    protected CheckableItemVH<T> onCreateNormalViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2_checkable, parent, false);
        return new CheckableItemVH<T>(v);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull CheckableItemVH<T> holder, int position) {
        CheckableItem<T> entity = getItemByHolderPosition(position);
        holder.setEnableIcon(enableIcon);
        holder.bind(mContext, entity);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, entity, holder.getAdapterPosition());
                }
                holder.checkBox.toggle();
                entity.setChecked(holder.checkBox.isChecked());
            }
        });
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                entity.setChecked(isChecked);
            }
        });

    }

    @Override
    protected void onBindHeaderViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    protected void onBindFooterViewHolder(@NonNull FooterAddVH holder, int position) {
        if (getFooterItem() != null) {
            holder.bind(mContext, getFooterItem().toString());
        }
    }

    public void setAllChecked(boolean b) {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setChecked(b);
        }
        notifyDataSetChanged();
    }
}