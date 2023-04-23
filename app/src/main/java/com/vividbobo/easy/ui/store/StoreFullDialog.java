package com.vividbobo.easy.ui.store;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.adapter.BaseEntityAdapter;
import com.vividbobo.easy.database.model.Store;
import com.vividbobo.easy.databinding.DialogBaseEntityBinding;
import com.vividbobo.easy.ui.common.BaseEntityFullDialog;
import com.vividbobo.easy.ui.common.ContextOperationMenuDialog;
import com.vividbobo.easy.ui.others.OnItemLongClickListener;
import com.vividbobo.easy.ui.role.RoleAddFullFullDialog;
import com.vividbobo.easy.viewmodel.StoreViewModel;

import java.util.List;

public class StoreFullDialog extends BaseEntityFullDialog<Store> {
    public static final String TAG = "StoreFullDialog";
    private StoreViewModel storeViewModel;
    private ContextOperationMenuDialog<Store> operationDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeViewModel = new ViewModelProvider(getActivity()).get(StoreViewModel.class);
        operationDialog = new ContextOperationMenuDialog<>();
    }

    public static StoreFullDialog newInstance() {
        Bundle args = new Bundle();

        StoreFullDialog fragment = new StoreFullDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void menuAddClick() {
        StoreAddFullDialog.newInstance().show(getParentFragmentManager(), StoreAddFullDialog.TAG);
    }

    @Override
    public void config(DialogBaseEntityBinding binding, BaseEntityAdapter<Store> adapter) {
        binding.appBarLayout.layoutToolBarTitleTv.setText(R.string.store_manage);

        adapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Object item, int position) {
                operationDialog.show(getParentFragmentManager(), (Store) item);
            }
        });
        storeViewModel.getAllStores().observe(getViewLifecycleOwner(), new Observer<List<Store>>() {
            @Override
            public void onChanged(List<Store> stores) {
                adapter.updateItems(stores);
            }
        });

        operationDialog.setOnDetailClickListener(new ContextOperationMenuDialog.OnOperationMenuItemClickListener<Store>() {
            @Override
            public void onMenuItemClick(Store item) {

            }
        });
        operationDialog.setOnEditClickListener(new ContextOperationMenuDialog.OnOperationMenuItemClickListener<Store>() {
            @Override
            public void onMenuItemClick(Store item) {
                StoreAddFullDialog.newInstance(item).show(getParentFragmentManager(), RoleAddFullFullDialog.TAG);
            }
        });
        operationDialog.setOnDeleteClickListener(new ContextOperationMenuDialog.OnOperationMenuItemClickListener<Store>() {
            @Override
            public void onMenuItemClick(Store item) {
                storeViewModel.delete(item);
            }
        });
    }
}
