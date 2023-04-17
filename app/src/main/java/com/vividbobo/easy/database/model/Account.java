package com.vividbobo.easy.database.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "accounts")
public class Account extends BaseEntity {
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private String currencyCode;    //币种Code
    private Long balance;   //余额
    private List<Integer> legerIds; //适用账本Id
    private String cardNum;     //卡号
    private String remark;      //备注

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

    public List<Integer> getLegerIds() {
        return legerIds;
    }

    public void setLegerIds(List<Integer> legerIds) {
        this.legerIds = legerIds;
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
}
