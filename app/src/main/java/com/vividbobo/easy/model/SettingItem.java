package com.vividbobo.easy.model;

public class SettingItem {
    private Long id;
    private String title;
    private String targetClass;

    public SettingItem(String title, String targetClass) {
        this.title = title;
        this.targetClass = targetClass;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTargetClass() {
        return targetClass;
    }
}
