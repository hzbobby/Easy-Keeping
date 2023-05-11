package com.vividbobo.easy.ui.bill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.viewholder.GridCategoryViewHolder;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.ui.others.commonAdapter.CommonAdapter;

public class MoreCategoryAdapter extends CommonAdapter<Category, GridCategoryViewHolder, GridCategoryViewHolder, RecyclerView.ViewHolder> {


    public MoreCategoryAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected GridCategoryViewHolder onCreateHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        return onCreateNormalViewHolder(parent, viewType);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateFooterViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected GridCategoryViewHolder onCreateNormalViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_category, parent, false);
        return new GridCategoryViewHolder(view);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull GridCategoryViewHolder holder, int position) {
        Category item = getItemByHolderPosition(position);
        holder.bind(mContext, item);
    }

    @Override
    protected void onBindHeaderViewHolder(@NonNull GridCategoryViewHolder holder, int position) {
        holder.bind((Category) headerItem);
    }

    @Override
    protected void onBindFooterViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }
}
