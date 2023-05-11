package com.vividbobo.easy.ui.bill;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.CategoryPresent;
import com.vividbobo.easy.databinding.SheetMoreCategoryBinding;
import com.vividbobo.easy.ui.common.BottomSheetDialog;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.viewmodel.CategoryViewModel;
import com.vividbobo.easy.viewmodel.MoreCategoryViewModel;

import java.util.List;
import java.util.Objects;

public class MoreCategoryBottomSheet extends BottomSheetDialog<SheetMoreCategoryBinding> {
    public static final String TAG = "MoreCategoryBottomSheet";
    private static final String KEY_DATA = "data";
    private static final String BILL_TYPE = "billType";
    private OnItemChangeListener onItemChangeListener;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private MoreCategoryViewModel moreCategoryViewModel;

    public static MoreCategoryBottomSheet newInstance(Integer billType) {

        Bundle args = new Bundle();
        args.putSerializable(BILL_TYPE, billType);
        MoreCategoryBottomSheet fragment = new MoreCategoryBottomSheet();
        fragment.setArguments(args);
        return fragment;
    }

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
        binding.bottomMoreCategoryTitleTv.setText("选择类别");
        CategoryPresent categoryPresent = getArguments().getParcelable(KEY_DATA);

        binding.bottomMoreCategoryCloseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        MoreCategoryAdapter moreCategoryAdapter = new MoreCategoryAdapter(getContext());
        binding.bottomMoreCategoryRv.setAdapter(moreCategoryAdapter);

        //二级类别
        if (Objects.nonNull(categoryPresent)) {
            binding.bottomMoreCategoryTitleTv.setText(categoryPresent.getParent().getTitle());

            moreCategoryAdapter.setEnableHeader(true);
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
        }
        // 作为普通的选择类别sheet
        Integer billType = getArguments().getInt(BILL_TYPE, -1);
        if (billType != -1) {
            moreCategoryViewModel = new ViewModelProvider(this).get(MoreCategoryViewModel.class);
            moreCategoryViewModel.setBillType(billType);
            moreCategoryViewModel.getCategories().observe(this, new Observer<List<Category>>() {
                @Override
                public void onChanged(List<Category> categories) {
                    moreCategoryAdapter.updateItems(categories);
                }
            });
            moreCategoryAdapter.setOnItemClickListener(onItemClickListener);
        }
    }
}
