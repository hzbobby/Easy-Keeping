package com.vividbobo.easy.viewmodel;

import android.app.Application;
import android.net.Uri;

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
    private final MutableLiveData<Category> categoryExpenditure = new MutableLiveData<>(new Category("其他","category_othe_others"));
    //收入类别
    private final MutableLiveData<Category> categoryIncome = new MutableLiveData<>(new Category("其他","category_othe_others"));

    //账单类别
    private final MutableLiveData<Integer> billType = new MutableLiveData<>(Bill.EXPENDITURE);

    //账户
    private Account srcAccount = null;
    private Account tarAccount = null;
    //备注
    private final MutableLiveData<String> remark = new MutableLiveData<>("");
    //标签
    private final MutableLiveData<List<Tag>> tags = new MutableLiveData<>(new ArrayList<>());
    //日期
    private Date date = new Date(System.currentTimeMillis());  //或改用long
    //时间
    private LocalTime time = LocalTime.now();
    private Store store = null;
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
//    private final MutableLiveData<LegerItem> leger=new MutableLiveData<LegerItem>();  //账本是否在此处共享？


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

    public void setStore(Store store) {
        this.store = store;
    }

    public Store getStore() {
        return store;
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
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
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

    public Date getDate() {
        return date;
    }

    public LocalTime getTime() {
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
}