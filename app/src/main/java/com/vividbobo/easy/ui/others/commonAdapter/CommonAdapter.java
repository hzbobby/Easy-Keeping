package com.vividbobo.easy.ui.others.commonAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.ui.others.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个带 Header, Footer, NormalItem 且支持最大显示item数量的RecyclerView Adapter
 *
 * @param <T> type of item
 * @param <HEADER> header view holder
 * @param <NORMAL> item view holder
 * @param <FOOTER> footer view holder
 */
public abstract class CommonAdapter<T, HEADER extends RecyclerView.ViewHolder, NORMAL extends RecyclerView.ViewHolder, FOOTER extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "BaseRecyclerViewAdapter";
    public static final Short ITEM_TYPE_NORMAL = 0;
    public static final Short ITEM_TYPE_HEADER = 1;
    public static final Short ITEM_TYPE_FOOTER = 2;

    protected Context mContext;

    private int maxCount = 0;
    protected Object headerItem;
    protected Object footerItem;
    // the items
    protected List<T> items;
    // enable header & footer
    private boolean enableHeader = false;
    private boolean enableFooter = false;
    private boolean enableMaxCount = false;
    // click
    protected OnItemClickListener onItemClickListener;
    protected OnItemClickListener onHeaderClickListener;
    protected OnItemClickListener onFooterClickListener;

    //long click
    protected OnItemLongClickListener onItemLongClickListener;

    public CommonAdapter(Context mContext) {
        this.mContext = mContext;
        this.items = new ArrayList<>();
    }

    public void updateHeaderItem(Object object) {
        this.headerItem = object;
        notifyItemChanged(0);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void updateFooterItem(Object object) {
        this.footerItem = object;
        notifyItemChanged(getItemCount());
    }

    public boolean isEnableMaxCount() {
        return enableMaxCount;
    }

    public void setEnableMaxCount(boolean enableMaxCount) {
        this.enableMaxCount = enableMaxCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public Object getHeaderItem() {
        return headerItem;
    }

    public Object getFooterItem() {
        return footerItem;
    }

    public void setHeaderItem(Object headerItem) {
        this.headerItem = headerItem;
        notifyItemChanged(0);
    }

    public void setFooterItem(Object footerItem) {
        this.footerItem = footerItem;
    }

    public void setOnHeaderClickListener(OnItemClickListener onHeaderClickListener) {
        this.onHeaderClickListener = onHeaderClickListener;
    }

    public void setOnFooterClickListener(OnItemClickListener onFooterClickListener) {
        this.onFooterClickListener = onFooterClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public void setEnableFooter(boolean enableFooter) {
        this.enableFooter = enableFooter;
    }

    public boolean isEnableFooter() {
        return enableFooter;
    }

    public boolean isEnableHeader() {
        return enableHeader;
    }

    public void setEnableHeader(boolean enableHeader) {
        this.enableHeader = enableHeader;
    }

    /**
     * get viewTypes which is type of header, footer, normal item
     *
     * @param position position to query
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        //如果启用header且该位置是0，则类型为HEADER
        //如果启用footer,且该位置是最后一个,则为FOOTER
        //其余为NORMAL
        if (enableHeader && position == 0) return ITEM_TYPE_HEADER;
        if (enableFooter && position == getItemCount() - 1) return ITEM_TYPE_FOOTER;
        return ITEM_TYPE_NORMAL;
    }


    /**
     * if you want to override this method, may be you need to super.onCreateViewHolder or
     * to compare the viewType with your custom viewType.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_HEADER) {
            HEADER header = onCreateHeaderViewHolder(parent, viewType);
            onHeaderCreated(header);
            return header;
        } else if (viewType == ITEM_TYPE_FOOTER) {
            FOOTER footer = onCreateFooterViewHolder(parent, viewType);
            onFooterCreated(footer);
            return footer;
        } else {
            NORMAL normal = onCreateNormalViewHolder(parent, viewType);
            onNormalCreated(normal);
            return normal;
        }
    }

    protected void onHeaderCreated(HEADER header) {
        // child adapter do some specify
    }

    protected void onFooterCreated(FOOTER footer) {
        // child adapter do some specify
    }

    protected void onNormalCreated(NORMAL normal) {
        // child adapter do some specify
    }

    /**
     * needed to override
     * code may like
     * View view=LayoutInflate.inflate()
     * return HeaderViewHolder(view);   //the HeaderViewHolder is your custom HeaderViewHolder
     *
     * @return return the HeaderViewHolder
     */
    protected abstract HEADER onCreateHeaderViewHolder(@NonNull ViewGroup parent, int viewType);

    /**
     * needed to override
     * code may like
     * View view=LayoutInflate.inflate()
     * return FooterViewHolder(view);   //the FooterViewHolder is your custom FooterViewHolder
     *
     * @return return the FooterViewHolder
     */
    protected abstract FOOTER onCreateFooterViewHolder(@NonNull ViewGroup parent, int viewType);

    /**
     * needed to override
     * code may like
     * View view=LayoutInflate.inflate()
     * return NormalViewHolder(view);   //the NormalViewHolder is your custom NormalViewHolder
     *
     * @return return the NormalViewHolder
     */
    protected abstract NORMAL onCreateNormalViewHolder(@NonNull ViewGroup parent, int viewType);


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (getItemViewType(position) == ITEM_TYPE_NORMAL) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(view, getItemByHolderPosition(holder.getAdapterPosition()), holder.getAdapterPosition());
                    }
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(getItemByHolderPosition(holder.getAdapterPosition()), holder.getAdapterPosition());
                        return true;
                    }
                    return false;
                }
            });
            onBindNormalViewHolder((NORMAL) holder, position);
        } else if (getItemViewType(position) == ITEM_TYPE_FOOTER) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onFooterClickListener != null) {
                        onFooterClickListener.onItemClick(view, headerItem, holder.getAdapterPosition());
                    }
                }
            });
            onBindFooterViewHolder((FOOTER) holder, position);
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onHeaderClickListener != null) {
                        onHeaderClickListener.onItemClick(view, headerItem, holder.getAdapterPosition());

                    }
                }
            });
            onBindHeaderViewHolder((HEADER) holder, position);
        }
    }

    /**
     * bind your normal item
     *
     * @param holder
     * @param position
     */
    protected abstract void onBindNormalViewHolder(@NonNull NORMAL holder, @SuppressLint("RecyclerView") int position);

    /**
     * bind your header
     *
     * @param holder
     * @param position
     */
    protected abstract void onBindHeaderViewHolder(@NonNull HEADER holder, @SuppressLint("RecyclerView") int position);

    /**
     * bind your footer
     *
     * @param holder
     * @param position
     */
    protected abstract void onBindFooterViewHolder(@NonNull FOOTER holder, @SuppressLint("RecyclerView") int position);

    @Override
    public int getItemCount() {
        //enable header then count+1, so do footer.
        int cnt = items.size();
        if (enableMaxCount) {
            cnt = maxCount > items.size() ? items.size() : maxCount;
        }
        return cnt + (enableHeader ? 1 : 0) + (enableFooter ? 1 : 0);
    }

    public int getItemPosition(int holderPosition) {
        if (enableHeader) holderPosition -= 1;
        return holderPosition;
    }

    public int getHolderPosition(int itemPosition) {
        if (enableHeader) itemPosition += 1;
        return itemPosition;
    }

    public T getItemByHolderPosition(int holderPosition) {
        return items.get(getItemPosition(holderPosition));
    }

    public T getItem(int position) {
        return items.get(position);
    }

    public void updateItems(List<T> newItems) {
        items = newItems;
        Log.d(TAG, "updateItems: ");
        notifyDataSetChanged();
    }

    public void addItem(T item) {
        items.add(item);
        notifyItemInserted(enableHeader ? items.size() : items.size() - 1);
    }

    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(getHolderPosition(position));
    }
}
