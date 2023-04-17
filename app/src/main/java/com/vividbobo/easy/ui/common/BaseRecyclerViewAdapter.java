package com.vividbobo.easy.ui.common;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.ui.others.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <T>  data type
 * @param <VH> normal item view holder
 * @param <H>  header view holder
 * @param <F>  footer view holder
 */
public abstract class BaseRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder, H extends RecyclerView.ViewHolder, F extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "BaseRecyclerViewAdapter";
    public static final Short ITEM_TYPE_NORMAL = 0;
    public static final Short ITEM_TYPE_HEADER = 1;
    public static final Short ITEM_TYPE_FOOTER = 2;

    protected List<T> items;


    private boolean enableFooter = false;
    private boolean enableHeader = false;


    protected OnItemClickListener onItemClickListener;
    protected View.OnClickListener onHeaderClickListener;
    protected View.OnClickListener onFooterClickListener;

    public void setOnHeaderClickListener(View.OnClickListener onHeaderClickListener) {
        this.onHeaderClickListener = onHeaderClickListener;
    }

    public void setOnFooterClickListener(View.OnClickListener onFooterClickListener) {
        this.onFooterClickListener = onFooterClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    protected void setEnableFooter(boolean enableFooter) {
        this.enableFooter = enableFooter;
    }

    public boolean isEnableFooter() {
        return enableFooter;
    }

    public boolean isEnableHeader() {
        return enableHeader;
    }

    protected void setEnableHeader(boolean enableHeader) {
        this.enableHeader = enableHeader;
    }

    @Override
    public int getItemViewType(int position) {
        //如果启用header且该位置是0，则类型为HEADER
        //如果启用footer,且该位置是最后一个,则为FOOTER
        //其余为NORMAL
        if (enableHeader && position == 0) return ITEM_TYPE_HEADER;
        if (enableFooter && position == getItemCount() - 1) return ITEM_TYPE_FOOTER;
        return ITEM_TYPE_NORMAL;
    }

    public BaseRecyclerViewAdapter() {
        this.items = new ArrayList<>();
    }


    /**
     * 复用 onCreateViewHolder,在子类中重写getXXXViewHolder()进行ViewHolder的转化
     * 可以不用重写 onCreateViewHolder
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == ITEM_TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(getHeaderLayoutRes(), parent, false);
            return getHeaderViewHolder(view);
        } else if (viewType == ITEM_TYPE_FOOTER) {
            view = LayoutInflater.from(parent.getContext()).inflate(getFooterLayoutRes(), parent, false);
            return getFooterViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(getNormalLayoutRes(), parent, false);
            return getNormalViewHolder(view);
        }
    }

    /**
     * 获取 item 的 layout id
     *
     * @return
     */
    @LayoutRes
    protected abstract Integer getNormalLayoutRes();

    @LayoutRes
    protected abstract Integer getFooterLayoutRes();

    @LayoutRes
    protected abstract Integer getHeaderLayoutRes();

    /**
     * 获取item的ViewHolder
     * 通常直接返回 new NormalViewHolder(view)即可
     *
     * @param view
     * @return
     */
    protected abstract VH getNormalViewHolder(View view);

    protected abstract H getHeaderViewHolder(View view);

    protected abstract F getFooterViewHolder(View view);

    /**
     * 重写了该方法，将该方法分为 onBindNormalViewHolder, onBindHeaderViewHolder, onBindFooterViewHolder,
     * 您需要在派生类Adapter中重写此3个方法以替代onBindViewHolder
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Log.d(TAG, "onBindViewHolder: position, itemCount,viewType: " + String.format("%d, %d, %d", position, getItemCount(), getItemViewType(position)));

        if (getItemViewType(position) == ITEM_TYPE_NORMAL) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(view, items.get(holder.getAdapterPosition()), holder.getAdapterPosition());
                    }
                }
            });
            onBindNormalViewHolder((VH) holder, position);
        } else if (getItemViewType(position) == ITEM_TYPE_FOOTER) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onFooterClickListener != null) {
                        onFooterClickListener.onClick(view);
                    }
                }
            });
            onBindFooterViewHolder((F) holder, position);
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onHeaderClickListener != null) {
                        onHeaderClickListener.onClick(view);
                    }
                }
            });
            onBindHeaderViewHolder((H) holder, position);
        }
    }

    protected abstract void onBindNormalViewHolder(@NonNull VH holder, @SuppressLint("RecyclerView") int position);

    protected abstract void onBindHeaderViewHolder(@NonNull H holder, @SuppressLint("RecyclerView") int position);

    protected abstract void onBindFooterViewHolder(@NonNull F holder, @SuppressLint("RecyclerView") int position);

    public int getItemPosition(int holderPosition) {
        if (isEnableHeader()) holderPosition -= 1;
        return holderPosition;
    }

    @Override
    public int getItemCount() {
        //若有header,总数+1,否则+0
        //若有footer,总数+1,否则+0

        return items.size() + (enableHeader ? 1 : 0) + (enableFooter ? 1 : 0);
    }

    public T getItem(int position) {
        return items.get(position);
    }

    public void updateItems(List<T> newItems) {
        items = newItems;
        notifyDataSetChanged();
    }

    public void addItem(T item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }
}

