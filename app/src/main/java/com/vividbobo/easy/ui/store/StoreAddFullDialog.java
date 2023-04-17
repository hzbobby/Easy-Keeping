package com.vividbobo.easy.ui.store;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.vividbobo.easy.database.model.Store;
import com.vividbobo.easy.ui.common.BaseEntityAddFullDialog;
import com.vividbobo.easy.viewmodel.StoreViewModel;

public class StoreAddFullDialog extends BaseEntityAddFullDialog<Store> {
    public static final String TAG = "AddRoleDialog";

    private StoreViewModel storeViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeViewModel = new ViewModelProvider(getActivity()).get(StoreViewModel.class);
    }

    public static StoreAddFullDialog newInstance(Store store) {

        Bundle args = new Bundle();
        args.putSerializable(KEY_DATA, store);
        StoreAddFullDialog fragment = new StoreAddFullDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static StoreAddFullDialog newInstance() {
        Bundle args = new Bundle();

        StoreAddFullDialog fragment = new StoreAddFullDialog();
        fragment.setArguments(args);
        return fragment;
    }

    //viewModel
    @Override
    public String getTitle() {
        return "添加店家";
    }

    @Override
    public void save(String title, String desc) {
        Store store = new Store();
        store.setTitle(title);
        store.setDesc(desc);
        storeViewModel.insert(store);
    }

    @Override
    public void update(Store entity) {
        storeViewModel.update(entity);
    }
}
