package com.vividbobo.easy.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.ThemeUtils;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.vividbobo.easy.R;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.ui.others.OnItemLongClickListener;
import com.vividbobo.easy.ui.others.commonAdapter.CommonAdapter;
import com.vividbobo.easy.utils.LayoutManagerCloner;
import com.vividbobo.easy.utils.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <T> the type of child
 */
public class ExpandableAdapter<T extends ExpandableAdapter.ItemExpandableChild> extends CommonAdapter<ExpandableAdapter.ExpandableItem<T>, RecyclerView.ViewHolder, ExpandableAdapter.ExpandableVH<T>, RecyclerView.ViewHolder> {

    // child adapter layout manager
    private final RecyclerView.LayoutManager childLayoutManager;
    @LayoutRes
    private final int childLayoutRes;       //child item layout res id


    // enable parent item start icon
    private boolean enableParentIcon;
    // enable child item start icon
    private boolean enableChildIcon;

    // child item click
    private OnItemClickListener onChildItemClickListener;
    private OnItemLongClickListener onChildItemLongClickListener;


    public ExpandableAdapter(Context mContext, RecyclerView.LayoutManager childLayoutManager, @LayoutRes int childLayoutRes) {
        super(mContext);
        this.childLayoutManager = childLayoutManager;
        this.childLayoutRes = childLayoutRes;
    }

    public void setOnChildItemClickListener(OnItemClickListener onChildItemClickListener) {
        this.onChildItemClickListener = onChildItemClickListener;
    }

    public void setOnChildItemLongClickListener(OnItemLongClickListener onChildItemLongClickListener) {
        this.onChildItemLongClickListener = onChildItemLongClickListener;
    }

    public void setEnableParentIcon(boolean enableParentIcon) {
        this.enableParentIcon = enableParentIcon;
    }

    public void setEnableChildIcon(boolean enableChildIcon) {
        this.enableChildIcon = enableChildIcon;
    }


    @Override
    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateFooterViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected ExpandableAdapter.ExpandableVH<T> onCreateNormalViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expandable_parent, parent, false);

        return new ExpandableAdapter.ExpandableVH<>(view, childLayoutManager);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull ExpandableAdapter.ExpandableVH<T> holder, int position) {
        ExpandableItem<T> expandableItem = getItemByHolderPosition(position);
        holder.setEnableChildIcon(enableChildIcon);
        holder.setEnableParentIcon(enableParentIcon);
        holder.setOnChildItemClickListener(onChildItemClickListener);
        holder.setOnChildItemLongClickListener(onChildItemLongClickListener);
        holder.bind(mContext, childLayoutRes, expandableItem);
    }

    @Override
    protected void onBindHeaderViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    protected void onBindFooterViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    /**
     * @param <S> the type of child item in expandable parent item;
     */
    public interface ItemExpandable<S> {
        List<S> getChildrenItems();

        String getItemTitle();

        String getItemIconResName();
    }

    public interface ItemExpandableChild {
        String getItemTitle();

        String getItemSubTitle();

        String getItemIconResName();

    }

    /**
     * ExpandableItem model
     *
     * @param <T>
     */
    public static class ExpandableItem<T> {
        private String title;
        private String iconResName;
        private List<T> children;

        private int order;  //顺序

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public ExpandableItem(String title) {
            this.title = title;
            children = new ArrayList<>();
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIconResName() {
            return iconResName;
        }

        public void setIconResName(String iconResName) {
            this.iconResName = iconResName;
        }

        public List<T> getChildren() {
            return children;
        }

        public void setChildren(List<T> children) {
            this.children = children;
        }

        public void addChild(T item) {
            children.add(item);
        }
    }

    /**
     * the parent view holder
     *
     * @param <T> the type of child
     */
    public static class ExpandableVH<T extends ItemExpandableChild> extends RecyclerView.ViewHolder {

        private ImageView expandIv, iconIv;
        private TextView titleTv;

        private MaterialCardView contentView;
        private RecyclerView childRv;
        private boolean expanded = false;
        private int originalContentViewHeight = 0;
        private RecyclerView.LayoutManager layoutManager;
        private ChildAdapter<T> adapter;
        private ViewGroup.LayoutParams hideParams;
        private ViewGroup.LayoutParams extendParams;

        private boolean enableParentIcon;
        private boolean enableChildIcon;
        private OnItemClickListener onChildItemClickListener;   //child adapter click
        private OnItemLongClickListener onChildItemLongClickListener;   //child adapter long click

        public ExpandableVH(@NonNull View itemView, RecyclerView.LayoutManager layoutManager) {
            super(itemView);
            expandIv = itemView.findViewById(R.id.item_expand_iv);
            iconIv = itemView.findViewById(R.id.item_icon_iv);
            titleTv = itemView.findViewById(R.id.item_title_tv);
            contentView = itemView.findViewById(R.id.item_content_cv);
            contentView.setVisibility(View.GONE);

            childRv = itemView.findViewById(R.id.item_child_rv);
            if (layoutManager instanceof GridLayoutManager) {
                this.layoutManager = LayoutManagerCloner.clone(itemView.getContext(), (GridLayoutManager) layoutManager);
            } else {
                this.layoutManager = LayoutManagerCloner.clone(itemView.getContext(), (LinearLayoutManager) layoutManager);
            }
            childRv.setLayoutManager(this.layoutManager);

            hideParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            extendParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            expandIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expanded = !expanded;
                    if (expanded) {
                        //展开
//                        contentView.setLayoutParams(extendParams);
                        contentView.setVisibility(View.VISIBLE);
                    } else {
                        //关闭
//                        contentView.setLayoutParams(hideParams);
                        contentView.setVisibility(View.GONE);
                    }
                }
            });
        }

        public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
            this.layoutManager = layoutManager;
            childRv.setLayoutManager(layoutManager);
        }

        public void bind(Context context, @LayoutRes int childLayoutRes, ExpandableAdapter.ExpandableItem<T> item) {
            titleTv.setText(item.getTitle());
            if (enableParentIcon) {
                iconIv.setVisibility(View.VISIBLE);
                if (item.getIconResName() == null || item.getIconResName().isEmpty()) {
                    ResourceUtils.bindImageDrawable(context, ResourceUtils.getTextImageIcon(item.getTitle(),
                                    ResourceUtils.getColor(R.color.black)))
                            .centerInside().into(iconIv);
                } else {
                    ResourceUtils.bindImageDrawable(context, ResourceUtils.getDrawable(item.getIconResName()))
                            .centerInside().into(iconIv);
                }
            } else {
                iconIv.setVisibility(View.GONE);
            }
            //set adapter
            adapter = new ChildAdapter<>(context, childLayoutRes);
            adapter.setOnItemClickListener(onChildItemClickListener);
            adapter.setOnItemLongClickListener(onChildItemLongClickListener);
            adapter.setEnableIcon(enableChildIcon);
            adapter.updateItems(item.getChildren());
            childRv.setAdapter(adapter);
        }

        public boolean isExpanded() {
            return expanded;
        }

        public void setExpanded(boolean expanded) {
            this.expanded = expanded;
        }

        public void setEnableParentIcon(boolean enableParentIcon) {
            this.enableParentIcon = enableParentIcon;
        }

        public void setEnableChildIcon(boolean enableChildIcon) {
            this.enableChildIcon = enableChildIcon;
        }

        public void setOnChildItemClickListener(OnItemClickListener onChildItemClickListener) {
            this.onChildItemClickListener = onChildItemClickListener;
        }

        public void setOnChildItemLongClickListener(OnItemLongClickListener onChildItemLongClickListener) {
            this.onChildItemLongClickListener = onChildItemLongClickListener;
        }

        public ChildAdapter<T> getAdapter() {
            return adapter;
        }
    }

    /**
     * the child recycler view adapter
     *
     * @param <T> the type of child item
     */
    public static class ChildAdapter<T extends ItemExpandableChild> extends CommonAdapter<T, RecyclerView.ViewHolder, ChildVH<T>, RecyclerView.ViewHolder> {

        /**
         * the item layout have 3 component id:
         * 1. item_icon_iv: ImageView
         * 2. item_title_tv: TextView
         * 3. item_sub_title_tv: TextView
         * you need to set this 3 id of components in your item layout
         * <p>
         * the layout manager was set by ExpandableAdapter
         */
        @LayoutRes
        private int itemLayoutRes;
        private boolean enableIcon;

        public ChildAdapter(Context mContext, @LayoutRes int itemLayoutRes) {
            super(mContext);
            this.itemLayoutRes = itemLayoutRes;
        }

        @Override
        protected RecyclerView.ViewHolder onCreateHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        protected RecyclerView.ViewHolder onCreateFooterViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        protected ChildVH<T> onCreateNormalViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(itemLayoutRes, parent, false);
            return new ChildVH<>(view);
        }

        @Override
        protected void onBindNormalViewHolder(@NonNull ChildVH<T> holder, int position) {
            T item = getItemByHolderPosition(position);
            holder.setEnableIcon(enableIcon);
            holder.bind(mContext, item);
        }

        @Override
        protected void onBindHeaderViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        protected void onBindFooterViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        }

        public void setEnableIcon(boolean enableChildIcon) {
            this.enableIcon = enableChildIcon;
        }
    }

    public static class ChildVH<T extends ItemExpandableChild> extends RecyclerView.ViewHolder {
        private ImageView iconIv;
        private TextView titleTv, subTitleTv;
        private boolean enableIcon;

        public ChildVH(@NonNull View itemView) {
            super(itemView);
            iconIv = itemView.findViewById(R.id.item_icon_iv);
            titleTv = itemView.findViewById(R.id.item_title_tv);
            subTitleTv = itemView.findViewById(R.id.item_sub_title_tv);
        }

        public void bind(Context context, T item) {
            titleTv.setText(item.getItemTitle());
            subTitleTv.setText(item.getItemSubTitle());
            if (enableIcon) {
                iconIv.setVisibility(View.VISIBLE);
                if (item.getItemIconResName() == null || item.getItemIconResName().isEmpty()) {
                    ResourceUtils.bindImageDrawable(context, ResourceUtils.getTextImageIcon(item.getItemTitle(),
                                    ResourceUtils.getColor(R.color.black)))
                            .centerInside().into(iconIv);
                } else {
                    ResourceUtils.bindImageDrawable(context, ResourceUtils.getDrawable(item.getItemIconResName()))
                            .centerInside().into(iconIv);
                }
            } else {
                iconIv.setVisibility(View.GONE);
            }

        }

        public void setEnableIcon(boolean enableIcon) {
            this.enableIcon = enableIcon;
        }

    }

}
