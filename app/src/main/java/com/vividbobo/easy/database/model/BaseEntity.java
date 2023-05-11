package com.vividbobo.easy.database.model;

import java.io.Serializable;

public abstract class BaseEntity extends ServerBaseEntity implements Serializable {

    private String title;
    private String desc;

    private Integer iconResId;
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

    public Integer getIconResId() {
        return iconResId;
    }

    public void setIconResId(Integer iconResId) {
        this.iconResId = iconResId;
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

    @Override
    public String toString() {
        return "BaseEntity{" +
                "title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", iconResId=" + iconResId +
                ", iconResName='" + iconResName + '\'' +
                '}';
    }
}
