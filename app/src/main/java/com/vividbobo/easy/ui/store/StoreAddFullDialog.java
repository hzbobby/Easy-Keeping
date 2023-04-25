package com.vividbobo.easy.ui.store;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.vividbobo.easy.database.model.Payee;
import com.vividbobo.easy.ui.common.BaseEntityAddFullDialog;
import com.vividbobo.easy.viewmodel.PayeeViewModel;

public class StoreAddFullDialog extends BaseEntityAddFullDialog<Payee> {
    public static final String TAG = "AddRoleDialog";

    private PayeeViewModel payeeViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payeeViewModel = new ViewModelProvider(getActivity()).get(PayeeViewModel.class);
    }

    public static StoreAddFullDialog newInstance(Payee payee) {

        Bundle args = new Bundle();
        args.putSerializable(KEY_DATA, payee);
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
        Payee payee = new Payee();
        payee.setTitle(title);
        payee.setDesc(desc);
        payeeViewModel.insert(payee);
    }

    @Override
    public void update(Payee entity) {
        payeeViewModel.update(entity);
    }
}
