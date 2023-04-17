package com.vividbobo.easy.ui.bill;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.CategoryPresent;
import com.vividbobo.easy.databinding.SheetMoreCategoryBinding;
import com.vividbobo.easy.ui.common.BottomSheetDialog;
import com.vividbobo.easy.ui.others.OnItemClickListener;

public class MoreCategoryBottomSheet extends BottomSheetDialog<SheetMoreCategoryBinding> {
    public static final String TAG = "MoreCategoryBottomSheet";
    private static final String KEY_DATA = "data";
    private OnItemChangeListener onItemChangeListener;

    public static MoreCategoryBottomSheet newInstance() {
        Bundle args = new Bundle();
        MoreCategoryBottomSheet fragment = new MoreCategoryBottomSheet();
        fragment.setArguments(args);
        return fragment;
    }

    public static MoreCategoryBottomSheet newInstance(CategoryPresent categoryPresent) {
        MoreCategoryBottomSheet fragment = newInstance();
        fragment.getArguments().putParcelable(KEY_DATA, categoryPresent);
        return fragment;
    }

    public static MoreCategoryBottomSheet newInstance(CategoryPresent categoryPresent, OnItemChangeListener onItemChangeListener) {
        MoreCategoryBottomSheet fragment = newInstance();
        fragment.getArguments().putParcelable(KEY_DATA, categoryPresent);
        fragment.setNotifyItemChange(onItemChangeListener);
        return fragment;
    }

    @Override
    public SheetMoreCategoryBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return SheetMoreCategoryBinding.inflate(inflater);
    }

    public void setNotifyItemChange(OnItemChangeListener onItemChangeListener) {
        this.onItemChangeListener = onItemChangeListener;
    }

    @Override
    public void onViewBinding(SheetMoreCategoryBinding binding) {
        CategoryPresent categoryPresent = getArguments().getParcelable(KEY_DATA);

        binding.bottomMoreCategoryCloseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.bottomMoreCategoryTitleTv.setText(categoryPresent.getParent().getTitle());

        MoreCategoryAdapter moreCategoryAdapter = new MoreCategoryAdapter(getContext());
        moreCategoryAdapter.setHeaderItem(categoryPresent.getParent());
        moreCategoryAdapter.updateItems(categoryPresent.getChildren());
        moreCategoryAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                categoryPresent.setPresentCategory((Category) item);
                onItemChangeListener.onChange();
                dismiss();
            }
        });
        moreCategoryAdapter.setOnHeaderClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                categoryPresent.setPresentCategory((Category) item);
                onItemChangeListener.onChange();
                dismiss();
            }
        });

        binding.bottomMoreCategoryRv.setAdapter(moreCategoryAdapter);
    }
}
