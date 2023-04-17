package com.vividbobo.easy.ui.others.expandableRv;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.utils.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

public class ExpandableChildRvAdapter extends RecyclerView.Adapter<ExpandableChildRvAdapter.ChildViewHolder>  {

    protected final static Integer ITEM_NORMAL = 1;
    protected final static Integer ITEM_FOOTER = 2;
    protected List<Category> items;

    private View.OnClickListener onAddClickListener;

    public void setOnAddClickListener(View.OnClickListener onAddClickListener) {
        this.onAddClickListener = onAddClickListener;
    }

    public ExpandableChildRvAdapter() {
        items = new ArrayList<>();
    }

    public ExpandableChildRvAdapter(List<Category> items) {
        this.items = items;
    }

    public void setItems(List<Category> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_gridchild_vh, parent, false);
        return new ChildViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_FOOTER) {
            holder.iconIv.setImageDrawable(ResourceUtils.getDrawable(R.drawable.ic_add));
            holder.titleTv.setText(R.string.add);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onAddClickListener != null) {
                        onAddClickListener.onClick(v);
                    }
                }
            });

        } else {
            Category category = items.get(position);
            holder.bind(category);
        }
    }

    @Override
    public int getItemCount() {
        return items.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) return ITEM_FOOTER;
        return ITEM_NORMAL;
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {
        ImageView iconIv;
        TextView titleTv;

        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            iconIv = itemView.findViewById(R.id.grid_child_icon_iv);
            titleTv = itemView.findViewById(R.id.grid_child_title_tv);
        }

        public void bind(Category category) {
            iconIv.setImageDrawable(ResourceUtils.getDrawable(category.getIconResName()));
            titleTv.setText(category.getTitle());
        }
    }
}
