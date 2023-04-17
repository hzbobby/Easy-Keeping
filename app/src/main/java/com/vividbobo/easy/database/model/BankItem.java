package com.vividbobo.easy.database.model;

import java.io.Serializable;

public class BankItem implements Serializable {
    private String icon;
    private String title;

    public BankItem() {
    }

    public BankItem(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
