package com.vividbobo.easy.database.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity(tableName = "bills")
public class Bill {
    public static final int EXPENDITURE = 0;
    public static final int INCOME = 1;
    public static final int TRANSFER = 2;

    @PrimaryKey(autoGenerate = true)
    private Long id;
    private Long amount; //金额
    private Integer categoryId; //类别ID
    private String categoryTitle;
    private String categoryIconResName;
    private Integer legerId;    //账本ID
    private String legerTitle;
    private Integer roleId;     //角色ID
    private String roleTitle;
    private Integer accountId;  //账户ID
    private String accountTitle;
    private List<Tag> tags;   //标签
    private List<String> imagePaths;    //图片路径
    private String currencyCode;    //此账单的币种
    private Boolean isIncomeExpenditureIncluded;    //记入收支
    private Date date;    //日期
    private LocalTime time;    //时间
    private String remark;      //备注
    private Timestamp lastUpdateTime; //更新时间
    private Integer storeId;    //商家ID
    private String storeTitle;
    private Boolean isRefund;   //退款
    private Boolean isReimburse;    //报销
    private Boolean isBudgetIncluded;   //计入预算

    private Integer tarAccountId;   //转出账户s
    private String tarAccountTitle;   //转出账户

    private Integer billType;

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getCategoryIconResName() {
        return categoryIconResName;
    }

    public void setCategoryIconResName(String categoryIconResName) {
        this.categoryIconResName = categoryIconResName;
    }

    public Integer getLegerId() {
        return legerId;
    }

    public void setLegerId(Integer legerId) {
        this.legerId = legerId;
    }

    public String getLegerTitle() {
        return legerTitle;
    }

    public void setLegerTitle(String legerTitle) {
        this.legerTitle = legerTitle;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleTitle() {
        return roleTitle;
    }

    public void setRoleTitle(String roleTitle) {
        this.roleTitle = roleTitle;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Boolean getIncomeExpenditureIncluded() {
        return isIncomeExpenditureIncluded;
    }

    public void setIncomeExpenditureIncluded(Boolean incomeExpenditureIncluded) {
        isIncomeExpenditureIncluded = incomeExpenditureIncluded;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Timestamp getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Timestamp lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getStoreTitle() {
        return storeTitle;
    }

    public void setStoreTitle(String storeTitle) {
        this.storeTitle = storeTitle;
    }

    public Boolean getRefund() {
        return isRefund;
    }

    public void setRefund(Boolean refund) {
        isRefund = refund;
    }

    public Boolean getReimburse() {
        return isReimburse;
    }

    public void setReimburse(Boolean reimburse) {
        isReimburse = reimburse;
    }

    public Boolean getBudgetIncluded() {
        return isBudgetIncluded;
    }

    public void setBudgetIncluded(Boolean budgetIncluded) {
        isBudgetIncluded = budgetIncluded;
    }

    public Integer getTarAccountId() {
        return tarAccountId;
    }

    public void setTarAccountId(Integer tarAccountId) {
        this.tarAccountId = tarAccountId;
    }

    public String getTarAccountTitle() {
        return tarAccountTitle;
    }

    public void setTarAccountTitle(String tarAccountTitle) {
        this.tarAccountTitle = tarAccountTitle;
    }
}
