package com.vividbobo.easy.ui.others;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.vividbobo.easy.adapter.adapter.DropdownMenuAdapter;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.Config;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.databinding.DialogAutoBillingSettingsBinding;
import com.vividbobo.easy.ui.common.BaseFullScreenMaterialDialog;
import com.vividbobo.easy.utils.AccessibilityUtils;
import com.vividbobo.easy.utils.ResourceUtils;
import com.vividbobo.easy.viewmodel.AccountSettingsViewModel;

import java.util.List;
import java.util.Objects;

public class AutoBillingSettingsDialog extends BaseFullScreenMaterialDialog<DialogAutoBillingSettingsBinding> {
    public static final String TAG = "AccountSettingsDialog";
    private static final int REQUEST_ACTION_ACCESSIBILITY = 0x001;

    AccountSettingsViewModel accountSettingsViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accountSettingsViewModel = new ViewModelProvider(this).get(AccountSettingsViewModel.class);
    }

    public static AutoBillingSettingsDialog newInstance() {

        Bundle args = new Bundle();

        AutoBillingSettingsDialog fragment = new AutoBillingSettingsDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected DialogAutoBillingSettingsBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return DialogAutoBillingSettingsBinding.inflate(inflater);
    }

    @Override
    protected void onViewBinding(DialogAutoBillingSettingsBinding binding) {
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


        binding.enableServiceSw.setChecked(AccessibilityUtils.isAccessibilitySettingsOn(getContext()));
        binding.enableServiceSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!AccessibilityUtils.isAccessibilitySettingsOn(getContext())) {
                        Toast.makeText(getContext(), "请手动开启无障碍服务", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivityForResult(intent, REQUEST_ACTION_ACCESSIBILITY);
                    }
                } else {
                    if (AccessibilityUtils.isAccessibilitySettingsOn(getContext())) {
                        Toast.makeText(getContext(), "请手动关闭无障碍服务", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivityForResult(intent, REQUEST_ACTION_ACCESSIBILITY);
                    }
                }
            }
        });


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
