package com.vividbobo.easy.database.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "bills")
public class Bill extends ServerBaseEntity implements Serializable {
    public static final int EXPENDITURE = 0;
    public static final int INCOME = 1;
    public static final int TRANSFER = 2;

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private Long amount; //金额

    private Integer billType;

    private Integer categoryId; //类别ID

    private String categoryTitle;

    private String categoryIconResName;

    private Integer legerId;    //账本ID

    private String legerTitle;

    private Integer roleId;     //角色ID

    private String roleTitle;

    private Integer accountId;  //账户ID

    private String accountTitle;

    private String accountIconResName;


    private Integer payeeId;    //收款方ID

    private String payeeTitle;

    private Integer tarAccountId;   //转入账户

    private String tarAccountTitle;   //转入账户

    private String tarAccountIconResName;

    private String currencyCode;    //此账单的币种

    private List<Tag> tags;   //标签
    private List<String> imagePaths;    //图片路径
    private String remark;      //备注
    private Boolean isRefund;   //退款

    private Boolean isReimburse;    //报销

    private Boolean isIncomeExpenditureIncluded;    //记入收支

    private Boolean isBudgetIncluded;   //计入预算

    private Date date;    //日期

    private LocalTime time;    //时间


    @Ignore
    public static Bill create(int billType) {
        Bill bill = new Bill();
        bill.setBillType(billType);
        bill.setRefund(false);
        bill.setTags(null);
        bill.setImagePaths(null);
        bill.setDate(new Date(System.currentTimeMillis()));
        bill.setTime(LocalTime.now());
        bill.setTarAccountId(null);
        bill.setTarAccountTitle(null);
        bill.setTarAccountIconResName(null);
        bill.setUploaded(false);
        bill.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return bill;
    }

    @Ignore
    public static Bill create() {
        Bill bill = new Bill();
        bill.setRefund(false);
        bill.setTags(null);
        bill.setImagePaths(null);
        bill.setDate(new Date(System.currentTimeMillis()));
        bill.setTime(LocalTime.now());
        bill.setTarAccountId(null);
        bill.setTarAccountTitle(null);
        bill.setTarAccountIconResName(null);
        bill.setUploaded(false);
        bill.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return bill;
    }


    public Integer getBillType() {
        return billType;
    }

    public String getAccountIconResName() {
        return accountIconResName;
    }

    public void setAccountIconResName(String accountIconResName) {
        this.accountIconResName = accountIconResName;
    }

    public String getTarAccountIconResName() {
        return tarAccountIconResName;
    }

    public void setTarAccountIconResName(String tarAccountIconResName) {
        this.tarAccountIconResName = tarAccountIconResName;
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

    public float getFloatAmount() {
        return (float) amount / 100;
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

    public Integer getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(Integer payeeId) {
        this.payeeId = payeeId;
    }

    public String getPayeeTitle() {
        return payeeTitle;
    }

    public void setPayeeTitle(String payeeTitle) {
        this.payeeTitle = payeeTitle;
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

    @Override
    public String toString() {
        return "Bill{" + "id=" + id + ", amount=" + amount + ", categoryId=" + categoryId + ", categoryTitle='" + categoryTitle + '\'' + ", categoryIconResName='" + categoryIconResName + '\'' + ", legerId=" + legerId + ", legerTitle='" + legerTitle + '\'' + ", roleId=" + roleId + ", roleTitle='" + roleTitle + '\'' + ", accountId=" + accountId + ", accountTitle='" + accountTitle + '\'' + ", accountIconResName='" + accountIconResName + '\'' + ", tags=" + tags + ", imagePaths=" + imagePaths + ", currencyCode='" + currencyCode + '\'' + ", isIncomeExpenditureIncluded=" + isIncomeExpenditureIncluded + ", date=" + date + ", time=" + time + ", remark='" + remark + '\'' + ", payeeId=" + payeeId + ", payeeTitle='" + payeeTitle + '\'' + ", isRefund=" + isRefund + ", isReimburse=" + isReimburse + ", isBudgetIncluded=" + isBudgetIncluded + ", tarAccountId=" + tarAccountId + ", tarAccountTitle='" + tarAccountTitle + '\'' + ", tarAccountIconResName='" + tarAccountIconResName + '\'' + ", billType=" + billType + '}';
    }

    public static Bill createBill(Integer billType, Long amount, Leger leger) {
        Bill bill = new Bill();
        bill.setBillType(billType);
        bill.setAmount(amount);
        bill.setLeger(leger);
        bill.setCreateTime(new Timestamp(System.currentTimeMillis()));
        bill.initBoolValue();
        return bill;
    }

    public void initBoolValue() {
        setRefund(false);
        setReimburse(false);
        setBudgetIncluded(false);
        setIncomeExpenditureIncluded(false);
        setUploaded(false);
    }

    public void initDateTime() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        setDate(new Date(timestamp.getTime()));
        setTime(LocalTime.now());
        setCreateTime(timestamp);
    }

    public void setRole(Role role) {
        if (Objects.isNull(role)) return;
        setRoleId(role.getId());
        setRoleTitle(role.getTitle());
    }

    public void setCategory(Category category) {
        if (Objects.isNull(category)) return;
        setCategoryId(category.getId());
        setCategoryTitle(category.getTitle());
        setCategoryIconResName(category.getIconResName());
    }

    public void setAccount(Account account) {
        if (Objects.isNull(account)) return;
        setAccountId(account.getId());
        setAccountTitle(account.getTitle());
        setAccountIconResName(account.getIconResName());
        setCurrencyCode(account.getCurrencyCode());
    }

    public void setLeger(Leger leger) {
        if (Objects.isNull(leger)) return;
        setLegerId(leger.getId());
        setLegerTitle(leger.getTitle());
    }

    public void setTarAccount(Account account) {
        if (Objects.isNull(account)) return;
        setTarAccountId(account.getId());
        setTarAccountTitle(account.getTitle());
        setTarAccountIconResName(account.getIconResName());
    }

    public void setPayee(Payee billPayee) {
        if (Objects.isNull(billPayee)) return;
        setPayeeId(billPayee.getId());
        setPayeeTitle(billPayee.getTitle());
    }
}

