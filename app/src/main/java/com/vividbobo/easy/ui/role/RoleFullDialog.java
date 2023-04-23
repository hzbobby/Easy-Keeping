package com.vividbobo.easy.ui.role;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.vividbobo.easy.adapter.adapter.BaseEntityAdapter;
import com.vividbobo.easy.database.model.Role;
import com.vividbobo.easy.databinding.DialogBaseEntityBinding;
import com.vividbobo.easy.ui.common.BaseEntityFullDialog;
import com.vividbobo.easy.ui.common.ContextOperationMenuDialog;
import com.vividbobo.easy.ui.others.OnItemLongClickListener;
import com.vividbobo.easy.viewmodel.RoleViewModel;

import java.util.List;

public class RoleFullDialog extends BaseEntityFullDialog<Role> {
    public static final String TAG = "RoleFullDialog";

    private RoleViewModel roleViewModel;
    private ContextOperationMenuDialog<Role> operationDialog;

    public static RoleFullDialog newInstance() {
        Bundle args = new Bundle();
        RoleFullDialog fragment = new RoleFullDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roleViewModel = new ViewModelProvider(getActivity()).get(RoleViewModel.class);
        operationDialog = new ContextOperationMenuDialog<>();
    }

    @Override
    public void menuAddClick() {
        //add
        RoleAddFullFullDialog.newInstance().show(getParentFragmentManager(), RoleAddFullFullDialog.TAG);
    }

    @Override
    public void config(DialogBaseEntityBinding binding, BaseEntityAdapter<Role> adapter) {
        roleViewModel.getRoleList().observe(getViewLifecycleOwner(), new Observer<List<Role>>() {
            @Override
            public void onChanged(List<Role> roles) {
                adapter.updateItems(roles);
            }
        });
        adapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Object item, int position) {
                Role role = (Role) item;
                operationDialog.show(getParentFragmentManager(), role);
            }
        });

        operationDialog.setOnDetailClickListener(new ContextOperationMenuDialog.OnOperationMenuItemClickListener<Role>() {
            @Override
            public void onMenuItemClick(Role item) {

            }
        });
        operationDialog.setOnEditClickListener(new ContextOperationMenuDialog.OnOperationMenuItemClickListener<Role>() {
            @Override
            public void onMenuItemClick(Role item) {
                RoleAddFullFullDialog.newInstance(item).show(getParentFragmentManager(), RoleAddFullFullDialog.TAG);
            }
        });
        operationDialog.setOnDeleteClickListener(new ContextOperationMenuDialog.OnOperationMenuItemClickListener<Role>() {
            @Override
            public void onMenuItemClick(Role item) {
                roleViewModel.delete(item);
            }
        });
    }

}
