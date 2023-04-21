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
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.databinding.ActivityAddAccountBinding;
import com.vividbobo.easy.ui.others.FullScreenDialog;

@Deprecated
public class AddAccountDialog extends FullScreenDialog {
    public static final String TAG = "AddAccountDialog";
    ActivityAddAccountBinding binding;
    private static String ARGS_KEY1 = "accountItem1";

    public static AddAccountDialog newInstance() {

        Bundle args = new Bundle();

        AddAccountDialog fragment = new AddAccountDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static AddAccountDialog newInstance(Account account) {

        Bundle args = new Bundle();
        args.putSerializable(ARGS_KEY1, account);

        AddAccountDialog fragment = new AddAccountDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityAddAccountBinding.inflate(getLayoutInflater());

        Account account = (Account) getArguments().getSerializable(ARGS_KEY1);
        if (account != null) {
            //edit initial
            binding.addAccountTitleTv.getEditText().setText(account.getTitle());
            binding.addAccountBalanceTextTil.getEditText().setText(account.getFormatBalance());
            //...
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

                    return true;
                }
                return false;
            }
        });


        return binding.getRoot();
    }
}
