package com.vividbobo.easy.ui.others.expandableRv;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.CategoryPresent;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.utils.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 折叠的RecyclerView
 *
 * @param
 */
public class ExpandableGroupRvAdapter extends RecyclerView.Adapter<ExpandableGroupRvAdapter.GroupViewHolder> {
    private static final String TAG = "ExpandableGroupRvAdapte";
    private List<CategoryPresent> items;
    private OnItemClickListener onAddClickListener;
    private Context mContext;

    public ExpandableGroupRvAdapter(Context mContext) {
        this.mContext = mContext;
        items = new ArrayList<>();
    }

    public void setOnAddClickListener(OnItemClickListener onAddClickListener) {
        this.onAddClickListener = onAddClickListener;
    }

    public ExpandableGroupRvAdapter(OnItemClickListener onAddClickListener) {
        this.onAddClickListener = onAddClickListener;
    }


    public void setItems(List<CategoryPresent> items) {
        Log.d(TAG, "setItems: ");
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_group_vh, parent, false);
        return new GroupViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        CategoryPresent item = items.get(position);
        holder.bind(item);
        holder.setOnAddClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle child add item click
                if (onAddClickListener != null) {
                    onAddClickListener.onItemClick(v, item, holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        private ImageView expandIv, moreIv, iconIv;
        private TextView title;
        private MaterialCardView dropContainerMcv;
        private RecyclerView childRv;
        private ExpandableChildRvAdapter adapter;


        private OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            expandIv = itemView.findViewById(R.id.group_vh_expand_iv);
            moreIv = itemView.findViewById(R.id.group_vh_more_iv);
            iconIv = itemView.findViewById(R.id.group_vh_icon_iv);
            title = itemView.findViewById(R.id.group_vh_title_tv);
            dropContainerMcv = itemView.findViewById(R.id.group_vh_container_mcv);
            childRv = itemView.findViewById(R.id.group_vh_child_rv);    //in xml init

            adapter = new ExpandableChildRvAdapter();
        }

        public void bind(CategoryPresent item) {
            Log.d(TAG, "bind: bind categoryParent");
            title.setText(item.getTitle());
            ResourceUtils.bindImageDrawable(mContext,ResourceUtils.getDrawable(item.getIconResName())).fitCenter().into(iconIv);

            expandIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.setExpanded(!item.isExpanded());
                    if (item.isExpanded()) {
                        dropContainerMcv.setVisibility(View.VISIBLE);
                        expandIv.setSelected(true);
                    } else {
                        dropContainerMcv.setVisibility(View.GONE);
                        expandIv.setSelected(false);
                    }
                }
            });
            moreIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, item, getAdapterPosition());
                    }
                }
            });

            childRv.setAdapter(adapter);
            adapter.setItems(item.getChildren());
        }

        public void setOnAddClickListener(View.OnClickListener onAddClickListener) {
            adapter.setOnAddClickListener(onAddClickListener);
        }
    }
}
