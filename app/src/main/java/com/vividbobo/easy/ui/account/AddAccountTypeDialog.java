package com.vividbobo.easy.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.vividbobo.easy.R;
import com.vividbobo.easy.databinding.DialogAddAccountTypeBinding;
import com.vividbobo.easy.database.model.AccountType;
import com.vividbobo.easy.ui.others.FullScreenDialog;

@Deprecated
public class AddAccountTypeDialog extends FullScreenDialog {
    public static final String TAG = "AddAccountType";
    private static final String KEY_ARG1 = "accountTypeItem1";

    public static AddAccountTypeDialog newInstance(AccountType accountType) {

        Bundle args = new Bundle();
        if (accountType != null) {
            args.putSerializable(KEY_ARG1, accountType);
        }
        AddAccountTypeDialog fragment = new AddAccountTypeDialog();
        fragment.setArguments(args);
        return fragment;
    }

    DialogAddAccountTypeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogAddAccountTypeBinding.inflate(getLayoutInflater());

        //edit init
        assert getArguments() != null;
        if (getArguments().getSerializable(KEY_ARG1) != null) {
            AccountType accountType = (AccountType) getArguments().getSerializable(KEY_ARG1);
            binding.addAccountTypeTitle.getEditText().setText(accountType.getTitle());
            binding.addAccountTypeDesc.getEditText().setText(accountType.getDesc());
            binding.addAccountTypeDeleteBtn.setVisibility(View.VISIBLE);
        } else {
            binding.addAccountTypeDeleteBtn.setVisibility(View.GONE);
        }


        binding.appBarLayout.layoutToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        binding.appBarLayout.layoutToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.confirm:

                        return true;
                    default:
                        return false;
                }
            }
        });

        binding.addAccountTypeSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        return binding.getRoot();
    }
}
