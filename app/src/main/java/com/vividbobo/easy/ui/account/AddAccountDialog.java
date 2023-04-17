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
import com.vividbobo.easy.databinding.DialogAddAccountBinding;
import com.vividbobo.easy.database.model.AccountItem;
import com.vividbobo.easy.ui.others.FullScreenDialog;

public class AddAccountDialog extends FullScreenDialog {
    public static final String TAG = "AddAccountDialog";
    DialogAddAccountBinding binding;
    private static String ARGS_KEY1 = "accountItem1";

    public static AddAccountDialog newInstance(AccountItem accountItem) {

        Bundle args = new Bundle();
        if (accountItem != null) {
            args.putSerializable(ARGS_KEY1, accountItem);
        }

        AddAccountDialog fragment = new AddAccountDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogAddAccountBinding.inflate(getLayoutInflater());

        assert getArguments() != null;
        if (getArguments().getSerializable(ARGS_KEY1) != null) {
            AccountItem accountItem = (AccountItem) getArguments().getSerializable(ARGS_KEY1);
            //TODO edit
            binding.addAccountTitleTv.getEditText().setText(accountItem.getTitle());
            binding.addAccountBalanceTextTv.setText(accountItem.getFormatBalance());
            binding.addAccountDeleteBtn.setVisibility(View.VISIBLE);
        } else {
            binding.addAccountDeleteBtn.setVisibility(View.GONE);
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
                if (item.getItemId() == R.id.confirm) {
                    //TODO save then dismiss;
                    requireActivity().finish();
                    return true;
                }
                return false;
            }
        });

        binding.addAccountSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO save then dismiss
            }
        });

        return binding.getRoot();
    }
}
