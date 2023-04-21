package com.vividbobo.easy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vividbobo.easy.R;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.utils.ResourceUtils;

import java.util.List;

public class DropdownMenuAdapter<T extends DropdownMenuAdapter.DropdownMenuItem> extends BaseAdapter implements Filterable {
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                return null;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

            }
        };
    }

    public interface DropdownMenuItem {
        String getItemIconResName();

        String getItemTitle();

        String getItemDesc();
    }

    private List<T> items;
    private OnItemClickListener onItemClickListener;
    private Context mContext;

    private boolean enableIcon = false;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setEnableIcon(boolean enableIcon) {
        this.enableIcon = enableIcon;
    }

    public boolean isEnableIcon() {
        return enableIcon;
    }

    public DropdownMenuAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setItems(List<T> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.indexOf(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2, parent, false);
        } else {
            view = convertView;
        }

        T item = (T) getItem(position);
        // bind the title
        TextView titleTv = view.findViewById(R.id.item_title_tv);
        TextView descTv = view.findViewById(R.id.item_desc_tv);
        if (item.getItemDesc() == null || item.getItemDesc().isEmpty()) {
            descTv.setVisibility(View.GONE);
        } else {
            descTv.setVisibility(View.VISIBLE);
            descTv.setText(item.getItemDesc());
        }

        titleTv.setText(item.getItemTitle());
        ImageView iconIv = view.findViewById(R.id.item_icon_iv);
        if (enableIcon) {
            if (item.getItemIconResName() == null || item.getItemIconResName().isEmpty()) {
                //显示第一个文字
                ResourceUtils.bindImageDrawable(mContext, ResourceUtils.getTextImageIcon(item.getItemTitle(),
                                ResourceUtils.getColor(R.color.black)))
                        .centerInside().into(iconIv);
            } else {
                //显示图片
                ResourceUtils.bindImageDrawable(mContext, ResourceUtils.getDrawable(item.getItemIconResName()))
                        .centerInside().into(iconIv);
            }
        } else {
            iconIv.setVisibility(View.GONE);
        }
        //click listener
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, item, position);
                }
            }
        });
        return view;
    }
}
