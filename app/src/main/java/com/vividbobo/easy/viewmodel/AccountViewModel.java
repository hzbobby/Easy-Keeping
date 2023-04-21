package com.vividbobo.easy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vividbobo.easy.adapter.ExpandableAdapter;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.AccountGroup;
import com.vividbobo.easy.database.model.AccountType;
import com.vividbobo.easy.repository.AccountsRepo;

import java.util.List;

public class AccountViewModel extends AndroidViewModel {
    private final AccountsRepo accountsRepo;

    private final LiveData<List<AccountType>> allAccountTypesLD;
    private final LiveData<List<ExpandableAdapter.ExpandableItem<Account>>> allAccountsLD;

    public AccountViewModel(@NonNull Application application) {
        super(application);
        accountsRepo = new AccountsRepo(application);

        allAccountTypesLD = accountsRepo.getAllAccountTypesLD();
        allAccountsLD = accountsRepo.getAllAccountsLD();
    }


    public LiveData<List<AccountType>> getAllAccountTypesLD() {
        return allAccountTypesLD;
    }


    public LiveData<List<ExpandableAdapter.ExpandableItem<Account>>> getAllAccountsLD() {
        return allAccountsLD;
    }

    public void insert(Account account) {
        accountsRepo.insert(account);
    }
}