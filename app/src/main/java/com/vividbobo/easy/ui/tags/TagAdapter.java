package com.vividbobo.easy.ui.tags;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.model.TagItem;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.utils.ColorUtils;
import com.vividbobo.easy.utils.DrawableUtils;

import java.util.ArrayList;
import java.util.List;

public class TagAdapter extends RecyclerView.Adapter {
    private static final int NORMAL_ITEM = 1;
    private static final int FOOTER_ITEM = 2;

    List<TagItem> tagItems;
    private boolean footerEnable;

    private OnItemClickListener onItemClickListener;
    private View.OnClickListener onFooterClickListener;

    public TagItem deleteItem(int position) {
        TagItem old = tagItems.remove(position);
        notifyItemRemoved(position);
        return old;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnFooterClickListener(View.OnClickListener onFooterClickListener) {
        this.onFooterClickListener = onFooterClickListener;
    }

    public void setFooterEnable(boolean footerEnable) {
        this.footerEnable = footerEnable;
    }

    public boolean isFooterEnable() {
        return footerEnable;
    }

    public TagAdapter() {
        tagItems = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            tagItems.add(new TagItem(String.format("标签%d", i), ColorUtils.getRandomColor()));
        }
    }

    public void setTagItems(List<TagItem> tagItems) {
        this.tagItems = tagItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == NORMAL_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);
            return new TagViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_item_tag, parent, false);
            return new FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (getItemViewType(position) == NORMAL_ITEM) {
            TagViewHolder tagViewHolder = (TagViewHolder) holder;
            //setColor
            TagItem tagItem = tagItems.get(position);
            tagViewHolder.tagIcon.setImageDrawable(DrawableUtils.getDrawable(R.drawable.ic_tag, tagItem.getColor()));
            tagViewHolder.tagTitle.setText(tagItem.getText());

            tagViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.OnItemClick(tagItem, holder.getAdapterPosition());
                    }
                }
            });
        } else {
            //footer
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onFooterClickListener != null) {
                        onFooterClickListener.onClick(view);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return footerEnable ? 1 + tagItems.size() : tagItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (footerEnable && position == getItemCount() - 1) return FOOTER_ITEM;
        return NORMAL_ITEM;
    }

    private class TagViewHolder extends RecyclerView.ViewHolder {
        ImageView tagIcon;
        TextView tagTitle;

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            tagIcon = itemView.findViewById(R.id.item_tag_icon);
            tagTitle = itemView.findViewById(R.id.item_tag_title);
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
