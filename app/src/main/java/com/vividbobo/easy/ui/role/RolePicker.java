package com.vividbobo.easy.ui.role;

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
import com.vividbobo.easy.database.model.Role;
import com.vividbobo.easy.databinding.DialogCommonPickerBinding;
import com.vividbobo.easy.ui.common.BaseMaterialDialog;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.viewmodel.RoleViewModel;

import java.util.List;

public class RolePicker extends BaseMaterialDialog<DialogCommonPickerBinding> {
    public static final String TAG = "RolePicker";
    private OnItemClickListener onItemClickListener;
    private RoleViewModel roleViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roleViewModel = new ViewModelProvider(getActivity()).get(RoleViewModel.class);
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
        builder.setTitle(R.string.pick_role)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });
    }

    @Override
    protected void onBindView(DialogCommonPickerBinding binding) {
        binding.descriptionContentTv.setText(R.string.pick_role_desc);

        BaseEntityAdapter<Role> roleAdapter = new BaseEntityAdapter<>(getActivity());
        roleAdapter.setEnableFooter(true);
        roleAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, item, position);
                }
            }
        });
        roleAdapter.setOnFooterClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                RoleAddFullFullDialog.newInstance().show(getParentFragmentManager(), RoleAddFullFullDialog.TAG);
            }
        });
        binding.contentRecyclerView.setAdapter(roleAdapter);

        roleViewModel.getRoleList().observe(getActivity(), new Observer<List<Role>>() {
            @Override
            public void onChanged(List<Role> roles) {
                roleAdapter.updateItems(roles);
            }
        });
    }


}
