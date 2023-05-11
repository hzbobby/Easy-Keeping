package com.vividbobo.easy.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.vividbobo.easy.database.EasyDatabase;
import com.vividbobo.easy.database.dao.AccountDao;
import com.vividbobo.easy.database.dao.BillDao;
import com.vividbobo.easy.database.dao.CategoryDao;
import com.vividbobo.easy.database.dao.ConfigDao;
import com.vividbobo.easy.database.dao.LegerDao;
import com.vividbobo.easy.database.dao.PayeeDao;
import com.vividbobo.easy.database.dao.RoleDao;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.Config;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.database.model.Payee;
import com.vividbobo.easy.database.model.Role;
import com.vividbobo.easy.utils.AsyncProcessor;

import java.util.Objects;
import java.util.concurrent.Callable;

public class BillsRepo {
    //the DAO
    private final BillDao billDao;
    private final LiveData<Leger> filterLeger;
    private final MutableLiveData<Integer> filterRoleId;
    private final LiveData<Role> selectedRole;
    private final MutableLiveData<Integer> filterExpCategoryId;
    private final LiveData<Category> selectedExpCategory;
    private final MutableLiveData<Integer> filterIncCategoryId;
    private final LiveData<Category> selectedIncCategory;
    private final MutableLiveData<Integer> filterLegerId;
    private final MutableLiveData<Integer> filterPayeeId;
    private final LiveData<Payee> selectedPayee;
    private final MutableLiveData<Integer> filterAccountId;
    private final LiveData<Account> selectedAccount;
    private final MutableLiveData<Integer> filterTarAccountId;
    private final LiveData<Account> selectedTarAccount;
    private final LiveData<Account> lastSelectedAccount;
    private final LiveData<Leger> selectedLeger;
    private final AccountDao accountDao;


    //the LiveData


    public BillsRepo(Application application) {
        EasyDatabase db = EasyDatabase.getDatabase(application);
        this.billDao = db.billDao();
        LegerDao legerDao = db.legerDao();
        RoleDao roleDao = db.roleDao();
        ConfigDao configDao = db.configDao();
        accountDao = db.accountDao();
        CategoryDao categoryDao = db.categoryDao();
        PayeeDao payeeDao = db.payeeDao();

        selectedLeger = configDao.getSelectedLeger();
        lastSelectedAccount = configDao.getSelectedAccount();

        filterTarAccountId = new MutableLiveData<Integer>();
        selectedTarAccount = Transformations.switchMap(filterTarAccountId, tarAccountId -> accountDao.getAccountByIdLD(tarAccountId));


        filterAccountId = new MutableLiveData<>();
        selectedAccount = Transformations.switchMap(filterAccountId, accountId -> accountDao.getAccountByIdLD(accountId));

        filterPayeeId = new MutableLiveData<>();
        selectedPayee = Transformations.switchMap(filterPayeeId, payeeId -> payeeDao.getPayeeById(payeeId));

        filterLegerId = new MutableLiveData<>(configDao.getSelectedIdByType(Config.TYPE_LEGER).getValue());
        filterLeger = Transformations.switchMap(filterLegerId, legerId -> legerDao.getLegerByIdLD(legerId));

        filterRoleId = new MutableLiveData<>(1);
        selectedRole = Transformations.switchMap(filterRoleId, roleId -> roleDao.getRoleByIdLD(roleId));


        filterExpCategoryId = new MutableLiveData<>(1);
        selectedExpCategory = Transformations.switchMap(filterExpCategoryId, categoryId -> categoryDao.getCategoryByIdLd(categoryId));
        filterIncCategoryId = new MutableLiveData<>(24);
        selectedIncCategory = Transformations.switchMap(filterIncCategoryId, categoryId -> categoryDao.getCategoryByIdLd(categoryId));
    }

    public LiveData<Leger> getSelectedLeger() {
        return selectedLeger;
    }

    public LiveData<Account> getSelectedTarAccount() {
        return selectedTarAccount;
    }

    public LiveData<Account> getLastSelectedAccount() {
        return lastSelectedAccount;
    }

    public LiveData<Account> getSelectedAccount() {
        return selectedAccount;
    }

    public LiveData<Payee> getSelectedPayee() {
        return selectedPayee;
    }

    public LiveData<Role> getSelectedRole() {
        return selectedRole;
    }

    public LiveData<Leger> getFilterLeger() {
        return filterLeger;
    }

    public void insert(Bill bill) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                billDao.insert(bill);
                return null;
            }
        });
    }

    public void setFilterRoleId(Integer roleId) {
        this.filterRoleId.setValue(roleId);
    }


    public void setFilterExpCategoryId(Integer categoryId) {
        this.filterExpCategoryId.setValue(categoryId);
    }

    public LiveData<Category> getSelectedExpCategory() {
        return selectedExpCategory;
    }

    public void setFilterIncCategoryId(Integer categoryId) {
        this.filterIncCategoryId.setValue(categoryId);

    }

    public LiveData<Category> getSelectedIncCategory() {
        return selectedIncCategory;
    }

    public void setFilterLegerId(Integer legerId) {
        filterLegerId.postValue(legerId);
    }

    public void setFilterPayeeId(Integer payeeId) {
        filterPayeeId.postValue(payeeId);
    }

    public void setFilterAccountId(Integer accountId) {
        filterAccountId.postValue(accountId);
    }

    public void setFilterTarAccountId(Integer tarAccountId) {

    }

    public void save(Bill bill) {
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                if (Objects.isNull(bill.getId())) {
                    billDao.insert(bill);
                } else {
                    billDao.update(bill);
                }

                return null;
            }
        });
        AsyncProcessor.getInstance().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                if (bill.getBillType() == Bill.TRANSFER) {
                    Account tarAccount = accountDao.getRawAccountById(bill.getTarAccountId());
                    tarAccount.setBalance(tarAccount.getBalance() + bill.getAmount());
                    accountDao.update(tarAccount);
                }
                Account srcAccount = accountDao.getRawAccountById(bill.getAccountId());
                Long balance = bill.getBillType() == Bill.INCOME ? bill.getAmount() : -bill.getAmount();
                srcAccount.setBalance(srcAccount.getBalance() + balance);
                accountDao.update(srcAccount);
                return null;
            }
        });
    }
}
