package com.vividbobo.easy.ui.others;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.Resource;
import com.vividbobo.easy.utils.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

public class ChildRvAdapter extends RecyclerView.Adapter<ChildRvAdapter.ChildViewHolder> {
    private Context mContext;
    private static final String TAG = "ChildRvAdapter";
    private List<Resource> items;
    private OnItemClickListener onItemClickListener;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public ChildRvAdapter(Context mContext) {
        this.mContext = mContext;
        this.items = new ArrayList<>();
    }

    @NonNull
    @Override
    public ChildRvAdapter.ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_icon_2, parent, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildRvAdapter.ChildViewHolder holder, int position) {
        Resource resource = items.get(position);
        holder.bind(resource);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ChildRv");
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, resource, holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Resource> childItems) {
        this.items = childItems;
        notifyDataSetChanged();
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.item_icon);
        }

        public void bind(Resource resource) {
            ResourceUtils.bindImageDrawable(mContext, ResourceUtils.getDrawable(resource.getResName())).fitCenter().into(icon);
        }
    }
}
