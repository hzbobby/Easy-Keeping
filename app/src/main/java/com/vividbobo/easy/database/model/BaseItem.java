package com.vividbobo.easy.database.model;

import java.io.Serializable;

public class BaseItem implements Serializable {
    protected String title;
    protected String icon;

    public BaseItem() {
    }

    public BaseItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
