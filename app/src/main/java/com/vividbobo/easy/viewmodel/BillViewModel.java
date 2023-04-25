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
import com.vividbobo.easy.database.model.Payee;
import com.vividbobo.easy.database.model.Role;
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
    private final MutableLiveData<Category> categoryExpenditure = new MutableLiveData<>(new Category("其他", "category_othe_others"));
    //收入类别
    private final MutableLiveData<Category> categoryIncome = new MutableLiveData<>(new Category("其他", "category_othe_others"));

    //账单类别
    private final MutableLiveData<Integer> billType = new MutableLiveData<>(Bill.EXPENDITURE);

    //账户
    private Account tarAccount = null;
    private Account srcAccount = null;

    //备注
    private final MutableLiveData<String> remark = new MutableLiveData<>("");
    //标签
    private final MutableLiveData<List<Tag>> tags = new MutableLiveData<>(new ArrayList<>());
    //日期
    private final MutableLiveData<Date> date = new MutableLiveData<>(new Date(System.currentTimeMillis()));  //或改用long
    //时间
    private final MutableLiveData<LocalTime> time = new MutableLiveData<>(LocalTime.now());
    private Payee payee = null;
    private final MutableLiveData<String> currencyCode = new MutableLiveData<>();
    private Boolean isIncomeExpenditureIncluded = false;
    private Boolean isBudgetIncluded = false;
    private Role role = null;


    private BillsRepo billsRepo;
    private Leger leger;
    private MutableLiveData<List<String>> imagePaths = new MutableLiveData<>(new ArrayList<>());

    public BillViewModel(@NonNull Application application) {
        super(application);
        billsRepo = new BillsRepo(application);

    }

    public void setFilterLegerId(Integer id) {
        billsRepo.setFilterLegerId(id);
    }

    public LiveData<Leger> getSelectedLeger() {
        return billsRepo.getSelectedLeger();
    }

    public Boolean getIncomeExpenditureIncluded() {
        return isIncomeExpenditureIncluded;
    }

    public void setIncomeExpenditureIncluded(Boolean incomeExpenditureIncluded) {
        isIncomeExpenditureIncluded = incomeExpenditureIncluded;
    }

    public Boolean getBudgetIncluded() {
        return isBudgetIncluded;
    }

    public void setBudgetIncluded(Boolean budgetIncluded) {
        isBudgetIncluded = budgetIncluded;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public void setStore(Payee payee) {
        this.payee = payee;
    }

    public Payee getStore() {
        return payee;
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
        this.srcAccount = account;
    }

    public void setTarAccount(Account account) {
        tarAccount = account;
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

    public Account getSrcAccount() {
        return srcAccount;
    }

    public Account getTarAccount() {
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

    public void setLeger(Leger leger) {
        this.leger = leger;
    }

    public Leger getLeger() {
        return leger;
    }

    public void setImagePaths(List<String> result) {
        this.imagePaths.setValue(result);
    }

    public MutableLiveData<List<String>> getImagePaths() {
        return imagePaths;
    }

    public void insert(Bill bill) {
        billsRepo.insert(bill);
    }

    public void setFilterRoleId(Integer roleId) {
        billsRepo.setFilterRoleId(roleId);
    }

    public LiveData<Role> getSelectedRole() {
        return billsRepo.getSelectedRole();
    }

    public void setFilterSrcAccountId(Integer id) {
        billsRepo.setFilterSrcAccountId(id);
    }

    public LiveData<Account> getSelectedSrcAccount() {
        return billsRepo.getSelectedSrcAccount();
    }

    public void setFilterTarAccountId(Integer tarAccountId) {
        billsRepo.setFilterTarAccountId(tarAccountId);
    }

    public LiveData<Account> getSelectedTarAccount() {
        return billsRepo.getSelectedTarAccount();
    }

    public void setFilterExpCategoryId(Integer categoryId) {
        billsRepo.setFilterExpCategoryId(categoryId);
    }

    public void setFilterIncCategoryId(Integer categoryId) {
        billsRepo.setFilterIncCategoryId(categoryId);
    }

    public LiveData<Category> getSelectedExpCategory() {
        return billsRepo.getSelectedExpCategory();
    }

    public LiveData<Category> getSelectedIncCategory() {
        return billsRepo.getSelectedIncCategory();
    }
}