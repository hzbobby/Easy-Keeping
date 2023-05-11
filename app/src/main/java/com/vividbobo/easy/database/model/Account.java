package com.vividbobo.easy.database.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.vividbobo.easy.adapter.Itemzable;
import com.vividbobo.easy.adapter.adapter.DropdownMenuAdapter;
import com.vividbobo.easy.adapter.adapter.ExpandableAdapter;

import java.io.Serializable;
import java.sql.Timestamp;

@Entity(tableName = "accounts")
public class Account extends BaseEntity implements Serializable, ExpandableAdapter.ItemExpandableChild, Itemzable, DropdownMenuAdapter.DropdownMenuItem {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String currencyCode;    //币种Code
    private Long balance;   //余额
    private String cardNum;     //卡号
    private String remark;      //备注

    private Integer accountTypeId;
    private String accountTypeTitle;
    private String accountTypeIconResName;


    public Account() {
    }

    @Ignore
    public Account(String title, long balancd, int typeId, String typeTitle, String typeIconResName, String currencyCode, String accountIconResName) {
        setTitle(title);
        setBalance(balancd);
        setAccountTypeId(typeId);
        setAccountTypeTitle(typeTitle);
        setAccountTypeIconResName(typeIconResName);
        setCurrencyCode(currencyCode);
        setIconResName(accountIconResName);
        setCreateTime(new Timestamp(System.currentTimeMillis()));
    }

    public String getFormatBalance() {
        return String.format("%.2f", (float) balance / 100);
    }

    public Integer getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountTypeId(Integer accountTypeId) {
        this.accountTypeId = accountTypeId;
    }

    public String getAccountTypeTitle() {
        return accountTypeTitle;
    }

    public void setAccountTypeTitle(String accountTypeTitle) {
        this.accountTypeTitle = accountTypeTitle;
    }

    public String getAccountTypeIconResName() {
        return accountTypeIconResName;
    }

    public void setAccountTypeIconResName(String accountTypeIconResName) {
        this.accountTypeIconResName = accountTypeIconResName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }


    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String getItemTitle() {
        return getTitle();
    }

    @Override
    public String getItemSubTitle() {
        return getFormatBalance();
    }

    @Override
    public String getItemIconResName() {
        return getIconResName();
    }

    @Override
    public String getItemDesc() {
        return "余额: " + getFormatBalance();
    }

    @Override
    public String toString() {
        return "Account{" +
                super.toString() +
                "id=" + id +
                ", currencyCode='" + currencyCode + '\'' +
                ", balance=" + balance +
                ", cardNum='" + cardNum + '\'' +
                ", remark='" + remark + '\'' +
                ", accountTypeId=" + accountTypeId +
                ", accountTypeTitle='" + accountTypeTitle + '\'' +
                ", accountTypeIconResName='" + accountTypeIconResName + '\'' +
                '}';
    }
}
