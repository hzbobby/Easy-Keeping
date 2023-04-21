package com.vividbobo.easy.ui.account;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.vividbobo.easy.BaseActivity;
import com.vividbobo.easy.R;
import com.vividbobo.easy.adapter.DropdownMenuAdapter;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.AccountType;
import com.vividbobo.easy.database.model.Currency;
import com.vividbobo.easy.database.model.Resource;
import com.vividbobo.easy.databinding.ActivityAddAccountBinding;
import com.vividbobo.easy.ui.others.OnItemClickListener;
import com.vividbobo.easy.utils.AsyncProcessor;
import com.vividbobo.easy.utils.ResourceUtils;
import com.vividbobo.easy.utils.ToastUtil;
import com.vividbobo.easy.viewmodel.AccountViewModel;
import com.vividbobo.easy.viewmodel.CurrencyViewModel;
import com.vividbobo.easy.viewmodel.ResourceViewModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AccountAddActivity extends BaseActivity {
    public static final String KEY_EDIT_ACCOUNT = "edit_account";
    private ActivityAddAccountBinding binding;

    private AccountViewModel accountViewModel;
    private CurrencyViewModel currencyViewModel;
    private ResourceViewModel resourceViewModel;


    /**
     * 点击暂时保存的对象
     */
    private AccountType selectedAccountType = null;
    private Currency selectedCurrency = null;
    private Resource selectedResource = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init viewModel
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        currencyViewModel = new ViewModelProvider(this).get(CurrencyViewModel.class);
        resourceViewModel = new ViewModelProvider(this).get(ResourceViewModel.class);

        binding.appBarLayout.layoutToolBarTitleTv.setText(R.string.add_account);
        binding.appBarLayout.layoutToolBar.inflateMenu(R.menu.confirm);
        binding.appBarLayout.layoutToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.appBarLayout.layoutToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.confirm) {
                    if (selectedAccountType == null) {
                        ToastUtil.makeToast("请选择账户类型");
                        return false;
                    }
                    if (selectedCurrency == null) {
                        ToastUtil.makeToast("请选择币种");
                        return false;
                    }
                    save();
                    finish();
                    return true;
                }
                return false;
            }
        });

        //edit initial
        Account editAccount = (Account) getIntent().getSerializableExtra(KEY_EDIT_ACCOUNT);
        if (editAccount != null) {
            binding.addAccountTitleTv.getEditText().setText(editAccount.getTitle());
            binding.addAccountTypeTil.getEditText().setText(editAccount.getAccountTypeTitle());
            binding.addAccountTypeTil.setStartIconDrawable(ResourceUtils.getDrawable(editAccount.getAccountTypeIconResName()));
            ResourceUtils.bindImageDrawable(this, ResourceUtils.getDrawable(editAccount.getIconResName())).centerInside().into(binding.addAccountIconIm);
            binding.addAccountCurrencyTil.getEditText().setText(editAccount.getCurrencyCode());
            binding.addAccountBalanceTextTil.getEditText().setText(editAccount.getFormatBalance());
            if (editAccount.getDesc() != null || !editAccount.getDesc().isEmpty()) {
                binding.addAccountDescTv.getEditText().setText(editAccount.getDesc());
            }
            if (editAccount.getCardNum() != null || !editAccount.getCardNum().isEmpty()) {
                binding.addAccountCardTv.getEditText().setText(editAccount.getCardNum());
            }
            if (editAccount.getRemark() != null || !editAccount.getRemark().isEmpty()) {
                binding.addAccountRemarkTv.getEditText().setText(editAccount.getRemark());
            }
        }

        //dropdown
//        binding.addAccountTypeDropdownTil
        DropdownMenuAdapter<AccountType> accountDmAdapter = new DropdownMenuAdapter<AccountType>(this);
        accountDmAdapter.setEnableIcon(true);
        accountDmAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                AccountType accountType = (AccountType) item;
                selectedAccountType = accountType;
                binding.addAccountTypeTil.getEditText().setText(accountType.getItemTitle());
                binding.addAccountTypeTil.setStartIconDrawable(ResourceUtils.getDrawable(accountType.getIconResName()));
            }
        });
        binding.addAccountTypeAtv.setAdapter(accountDmAdapter);
        accountViewModel.getAllAccountTypesLD().observe(this, new Observer<List<AccountType>>() {
            @Override
            public void onChanged(List<AccountType> accountTypes) {
                //set the drop down menu adapter
                accountDmAdapter.setItems(accountTypes);
            }
        });

        // currency drop down menu
        DropdownMenuAdapter<Currency> currencyAdapter = new DropdownMenuAdapter<Currency>(this);
        currencyAdapter.setEnableIcon(true);
        currencyAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                Currency currency = (Currency) item;
                selectedCurrency = currency;
                binding.addAccountCurrencyTil.getEditText().setText(currency.getItemTitle());
            }
        });
        binding.addAccountCurrencyAtv.setAdapter(currencyAdapter);

        currencyViewModel.getEnableCurrencies().observe(this, new Observer<List<Currency>>() {
            @Override
            public void onChanged(List<Currency> currencies) {
                currencyAdapter.setItems(currencies);
            }
        });

        //select account icon
        ResourceBottomSheet<Resource> resourceBottomSheet = new ResourceBottomSheet<Resource>();
        //对bottom进行初始化
        initialAccountBottomSheet(resourceViewModel.getAccountResLF(), resourceBottomSheet);
        binding.addAccountIconCons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resourceBottomSheet.show(getSupportFragmentManager(), ResourceBottomSheet.TAG);
            }
        });


    }

    /**
     * 对 account bottom sheet 进行初始化
     *
     * @param accountResLF
     */
    private void initialAccountBottomSheet(ListenableFuture<List<Resource>> accountResLF, ResourceBottomSheet<Resource> bottomSheet) {
        bottomSheet.setTitle("账户图标");
        bottomSheet.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                Resource resource = (Resource) item;
                selectedResource = resource;
                //setting account icon
                ResourceUtils.bindImageDrawable(binding.getRoot().getContext(), ResourceUtils.getDrawable(resource.getItemIconResName()))
                        .centerInside().into(binding.addAccountIconIm);


                bottomSheet.dismiss();
            }
        });
//        try {
//            //同步设置
//            bottomSheet.setItems(accountResLF.get());
//            Log.d("TAG", "initialAccountBottomSheet: ");
//        } catch (ExecutionException e) {
//            throw new RuntimeException(e);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        Futures.addCallback(accountResLF, new FutureCallback<List<Resource>>() {
            @Override
            public void onSuccess(List<Resource> result) {
                Log.d("TAG", "onSuccess: 子线程setItems");
                Log.d("TAG", "onSuccess: result is empty? " + result.isEmpty());
                bottomSheet.setItems(result);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("TAG", "onFailure: the account bottom initial failure");
            }
        }, AsyncProcessor.getInstance().getExecutorService());
    }

    private void save() {
        Account account = new Account();
        String balanceStr = binding.addAccountBalanceTextTil.getEditText().getText().toString();
        account.setBalance((long) (Double.parseDouble(balanceStr) * 100));
        account.setTitle(binding.addAccountTitleTv.getEditText().getText().toString());
        account.setAccountTypeId(selectedAccountType.getId());
        account.setAccountTypeTitle(selectedAccountType.getTitle());
        account.setAccountTypeIconResName(selectedAccountType.getIconResName());
        account.setCurrencyCode(selectedCurrency.getCode());
        account.setDesc(binding.addAccountDescTv.getEditText().getText().toString());
        account.setCardNum(binding.addAccountCardTv.getEditText().getText().toString());
        account.setRemark(binding.addAccountRemarkTv.getEditText().getText().toString());
        account.setIconResId(selectedResource.getId());
        account.setIconResName(selectedResource.getResName());

        accountViewModel.insert(account);
    }
}
