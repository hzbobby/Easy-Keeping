package com.vividbobo.easy.database.model;

public class RoleItem {
    private String name;
    private String icon;

    public RoleItem() {
    }

    public RoleItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
