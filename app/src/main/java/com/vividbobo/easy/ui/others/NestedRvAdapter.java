package com.vividbobo.easy.ui.others;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.R;
import com.vividbobo.easy.database.model.ChildRvItem;

import java.util.ArrayList;
import java.util.List;

public class NestedRvAdapter extends RecyclerView.Adapter<NestedRvAdapter.NestedRvViewHolder> {
    private static final String TAG = "NestedRvAdapter";

    private Context mContext;
    private List<ChildRvItem> childRvItems;
    private OnItemClickListener onItemClickListener;

    public NestedRvAdapter(Context mContext) {
        this.mContext = mContext;
        childRvItems = new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        notifyDataSetChanged();
    }


    public void setChildRvItems(List<ChildRvItem> childRvItems) {
        this.childRvItems = childRvItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NestedRvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nested_recyclerview, parent, false);
        return new NestedRvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NestedRvViewHolder holder, int position) {
        ChildRvItem item = childRvItems.get(position);
        holder.bind(item, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return childRvItems.size();
    }

    public class NestedRvViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private RecyclerView recyclerView;
        private ChildRvAdapter adapter;


        public NestedRvViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_nested_rv_title_tv);
            recyclerView = itemView.findViewById(R.id.item_nested_rv);
            recyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(), 5));
            adapter = new ChildRvAdapter(mContext);
            recyclerView.setAdapter(adapter);
        }


        public void bind(ChildRvItem item, OnItemClickListener onItemClickListener) {
            this.title.setText(item.getTitle());
            adapter.setItems(item.getChildItems());
            adapter.setOnItemClickListener(onItemClickListener);
        }
    }
}
