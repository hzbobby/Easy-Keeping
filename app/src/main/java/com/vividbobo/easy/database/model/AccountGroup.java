package com.vividbobo.easy.database.model;

import java.util.ArrayList;
import java.util.List;

public class AccountGroup {
    private String title;
    private List<Account> children;

    public AccountGroup(String title) {
        this.title = title;
        children = new ArrayList<>();
    }

    public AccountGroup() {
        children = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Account> getChildren() {
        return children;
    }

    public void addChild(Account account) {
        children.add(account);
    }

}
