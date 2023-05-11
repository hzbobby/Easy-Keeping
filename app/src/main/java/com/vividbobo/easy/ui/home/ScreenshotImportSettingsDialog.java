package com.vividbobo.easy.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.vividbobo.easy.adapter.adapter.DropdownMenuAdapter;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.databinding.DialogAccountSettingsBinding;
import com.vividbobo.easy.ui.common.BaseFullScreenMaterialDialog;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.utils.ResourceUtils;
import com.vividbobo.easy.viewmodel.ScreenshotImportViewModel;

import java.util.List;
import java.util.Objects;

public class ScreenshotImportSettingsDialog extends BaseFullScreenMaterialDialog<DialogAccountSettingsBinding> {
    public static final String TAG = "ScreenshotImportSetting";
    private ScreenshotImportViewModel screenshotImportViewModel;

    public static ScreenshotImportSettingsDialog newInstance() {

        Bundle args = new Bundle();

        ScreenshotImportSettingsDialog fragment = new ScreenshotImportSettingsDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenshotImportViewModel = new ViewModelProvider(getActivity()).get(ScreenshotImportViewModel.class);
    }

    @Override
    protected DialogAccountSettingsBinding getViewBinding(@NonNull LayoutInflater inflater) {
        return DialogAccountSettingsBinding.inflate(inflater);
    }

    @Override
    protected void onViewBinding(DialogAccountSettingsBinding binding) {
        binding.appBarLayout.layoutToolBarTitleTv.setText("截图导入设置");
        binding.appBarLayout.layoutToolBar.setNavigationOnClickListener(v -> dismiss());
        DropdownMenuAdapter<Account> wechatAdapter = new DropdownMenuAdapter<Account>(getActivity());
        wechatAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                Account account = (Account) item;

                screenshotImportViewModel.updateSelectedWechatId(account.getId());
            }
        });

        DropdownMenuAdapter<Account> alipayAdapter = new DropdownMenuAdapter<Account>(getActivity());
        alipayAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                Account account = (Account) item;
                screenshotImportViewModel.updateSelectedAlipayId(account.getId());
            }
        });

        binding.wechatAccountTv.setAdapter(wechatAdapter);
        binding.alipayAccountTv.setAdapter(alipayAdapter);

        screenshotImportViewModel.getAllAccounts().observe(getActivity(), new Observer<List<Account>>() {
            @Override
            public void onChanged(List<Account> accountList) {
                if (Objects.nonNull(accountList)) {
                    wechatAdapter.setItems(accountList);
                    alipayAdapter.setItems(accountList);
                }
            }
        });

        screenshotImportViewModel.getWechatAccount().observe(getActivity(), new Observer<Account>() {
            @Override
            public void onChanged(Account account) {
                if (Objects.nonNull(account)) {

                    binding.wechatAccountTil.getEditText().setText(account.getTitle());
                    binding.wechatAccountTil.setStartIconDrawable(ResourceUtils.getDrawable(account.getIconResName()));
                }
            }
        });
        screenshotImportViewModel.getAlipayAccount().observe(getActivity(), new Observer<Account>() {
            @Override
            public void onChanged(Account account) {
                if (Objects.nonNull(account)) {
                    binding.alipayAccountTil.getEditText().setText(account.getTitle());
                    binding.alipayAccountTil.setStartIconDrawable(ResourceUtils.getDrawable(account.getIconResName()));
                }
            }
        });


    }

}
