package com.vividbobo.easy.database.model;

import java.io.Serializable;
import java.util.Objects;

public class AccountTypeItem implements Serializable {
    public static final Integer ACCOUNT_NONE = 0;
    public static final Integer ACCOUNT_BANK = 1;

    private String icon;
    private String title, subTitle;

    private Integer childAccountType;    //是否有子类型

    public AccountTypeItem() {
        childAccountType = ACCOUNT_NONE;
    }

    public AccountTypeItem(String title) {
        this();
        this.title = title;
    }

    public AccountTypeItem(String title, String subTitle) {
        this();
        this.title = title;
        this.subTitle = subTitle;
    }

    public boolean isChildAccountEmpty() {
        return getChildAccountType().intValue() == ACCOUNT_NONE;
    }

    public Integer getChildAccountType() {
        return childAccountType;
    }

    public void setChildAccountType(Integer childAccountType) {
        this.childAccountType = childAccountType;
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

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}
