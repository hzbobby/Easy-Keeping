package com.vividbobo.easy.ui.store;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.adapter.BaseEntityAdapter;
import com.vividbobo.easy.database.model.Payee;
import com.vividbobo.easy.databinding.DialogBaseEntityBinding;
import com.vividbobo.easy.ui.common.BaseEntityFullDialog;
import com.vividbobo.easy.ui.common.ContextOperationMenuDialog;
import com.vividbobo.easy.ui.others.OnItemLongClickListener;
import com.vividbobo.easy.ui.role.RoleAddFullFullDialog;
import com.vividbobo.easy.viewmodel.PayeeViewModel;

import java.util.List;

public class StoreFullDialog extends BaseEntityFullDialog<Payee> {
    public static final String TAG = "StoreFullDialog";
    private PayeeViewModel payeeViewModel;
    private ContextOperationMenuDialog<Payee> operationDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payeeViewModel = new ViewModelProvider(getActivity()).get(PayeeViewModel.class);
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
    public void config(DialogBaseEntityBinding binding, BaseEntityAdapter<Payee> adapter) {
        binding.appBarLayout.layoutToolBarTitleTv.setText(R.string.store_manage);

        adapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Object item, int position) {
                operationDialog.show(getParentFragmentManager(), (Payee) item);
            }
        });
        payeeViewModel.getAllStores().observe(getViewLifecycleOwner(), new Observer<List<Payee>>() {
            @Override
            public void onChanged(List<Payee> payees) {
                adapter.updateItems(payees);
            }
        });

        operationDialog.setOnDetailClickListener(new ContextOperationMenuDialog.OnOperationMenuItemClickListener<Payee>() {
            @Override
            public void onMenuItemClick(Payee item) {

            }
        });
        operationDialog.setOnEditClickListener(new ContextOperationMenuDialog.OnOperationMenuItemClickListener<Payee>() {
            @Override
            public void onMenuItemClick(Payee item) {
                StoreAddFullDialog.newInstance(item).show(getParentFragmentManager(), RoleAddFullFullDialog.TAG);
            }
        });
        operationDialog.setOnDeleteClickListener(new ContextOperationMenuDialog.OnOperationMenuItemClickListener<Payee>() {
            @Override
            public void onMenuItemClick(Payee item) {
                payeeViewModel.delete(item);
            }
        });
    }
}
