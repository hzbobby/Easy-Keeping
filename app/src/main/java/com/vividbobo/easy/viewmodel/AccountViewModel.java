package com.vividbobo.easy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vividbobo.easy.adapter.adapter.ExpandableAdapter;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.AccountType;
import com.vividbobo.easy.database.model.AccountsInfo;
import com.vividbobo.easy.repository.AccountsRepo;

import java.util.List;

public class AccountViewModel extends AndroidViewModel {
    private final AccountsRepo accountsRepo;

    private final LiveData<List<AccountType>> allAccountTypesLD;
    private final LiveData<List<ExpandableAdapter.ExpandableItem<Account>>> expandableAccountsLD;
    private final LiveData<AccountsInfo> accountInfoLD;
    private final LiveData<List<Account>> allAccountsLD;

    public AccountViewModel(@NonNull Application application) {
        super(application);
        accountsRepo = new AccountsRepo(application);

        allAccountTypesLD = accountsRepo.getAllAccountTypesLD();
        expandableAccountsLD = accountsRepo.getExpandableAccountsLD();
        allAccountsLD = accountsRepo.getAllAccountsLD();
        accountInfoLD = accountsRepo.getAccountsInfoLD();
    }

    public LiveData<List<Account>> getAllAccountsLD() {
        return allAccountsLD;
    }

    public LiveData<AccountsInfo> getAccountInfoLD() {
        return accountInfoLD;
    }

    public LiveData<List<AccountType>> getAllAccountTypesLD() {
        return allAccountTypesLD;
    }


    public LiveData<List<ExpandableAdapter.ExpandableItem<Account>>> getExpandableAccountsLD() {
        return expandableAccountsLD;
    }

    public void insert(Account account) {
        accountsRepo.insert(account);
    }

    public void update(Account srcAccount) {
        accountsRepo.update(srcAccount);
    }

    public void delete(Account item) {
        accountsRepo.delete(item);
    }
}