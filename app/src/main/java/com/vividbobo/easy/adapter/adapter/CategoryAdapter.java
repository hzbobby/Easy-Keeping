package com.vividbobo.easy.adapter.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.CategoryPresent;
import com.vividbobo.easy.ui.others.commonAdapter.CommonAdapter;
import com.vividbobo.easy.utils.ResourceUtils;

import java.util.Objects;

/**
 * TODO 1.
 */
public class CategoryAdapter extends CommonAdapter<CategoryPresent, RecyclerView.ViewHolder, CategoryAdapter.NormalViewHolder, CategoryAdapter.FooterViewHolder> {
    private int selectPosition = 0;
//    private Category selectedCategory;

    public CategoryAdapter(Context mContext) {
        super(mContext);
        setEnableFooter(true);
    }


    @Override
    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected FooterViewHolder onCreateFooterViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_category, parent, false);
        return new FooterViewHolder(view);
    }

    @Override
    protected NormalViewHolder onCreateNormalViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_category, parent, false);
        return new NormalViewHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onBindNormalViewHolder(@NonNull CategoryAdapter.NormalViewHolder holder, int position) {
        //override click
        CategoryPresent categoryPresent = getItemByHolderPosition(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(holder.iconIv, categoryPresent, holder.getAdapterPosition());
                }
            }
        });
        holder.bind(categoryPresent);

        if (selectPosition == holder.getAdapterPosition()) {
            if (categoryPresent.getType() == Category.TYPE_EXPENDITURE) {
                Log.d("TAG", "onBindNormalViewHolder: set expenditure color icon");
                ResourceUtils.bindImageDrawable(mContext, ResourceUtils.getTintedDrawable(categoryPresent.getIconResName(), ContextCompat.getColor(mContext, R.color.expenditure))).centerInside().into(holder.iconIv);
            } else {
                ResourceUtils.bindImageDrawable(mContext, ResourceUtils.getTintedDrawable(categoryPresent.getIconResName(), ContextCompat.getColor(mContext, R.color.income))).centerInside().into(holder.iconIv);
            }
        } else {
            ResourceUtils.bindImageDrawable(mContext, ResourceUtils.getTintedDrawable(categoryPresent.getIconResName(), ContextCompat.getColor(mContext, R.color.black))).centerInside().into(holder.iconIv);
        }

//        if (selectedCategory != null) {
//            Integer selectedId = null;
//            if (Objects.isNull(selectedCategory.getParentId()) || selectedCategory.getParentId() == Category.DEFAULT_PARENT_ID) {
//                //parent
//                selectedId = selectedCategory.getId();
//            } else {
//                // is child category
//                selectedId = selectedCategory.getParentId();
//            }
//            if (selectedId != null && selectedId == categoryPresent.getId()) {
//                //定位到该位置
//                setItemSelected(holder.getAdapterPosition());
//            }
//        }

    }

    @Override
    protected void onBindHeaderViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    protected void onBindFooterViewHolder(@NonNull CategoryAdapter.FooterViewHolder holder, int position) {

    }

    public void setItemSelected(int position) {
        int oldPosition = selectPosition;
        selectPosition = position;
        notifyItemChanged(oldPosition);
        notifyItemChanged(selectPosition);
    }

    public void setSelectedCategory(Category category) {
        Log.d("TAG", "setSelectedCategory: ");
        for (int i = 0; i < items.size(); i++) {
            int id;
            if (Objects.isNull(category.getParentId()) || category.getParentId() == Category.DEFAULT_PARENT_ID) {
                id = category.getId();
            } else {
                id = category.getParentId();
            }
            if (id == items.get(i).getId()) {
                items.get(i).setPresentCategory(category);
                setItemSelected(getHolderPosition(i));
                break;
            }
        }
    }

    public class NormalViewHolder extends RecyclerView.ViewHolder {
        private ImageView iconIv, moreIv;
        private TextView titleTv;

        public NormalViewHolder(@NonNull View itemView) {
            super(itemView);
            iconIv = itemView.findViewById(R.id.grid_item_icon_iv);
            titleTv = itemView.findViewById(R.id.grid_item_title_tv);
            moreIv = itemView.findViewById(R.id.grid_item_more_iv);
        }

        public void bind(CategoryPresent item) {
            ResourceUtils.bindImageDrawable(mContext, ResourceUtils.getDrawable(item.getIconResName())).fitCenter().into(iconIv);
            titleTv.setText(item.getTitle());

            if (!item.getChildren().isEmpty()) {
                moreIv.setVisibility(View.VISIBLE);
            } else {
                moreIv.setVisibility(View.GONE);
            }
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        private ImageView iconIv;
        private TextView titleTv;

        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);
            iconIv = itemView.findViewById(R.id.grid_item_icon_iv);
            titleTv = itemView.findViewById(R.id.grid_item_title_tv);
            ResourceUtils.bindImageDrawable(mContext, ResourceUtils.getTintedDrawable(R.drawable.menu_ic_setting, ContextCompat.getColor(mContext, R.color.black))).fitCenter().into(iconIv);
            iconIv.setImageResource(R.drawable.menu_ic_setting);
            titleTv.setText(R.string.setting);
            itemView.findViewById(R.id.grid_item_more_iv).setVisibility(View.GONE);
        }
    }
}
