package com.vividbobo.easy.ui.role;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.vividbobo.easy.database.model.Role;
import com.vividbobo.easy.databinding.DialogAddBaseEntityBinding;
import com.vividbobo.easy.ui.common.BaseEntityAddFullDialog;
import com.vividbobo.easy.viewmodel.RoleViewModel;

public class RoleAddFullFullDialog extends BaseEntityAddFullDialog<Role> {
    public static final String TAG = "AddRoleDialog";
    private RoleViewModel roleViewModel;

    public static RoleAddFullFullDialog newInstance() {

        Bundle args = new Bundle();

        RoleAddFullFullDialog fragment = new RoleAddFullFullDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static RoleAddFullFullDialog newInstance(Role role) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_DATA, role);

        RoleAddFullFullDialog fragment = new RoleAddFullFullDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        roleViewModel = new ViewModelProvider(this).get(RoleViewModel.class);
    }

    @Override
    protected void onViewBinding(DialogAddBaseEntityBinding binding) {
        super.onViewBinding(binding);

    }

    @Override
    public String getTitle() {
        return "添加角色";
    }

    @Override
    public void save(String title, String desc) {
        Role role = new Role();
        role.setTitle(title);
        role.setDesc(desc);
        roleViewModel.insert(role);
    }

    @Override
    public void update(Role entity) {
        roleViewModel.update(entity);
    }
}
