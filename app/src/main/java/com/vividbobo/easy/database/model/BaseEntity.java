package com.vividbobo.easy.database.model;

import androidx.room.Ignore;

public abstract class BaseEntity {

    private String title;
    private String desc;
    private String iconResName;

    public BaseEntity() {
    }

    public BaseEntity(String title, String desc, String iconResName) {
        this.title = title;
        this.desc = desc;
        this.iconResName = iconResName;
    }

    public String getIconResName() {
        return iconResName;
    }

    public void setIconResName(String iconResName) {
        this.iconResName = iconResName;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
