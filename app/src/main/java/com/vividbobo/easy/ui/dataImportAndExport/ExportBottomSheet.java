package com.vividbobo.easy.ui.dataImportAndExport;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.vividbobo.easy.adapter.Itemzable;
import com.vividbobo.easy.adapter.adapter.CheckableAdapter;
import com.vividbobo.easy.database.model.CheckableItem;
import com.vividbobo.easy.databinding.SheetPickerBinding;
import com.vividbobo.easy.ui.common.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class ExportBottomSheet extends BottomSheetDialog<SheetPickerBinding> {
    public static final String TAG = "FilterBottomSheet";
    private View.OnClickListener onClearClickListener;
    private View.OnClickListener onDoneClickListener;
    private CheckableAdapter adapter;
    private List<CheckableItem> items;
    private boolean allChecked;

    public ExportBottomSheet() {
        items = new ArrayList<>();
    }

    public <T extends Itemzable> void updateItems(List<CheckableItem<T>> items) {
        this.items.clear();
        this.items.addAll(items);
    }

    public void setOnClearClickListener(View.OnClickListener onClearClickListener) {
        this.onClearClickListener = onClearClickListener;
    }

    public void setOnDoneClickListener(View.OnClickListener onDoneClickListener) {
        this.onDoneClickListener = onDoneClickListener;
    }


    private void configAdapter() {
        adapter = new CheckableAdapter(getActivity());
        adapter.updateItems(items);
        adapter.setEnableIcon(true);
    }

    @Override
    public SheetPickerBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return SheetPickerBinding.inflate(inflater);
    }

    @Override
    public void onViewBinding(SheetPickerBinding binding) {
        configToolBar(binding);
        configAdapter();
        configRv(binding);
    }

    private void configRv(SheetPickerBinding binding) {
        binding.dateRv.setAdapter(adapter);
    }

    private void configToolBar(SheetPickerBinding binding) {
        if (allChecked) {
            binding.clearSelectedTv.setText("全不选");
        } else {
            binding.clearSelectedTv.setText("全选");
        }
        binding.clearSelectedTv.setOnClickListener(v -> {
            allChecked = !allChecked;
            adapter.setAllChecked(allChecked);
            if (allChecked) {
                binding.clearSelectedTv.setText("全不选");
            } else {
                binding.clearSelectedTv.setText("全选");
            }
        });
        binding.doneTv.setOnClickListener(v -> {
            if (onDoneClickListener != null) {
                onDoneClickListener.onClick(v);
            }
        });
    }

    public void setAllChecked(boolean b) {
        this.allChecked = b;
    }
}
