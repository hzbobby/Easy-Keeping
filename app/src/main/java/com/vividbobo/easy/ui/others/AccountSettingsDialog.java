package com.vividbobo.easy.ui.others;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.vividbobo.easy.adapter.adapter.DropdownMenuAdapter;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.Config;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.databinding.DialogAccountSettingsBinding;
import com.vividbobo.easy.ui.common.BaseFullScreenMaterialDialog;
import com.vividbobo.easy.utils.ResourceUtils;
import com.vividbobo.easy.viewmodel.AccountSettingsViewModel;

import java.util.List;
import java.util.Objects;

public class AccountSettingsDialog extends BaseFullScreenMaterialDialog<DialogAccountSettingsBinding> {
    public static final String TAG = "AccountSettingsDialog";

    AccountSettingsViewModel accountSettingsViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accountSettingsViewModel = new ViewModelProvider(this).get(AccountSettingsViewModel.class);
    }

    public static AccountSettingsDialog newInstance() {

        Bundle args = new Bundle();

        AccountSettingsDialog fragment = new AccountSettingsDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected DialogAccountSettingsBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return DialogAccountSettingsBinding.inflate(inflater);
    }

    @Override
    protected void onViewBinding(DialogAccountSettingsBinding binding) {
        binding.appBarLayout.layoutToolBarTitleTv.setText("自动记账设置");
        binding.appBarLayout.layoutToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        DropdownMenuAdapter<Account> wechatAdapter = new DropdownMenuAdapter<>(getContext());
        DropdownMenuAdapter<Account> alipayAdapter = new DropdownMenuAdapter<>(getContext());
        DropdownMenuAdapter<Leger> legerAdapter = new DropdownMenuAdapter<>(getContext());
        binding.wechatAccountTv.setAdapter(wechatAdapter);
        binding.alipayAccountTv.setAdapter(alipayAdapter);
        binding.legerTv.setAdapter(legerAdapter);

        wechatAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                Account account = (Account) item;
                accountSettingsViewModel.updateSelectedWechatId(account.getId());
            }
        });
        alipayAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                Account account = (Account) item;
                accountSettingsViewModel.updateSelectedAlipayId(account.getId());
            }
        });
        legerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                Leger leger = (Leger) item;
                accountSettingsViewModel.updateSelectedId(Config.TYPE_AUTO_BILLING_LEGER, leger.getId());
            }
        });

        accountSettingsViewModel.getAllAccounts().observe(this, new Observer<List<Account>>() {
            @Override
            public void onChanged(List<Account> accountList) {
                wechatAdapter.setItems(accountList);
                alipayAdapter.setItems(accountList);
            }
        });
        accountSettingsViewModel.getAllLegers().observe(this, new Observer<List<Leger>>() {
            @Override
            public void onChanged(List<Leger> legers) {
                legerAdapter.setItems(legers);
            }
        });
        accountSettingsViewModel.getAlipayAccount().observe(this, new Observer<Account>() {
            @Override
            public void onChanged(Account account) {
                if (Objects.nonNull(account)) {

                    binding.alipayAccountTil.getEditText().setText(account.getTitle());
                    binding.alipayAccountTil.setStartIconDrawable(ResourceUtils.getDrawable(account.getIconResName()));
                }
            }
        });
        accountSettingsViewModel.getWechatAccount().observe(this, new Observer<Account>() {
            @Override
            public void onChanged(Account account) {
                if (Objects.nonNull(account)) {
                    binding.wechatAccountTil.getEditText().setText(account.getTitle());
                    binding.wechatAccountTil.setStartIconDrawable(ResourceUtils.getDrawable(account.getIconResName()));
                }
            }
        });
        accountSettingsViewModel.getLeger().observe(this, new Observer<Leger>() {
            @Override
            public void onChanged(Leger leger) {
                if (Objects.nonNull(leger)) {
                    binding.legerTil.getEditText().setText(leger.getTitle());
                }
            }
        });
    }
}
