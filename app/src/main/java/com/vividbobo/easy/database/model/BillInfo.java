package com.vividbobo.easy.database.model;

import androidx.room.Ignore;

import org.checkerframework.framework.qual.IgnoreInWholeProgramInference;

/**
 * 每日总账单记录
 */
public class BillInfo {
    @Ignore
    private String date = "";
    @Ignore
    private String week = "";
    private long incomeAmount;
    private long expenditureAmount;

    @Ignore
    private long balanceAmount;   //结余

    public long getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(long balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public long getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(long incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public long getExpenditureAmount() {
        return expenditureAmount;
    }

    public void setExpenditureAmount(long expenditureAmount) {
        this.expenditureAmount = expenditureAmount;
    }
}
