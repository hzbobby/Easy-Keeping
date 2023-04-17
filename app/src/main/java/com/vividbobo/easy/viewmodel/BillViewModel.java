package com.vividbobo.easy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vividbobo.easy.database.model.Account;
import com.vividbobo.easy.database.model.Bill;
import com.vividbobo.easy.database.model.Category;
import com.vividbobo.easy.database.model.Leger;
import com.vividbobo.easy.database.model.Role;
import com.vividbobo.easy.database.model.Store;
import com.vividbobo.easy.database.model.StoreItem;
import com.vividbobo.easy.database.model.Tag;
import com.vividbobo.easy.repository.BillsRepo;

import java.sql.Date;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/***
 * 用于三个Fragment之间共享数据
 */
public class BillViewModel extends AndroidViewModel {
    //用 viewmodel 共享数据


    //金额
    private final MutableLiveData<Long> amount = new MutableLiveData<>(0L);
    //支出类别
    private final MutableLiveData<Category> categoryExpenditure = new MutableLiveData<>(new Category());
    //收入类别
    private final MutableLiveData<Category> categoryIncome = new MutableLiveData<>(new Category());

    private final MutableLiveData<Leger> leger = new MutableLiveData<>();
    //账单类别
    private final MutableLiveData<Integer> billType = new MutableLiveData<>(Bill.EXPENDITURE);

    //账户
    private final MutableLiveData<Account> srcAccount = new MutableLiveData<>(new Account());
    private final MutableLiveData<Account> tarAccount = new MutableLiveData<>(new Account());
    //备注
    private final MutableLiveData<String> remark = new MutableLiveData<>("");
    //标签
    private final MutableLiveData<List<Tag>> tags = new MutableLiveData<>(new ArrayList<>());
    //日期
    private final MutableLiveData<Date> date = new MutableLiveData<>(new Date(System.currentTimeMillis()));  //或改用long
    //时间
    private final MutableLiveData<LocalTime> time = new MutableLiveData<>(LocalTime.now());
    private final MutableLiveData<Store> store = new MutableLiveData<>();
    private final MutableLiveData<String> currencyCode = new MutableLiveData<>();
    private final MutableLiveData<Role> role = new MutableLiveData<>();

    private final LiveData<Leger> selectedLeger;
    private final LiveData<Role> selectedRole;
    private final LiveData<Account> selectedAccount;


    private BillsRepo billsRepo;

    public BillViewModel(@NonNull Application application) {
        super(application);
        billsRepo = new BillsRepo(application);
        selectedLeger = billsRepo.getSelectedLeger();
        selectedRole = billsRepo.getSelectedRole();
        selectedAccount = billsRepo.getSelectedAccount();

    }
//    private final MutableLiveData<LegerItem> leger=new MutableLiveData<LegerItem>();  //账本是否在此处共享？

    public void setRole(Role role) {
        this.role.setValue(role);
    }

    public LiveData<Role> getRole() {
        return role;
    }

    public void setStore(Store store) {
        this.store.setValue(store);
    }

    public LiveData<Store> getStore() {
        return store;
    }

    public LiveData<Account> getSelectedAccount() {
        return selectedAccount;
    }

    public LiveData<Role> getSelectedRole() {
        return selectedRole;
    }

    public LiveData<Leger> getSelectedLeger() {
        return selectedLeger;
    }

    public void setAmount(Long aLong) {
        amount.setValue(aLong);
    }

    public void setCategoryExpenditure(Category category) {
        categoryExpenditure.setValue(category);
    }

    public void setCategoryIncome(Category category) {
        categoryIncome.setValue(category);
    }

    public void setBillType(int type) {
        billType.setValue(type);
    }

    public void setSrcAccount(Account account) {
        srcAccount.setValue(account);
    }

    public void setTarAccount(Account account) {
        tarAccount.setValue(account);
    }

    public void setRemark(String remarkStr) {
        remark.setValue(remarkStr);
    }

    public void setTags(List<Tag> tagList) {
        tags.setValue(tagList);
    }

    public void setDate(Date date) {
        this.date.setValue(date);
    }

    public void setTime(LocalTime time) {
        this.time.setValue(time);
    }

    public LiveData<Long> getAmount() {
        return amount;
    }

    public LiveData<Category> getCategoryExpenditure() {
        return categoryExpenditure;
    }

    public LiveData<Category> getCategoryIncome() {
        return categoryIncome;
    }

    public LiveData<Integer> getBillType() {
        return billType;
    }

    public LiveData<Account> getSrcAccount() {
        return srcAccount;
    }

    public LiveData<Account> getTarAccount() {
        return tarAccount;
    }

    public LiveData<String> getRemark() {
        return remark;
    }

    public LiveData<List<Tag>> getTags() {
        return tags;
    }

    public LiveData<Date> getDate() {
        return date;
    }

    public LiveData<LocalTime> getTime() {
        return time;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode.setValue(currencyCode);
    }

    public LiveData<String> getCurrencyCode() {
        return currencyCode;
    }
}