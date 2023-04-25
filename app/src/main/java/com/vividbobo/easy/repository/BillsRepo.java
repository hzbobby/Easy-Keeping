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
import com.vividbobo.easy.database.dao.RoleDao;
import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.Config;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.database.model.Role;
import com.vividbobo.easy.utils.AsyncProcessor;

import java.util.concurrent.Callable;

public class BillsRepo {
    //the DAO
    private final BillDao billDao;
    private final LegerDao legerDao;
    private final RoleDao roleDao;
    private final AccountDao accountDao;
    private final ConfigDao configDao;
    private final CategoryDao categoryDao;


    private final MutableLiveData<Integer> filterLegerId;
    private final LiveData<Leger> selectedLeger;

    private final MutableLiveData<Integer> filterRoleId;
    private final LiveData<Role> selectedRole;

    private final MutableLiveData<Integer> filterSrcAccountId;
    private final LiveData<Account> selectedSrcAccount;
    private final MutableLiveData<Integer> filterTarAccountId;
    private final LiveData<Account> selectedTarAccount;
    private final MutableLiveData<Integer> filterExpCategoryId;
    private final LiveData<Category> selectedExpCategory;
    private final MutableLiveData<Integer> filterIncCategoryId;
    private final LiveData<Category> selectedIncCategory;


    //the LiveData


    public BillsRepo(Application application) {
        EasyDatabase db = EasyDatabase.getDatabase(application);
        this.billDao = db.billDao();
        this.legerDao = db.legerDao();
        this.roleDao = db.roleDao();
        this.configDao = db.configDao();
        this.accountDao = db.accountDao();
        this.categoryDao = db.categoryDao();

        filterLegerId = new MutableLiveData<>(1);
        selectedLeger = Transformations.switchMap(filterLegerId, legerId -> legerDao.getLegerByIdLD(legerId));
        filterRoleId = new MutableLiveData<>(1);
        selectedRole = Transformations.switchMap(filterRoleId, roleId -> roleDao.getRoleByIdLD(roleId));
        filterSrcAccountId = new MutableLiveData<>(1);      //怎么获取config里选中的ID
        selectedSrcAccount = Transformations.switchMap(filterSrcAccountId, accountId -> accountDao.getAccountByIdLD(accountId));
        filterTarAccountId = new MutableLiveData<>(0);      //没有
        selectedTarAccount = Transformations.switchMap(filterTarAccountId, accountId -> accountDao.getAccountByIdLD(accountId));
        filterExpCategoryId = new MutableLiveData<>(1);
        selectedExpCategory = Transformations.switchMap(filterExpCategoryId, categoryId -> categoryDao.getCategoryByIdLd(categoryId));
        filterIncCategoryId = new MutableLiveData<>(24);
        selectedIncCategory = Transformations.switchMap(filterIncCategoryId, categoryId -> categoryDao.getCategoryByIdLd(categoryId));

    }

    public LiveData<Role> getSelectedRole() {
        return selectedRole;
    }

    public void setFilterLegerId(Integer id) {
        this.filterLegerId.setValue(id);
    }

    public LiveData<Leger> getSelectedLeger() {
        return selectedLeger;
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

    public void setFilterSrcAccountId(Integer id) {
        this.filterSrcAccountId.setValue(id);
    }

    public LiveData<Account> getSelectedSrcAccount() {
        return selectedSrcAccount;
    }

    public void setFilterTarAccountId(Integer tarAccountId) {
        this.filterTarAccountId.setValue(tarAccountId);
    }

    public LiveData<Account> getSelectedTarAccount() {
        return selectedTarAccount;
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
}
