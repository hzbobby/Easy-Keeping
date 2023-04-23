package com.vividbobo.easy.ui.store;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.adapter.BaseEntityAdapter;
import com.vividbobo.easy.database.model.Store;
import com.vividbobo.easy.databinding.DialogCommonPickerBinding;
import com.vividbobo.easy.ui.common.BaseMaterialDialog;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.viewmodel.StoreViewModel;

import java.util.List;

public class StorePicker extends BaseMaterialDialog<DialogCommonPickerBinding>  {
    public static final String TAG = "StorePicker";

    private OnItemClickListener onItemClickListener;
    private StoreViewModel storeViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeViewModel = new ViewModelProvider(getActivity()).get(StoreViewModel.class);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    protected DialogCommonPickerBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return DialogCommonPickerBinding.inflate(inflater);
    }

    @Override
    protected void onBuildDialog(MaterialAlertDialogBuilder builder) {
        builder.setTitle(R.string.pick_store)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });
    }

    @Override
    protected void onBindView(DialogCommonPickerBinding binding) {
        binding.descriptionContentTv.setText(R.string.pick_store_desc);

        BaseEntityAdapter<Store> storeAdapter = new BaseEntityAdapter<>(getActivity());
        storeAdapter.setEnableFooter(true);
        storeAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, item, position);
                }
            }
        });
        storeAdapter.setOnFooterClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                StoreAddFullDialog.newInstance().show(getParentFragmentManager(), StoreAddFullDialog.TAG);
            }
        });
        binding.contentRecyclerView.setAdapter(storeAdapter);

        storeViewModel.getAllStores().observe(getActivity(), new Observer<List<Store>>() {
            @Override
            public void onChanged(List<Store> stores) {
                storeAdapter.updateItems(stores);
            }
        });
    }


}
