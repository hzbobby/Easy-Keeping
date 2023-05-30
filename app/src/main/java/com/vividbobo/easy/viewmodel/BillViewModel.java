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
    private final BillsRepo billsRepo;
    private final LiveData<Category> chosenExpCategory;
    private final LiveData<Category> chosenInCategory;
    private final LiveData<Leger> chosenLeger;
    private final LiveData<Account> lastSelectedAccount;
    private final LiveData<Leger> selectedLeger;

    private MutableLiveData<List<Uri>> imageUris = new MutableLiveData<>(new ArrayList<>());
//
//
//    //用 viewmodel 共享数据
//
//    //金额
//    private final MutableLiveData<Long> amount = new MutableLiveData<>(0L);
//    //支出类别
//    private final MutableLiveData<Category> categoryExpenditure = new MutableLiveData<>(new Category("其他", "category_othe_others"));
//    //收入类别
//    private final MutableLiveData<Category> categoryIncome = new MutableLiveData<>(new Category("其他", "category_othe_others"));
//
//    //账单类别
//    private final MutableLiveData<Integer> billType = new MutableLiveData<>(Bill.EXPENDITURE);
//
//    //账户
//    private Account tarAccount = null;
//    private Account srcAccount = null;
//
//    //备注
//    private final MutableLiveData<String> remark = new MutableLiveData<>("");
//    //标签
//    private final MutableLiveData<List<Tag>> tags = new MutableLiveData<>(new ArrayList<>());
//    //日期
//    private final MutableLiveData<Date> date = new MutableLiveData<>(new Date(System.currentTimeMillis()));  //或改用long
//    //时间
//    private final MutableLiveData<LocalTime> time = new MutableLiveData<>(LocalTime.now());
//    private Payee payee = null;
//    private final MutableLiveData<String> currencyCode = new MutableLiveData<>();
//    private Boolean isIncomeExpenditureIncluded = false;
//    private Boolean isBudgetIncluded = false;
//    private Role role = null;
//
//
//    private BillsRepo billsRepo;
//    private Leger leger;
//    private MutableLiveData<List<String>> imagePaths = new MutableLiveData<>(new ArrayList<>());


    private final MutableLiveData<Date> billDate;
    private final MutableLiveData<LocalTime> billTime;
    private final MutableLiveData<List<Tag>> chosenTags;
    private final MutableLiveData<List<String>> billImages;
    private final MutableLiveData<Boolean> refundChecked;
    private final MutableLiveData<Boolean> reimburseChecked;
    private final MutableLiveData<Boolean> inExpIncludedChecked;
    private final LiveData<Role> chosenRole;
    private final MutableLiveData<String> inputRemark;
    private final MutableLiveData<Long> billAmount;
    private final LiveData<Payee> chosenPayee;
    private final LiveData<Account> chosenTarAccount;
    private final MutableLiveData<Boolean> budgetIncludedChecked;
    private final MutableLiveData<Integer> billType;

    private Account srcAccount;
    private Account tarAccount;


    public BillViewModel(@NonNull Application application) {
        super(application);
        this.billsRepo = new BillsRepo(application);
        chosenExpCategory = billsRepo.getSelectedExpCategory();
        chosenInCategory = billsRepo.getSelectedIncCategory();
        chosenLeger = billsRepo.getFilterLeger();
        chosenRole = billsRepo.getSelectedRole();
        chosenPayee = billsRepo.getSelectedPayee();
        chosenTarAccount = billsRepo.getSelectedTarAccount();
        lastSelectedAccount = billsRepo.getLastSelectedAccount();
        selectedLeger = billsRepo.getSelectedLeger();

        billDate = new MutableLiveData<>(new Date(System.currentTimeMillis()));
        billTime = new MutableLiveData<>(LocalTime.now());
        chosenTags = new MutableLiveData<>();
        billImages = new MutableLiveData<>();
        refundChecked = new MutableLiveData<>(false);
        reimburseChecked = new MutableLiveData<>(false);
        inExpIncludedChecked = new MutableLiveData<>(false);
        budgetIncludedChecked = new MutableLiveData<>(false);
        inputRemark = new MutableLiveData<String>();
        billType = new MutableLiveData<>();
        billAmount = new MutableLiveData<Long>();
    }

    public Account getSrcAccount() {
        return srcAccount;
    }

    public void setSrcAccount(Account srcAccount) {
        this.srcAccount = srcAccount;
    }

    public Account getTarAccount() {
        return tarAccount;
    }

    public void setTarAccount(Account tarAccount) {
        this.tarAccount = tarAccount;
    }

    public LiveData<Leger> getSelectedLeger() {
        return selectedLeger;
    }

    public LiveData<Account> getLastSelectedAccount() {
        return lastSelectedAccount;
    }


    public LiveData<Payee> getChosenPayee() {
        return chosenPayee;
    }

    public LiveData<Leger> getChosenLeger() {
        return chosenLeger;
    }

    public LiveData<Role> getChosenRole() {
        return chosenRole;
    }

    public void setFilterExpCategoryId(Integer id) {
        billsRepo.setFilterExpCategoryId(id);
    }

    public void setFilterIncCategoryId(Integer id) {
        billsRepo.setFilterIncCategoryId(id);
    }

    public void setChosenLegerId(Integer legerId) {
        billsRepo.setFilterLegerId(legerId);
    }

    public void setBillDate(Date date) {
        billDate.postValue(date);
    }

    public LiveData<Date> getBillDate() {
        return billDate;
    }

    public void setBillTime(LocalTime time) {
        billTime.postValue(time);
    }

    public LiveData<LocalTime> getBillTime() {
        return billTime;
    }

    public LiveData<Category> getChosenExpCategory() {
        return chosenExpCategory;
    }

    public LiveData<Category> getChosenInCategory() {
        return chosenInCategory;
    }

    public void setChosenRoleId(Integer roleId) {
        billsRepo.setFilterRoleId(roleId);
    }

    public void setChosenPayeeId(Integer payeeId) {
        billsRepo.setFilterPayeeId(payeeId);
    }

    public void setLastSelectedAccountId(Integer accountId) {
        billsRepo.setLastSelectedAccountId(accountId);
    }

    public void setChosenTags(List<Tag> tags) {
        chosenTags.postValue(tags);
    }

    public void setBillImages(List<String> imagePaths) {
        billImages.postValue(imagePaths);
    }

    public void setRefundChecked(Boolean refund) {
        refundChecked.postValue(refund);
    }

    public void setReimburseChecked(Boolean reimburse) {
        reimburseChecked.postValue(reimburse);
    }

    public void setInExpIncludedChecked(Boolean incomeExpenditureIncluded) {
        inExpIncludedChecked.postValue(incomeExpenditureIncluded);
    }


    public void setBudgetIncludedChecked(Boolean budgetIncluded) {
        budgetIncludedChecked.postValue(budgetIncluded);
    }

    public void setChosenTarAccountId(Integer tarAccountId) {
        billsRepo.setFilterTarAccountId(tarAccountId);
    }

    public void setRemark(String remark) {
        inputRemark.postValue(remark);
    }


    public void setBillType(int type) {
        billType.postValue(type);
    }

    public LiveData<Integer> getBillType() {
        return billType;
    }

    public LiveData<List<Tag>> getChosenTags() {
        return chosenTags;
    }

    public LiveData<List<String>> getBillImages() {
        return billImages;
    }

    public LiveData<Boolean> getRefundChecked() {
        return refundChecked;
    }

    public LiveData<Boolean> getReimburseChecked() {
        return reimburseChecked;
    }

    public LiveData<Boolean> getInExpIncludedChecked() {
        return inExpIncludedChecked;
    }

    public LiveData<String> getInputRemark() {
        return inputRemark;
    }

    public LiveData<Boolean> getBudgetIncludedChecked() {
        return budgetIncludedChecked;
    }


    public void setBillAmount(Long amount) {
        billAmount.postValue(amount);
    }

    public LiveData<Long> getBillAmount() {
        return billAmount;
    }

    public void setImageUris(List<Uri> objects) {
        List<Uri> uris = imageUris.getValue();
        if (uris == null) uris = new ArrayList<>();
        uris.addAll(objects);
        imageUris.postValue(uris);
    }

    public LiveData<List<Uri>> getImageUris() {
        return imageUris;
    }

    public void save(Bill bill) {
        billsRepo.save(bill);
    }
}