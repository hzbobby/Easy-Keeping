package com.vividbobo.easy.ui.bill;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.vividbobo.easy.R;
import com.vividbobo.easy.model.TagItem;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.utils.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

public class RemarkTagAdapter extends RecyclerView.Adapter<RemarkTagAdapter.ChipViewHolder> {
    private static final String TAG = "RemarkTagAdapter";
    private static final int ADD_ITEM = 0;
    private static final int NORMAL_ITEM = 1;

    private List<TagItem> tags;
    private View.OnClickListener onAddChipClickListener;
    private OnItemClickListener onItemClickListener;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnAddChipClickListener(View.OnClickListener onAddChipClickListener) {
        this.onAddChipClickListener = onAddChipClickListener;
    }

    public RemarkTagAdapter() {
        tags = new ArrayList<>();
    }

    public void setTags(List<TagItem> tags) {
        this.tags = tags;
        notifyDataSetChanged();
    }

    private boolean addChipEnable = true;

    public void setAddChipEnable(boolean addChipEnable) {
        this.addChipEnable = addChipEnable;
    }

    @Override
    public int getItemViewType(int position) {
        //最后一个为添加标签
        if (addChipEnable && position == getItemCount() - 1) {
            return ADD_ITEM;
        }

        return NORMAL_ITEM;
    }

    @NonNull
    @Override
    public RemarkTagAdapter.ChipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_chip, parent, false);
        ChipViewHolder chipViewHolder = new ChipViewHolder(view);

        return chipViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RemarkTagAdapter.ChipViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (getItemViewType(position) == NORMAL_ITEM) {
            //set text
            TagItem tagItem = tags.get(position);
            holder.chip.setChecked(tagItem.isChecked());
            holder.chip.setText(tagItem.getText());
            holder.chip.setTextColor(Color.parseColor(tagItem.getColor()));
            holder.chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tagItem.setChecked(holder.chip.isChecked());
                    if (onItemClickListener != null) {
                        onItemClickListener.OnItemClick(tagItem, position);
                    }
                }
            });

        } else {
            holder.chip.setChipIcon(ResourceUtils.getDrawable(R.drawable.ic_add));
            holder.chip.setText("新增标签");
            holder.chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onAddChipClickListener != null) {
                        onAddChipClickListener.onClick(view);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return addChipEnable ? tags.size() + 1 : tags.size();
    }

    public class ChipViewHolder extends RecyclerView.ViewHolder {
        Chip chip;

        public ChipViewHolder(@NonNull View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.item_tag_chip);

        }
    }
}
