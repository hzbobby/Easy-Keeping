package com.vividbobo.easy.database.model;

import java.io.Serializable;

public class AccountItem implements Serializable {
    //icon
    //title
    private String icon;
    private String title;
    private String desc;
    private int balance = 0;

    public AccountItem() {
    }

    public AccountItem(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFormatBalance() {
        return String.format("%.2f", (float) balance / 100);
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "AccountItem{" +
                ", title='" + title + '\'' +
                ", desc='" + desc +
                '}';
    }
}
