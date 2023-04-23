package com.vividbobo.easy.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.vividbobo.easy.adapter.adapter.ExpandableAdapter;
import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.dao.AccountDao;
import com.vividbobo.easy.database.dao.AccountTypeDao;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.AccountType;
import com.vividbobo.easy.database.model.AccountsInfo;
import com.vividbobo.easy.utils.AsyncProcessor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class AccountsRepo {
    //dao
    private final AccountTypeDao accountTypeDao;
    private final AccountDao accountDao;
    private final LiveData<List<AccountType>> allAccountTypesLD;
    private final LiveData<List<Account>> allAccountsLD;
    private final MutableLiveData<AccountsInfo> accountsInfoMLD;

    //livedata...

    public AccountsRepo(Application application) {
        EasyDatabase db = EasyDatabase.getDatabase(application);
        accountTypeDao = db.accountTypeDao();
        accountDao = db.accountDao();

        allAccountTypesLD = accountTypeDao.getAllAccountTypes();
        allAccountsLD = accountDao.getAllAccounts();
        accountsInfoMLD = new MutableLiveData<>(new AccountsInfo());
    }

    public LiveData<AccountsInfo> getAccountsInfoLD() {
        return accountsInfoMLD;
    }

    public LiveData<List<ExpandableAdapter.ExpandableItem<Account>>> getExpandableAccountsLD() {
        return Transformations.map(allAccountsLD, this::convertToAccountGroup);
    }

    public LiveData<List<Account>> getAllAccountsLD() {
        return allAccountsLD;
    }

    private List<ExpandableAdapter.ExpandableItem<Account>> convertToAccountGroup(List<Account> accountList) {
        AccountsInfo accountsInfo = new AccountsInfo();
        Map<String, ExpandableAdapter.ExpandableItem<Account>> groupMap = new HashMap<>();
        for (int i = 0; i < accountList.size(); i++) {
            Account account = accountList.get(i);
            String type = account.getAccountTypeTitle();
            if (type == null || type.isEmpty()) {
                continue;
            }
            accountsInfo.addNetAssert(account.getBalance());
            if (account.getBalance() < 0) {
                accountsInfo.setTotalDebt(account.getBalance());
            } else {
                accountsInfo.setTotalAssert(account.getBalance());
            }
            ExpandableAdapter.ExpandableItem<Account> group = null;
            if (!groupMap.containsKey(type)) {
                //不包含
                group = new ExpandableAdapter.ExpandableItem<>(type);
                group.setOrder(groupMap.size());
                groupMap.put(type, group);
            }
            group = groupMap.get(type);
            group.setIconResName(account.getAccountTypeIconResName());
            group.addChild(account);
        }
        List<ExpandableAdapter.ExpandableItem<Account>> groupList = new ArrayList<>();
        for (ExpandableAdapter.ExpandableItem<Account> expItem : groupMap.values()) {
            groupList.add(expItem);
            if (expItem.getChildren() != null || !expItem.getChildren().isEmpty())
                //每组内排序
                expItem.getChildren().sort(new Comparator<Account>() {
                    @Override
                    public int compare(Account o1, Account o2) {
                        return o1.getId().compareTo(o2.getId());
                    }
                });
        }
        //组间排序
        groupList.sort(new Comparator<ExpandableAdapter.ExpandableItem<Account>>() {
            @Override
            public int compare(ExpandableAdapter.ExpandableItem<Account> o1, ExpandableAdapter.ExpandableItem<Account> o2) {
                return Integer.compare(o2.getOrder(), o1.getOrder());
            }
        });

        //set accounts info
        accountsInfoMLD.postValue(accountsInfo);
        Log.d("TAG", "convertToAccountGroup: ");
        return groupList;
    }

    public LiveData<List<AccountType>> getAllAccountTypesLD() {
        return allAccountTypesLD;
    }

    public void insert(Account account) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                accountDao.insert(account);
                return null;
            }
        });
    }

    public void update(Account account) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                accountDao.update(account);
                return null;
            }
        });
    }
}
