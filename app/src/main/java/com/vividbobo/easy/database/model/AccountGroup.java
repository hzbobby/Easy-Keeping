package com.vividbobo.easy.database.model;

import java.util.ArrayList;
import java.util.List;

public class AccountGroup {
    private String title;
    private List<AccountItem> children;

    public AccountGroup() {
        children = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<AccountItem> getChildren() {
        return children;
    }

    public void setChildren(List<AccountItem> children) {
        this.children = children;
    }
}
