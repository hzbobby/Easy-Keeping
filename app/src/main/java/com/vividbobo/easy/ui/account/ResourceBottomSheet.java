package com.vividbobo.easy.ui.account;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.Itemzable;
import com.vividbobo.easy.adapter.adapter.ResourceAdapter;
import com.vividbobo.easy.databinding.SheetMoreCategoryBinding;
import com.vividbobo.easy.ui.common.BottomSheetDialog;
import com.vividbobo.easy.ui.others.OnItemClickListener;

import java.util.List;

/**
 * 资源选择bottom sheet
 */
public class ResourceBottomSheet<T extends Itemzable> extends BottomSheetDialog<SheetMoreCategoryBinding> {
    /**
     * 资源的相关属性
     */


    public static final String TAG = "ResourceBottomSheet";
    private static final String KEY_DATA = "data";
    private String title = null;
    private List<T> items;
    private ResourceAdapter<T> adapter;
    private OnItemClickListener onItemClickListener;    //点击回调

    public void setItems(List<T> items) {
        this.items = items;
        Log.d(TAG, "setItems: bottomSheet");
    }

    public ResourceBottomSheet<T> setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    public ResourceBottomSheet<T> setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public SheetMoreCategoryBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return SheetMoreCategoryBinding.inflate(inflater);
    }

    @Override
    public void onViewBinding(SheetMoreCategoryBinding binding) {
        if (title == null)
            binding.bottomMoreCategoryTitleTv.setText(R.string.resource_bottom_title);
        else binding.bottomMoreCategoryTitleTv.setText(title);

        binding.bottomMoreCategoryCloseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        adapter = new ResourceAdapter<>(getContext());
        adapter.setOnItemClickListener(onItemClickListener);
        Log.d(TAG, "onViewBinding: items is null? " + (items == null));
        binding.bottomMoreCategoryRv.setAdapter(adapter);
        adapter.updateItems(items);
    }
}
