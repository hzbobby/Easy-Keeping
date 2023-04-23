package com.vividbobo.easy.database.model;

import java.sql.Date;
import java.util.List;

/**
 * 主界面账单展示模型 支出账单、收入账单、转账账单
 */
@Deprecated
public class BillPresent extends BaseEntity {
    public static final int TYPE_EXPENDITURE = 100;
    public static final int TYPE_INCOME = 101;
    public static final int TYPE_TRANSFER = 102;

    private Long id;    // the id in database
    private Date date;
    private long amount;  //金额
    private String srcAccount; //原账户
    private String tarAccount;  //目标账户
    private List<Tag> tags;

    private int billType;       //Expenditure; Income; Transfer

    public BillPresent(Bill bill) {
        this.setTitle(bill.getCategoryTitle());
        this.setDesc(bill.getRemark());
        this.setIconResName(bill.getCategoryIconResName());


        this.id = bill.getId();
        this.date = bill.getDate();
        this.amount = bill.getAmount();
        this.srcAccount = bill.getAccountTitle();
        if (bill.getTarAccountTitle() != null) this.tarAccount = bill.getTarAccountTitle();
        if (bill.getTags() != null) this.tags = bill.getTags();
        this.billType = bill.getBillType();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getSrcAccount() {
        return srcAccount;
    }

    public void setSrcAccount(String srcAccount) {
        this.srcAccount = srcAccount;
    }

    public String getTarAccount() {
        return tarAccount;
    }

    public void setTarAccount(String tarAccount) {
        this.tarAccount = tarAccount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public int getBillType() {
        return billType;
    }

    public void setBillType(int billType) {
        this.billType = billType;
    }
}
