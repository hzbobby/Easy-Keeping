package com.vividbobo.easy.database.model;

import java.util.Date;

/**
 * 每日总账单记录
 */
public class DayBillPresent {
    private String date="";
    private String week="";
    private long incomeAmount;
    private long expenditureAmount;


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
