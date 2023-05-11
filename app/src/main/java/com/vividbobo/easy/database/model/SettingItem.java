package com.vividbobo.easy.database.model;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.vividbobo.easy.ui.settings.OnSettingItemClickListener;

public class SettingItem {
    public final static int SETTING_CATEGORY_EXPENDITURE = 2;
    public final static int SETTING_TESTING = 3;
    public final static int SETTING_ROLE= 4;
    public static final int SETTING_STORE = 5;
    public static final int SETTING_CURRENCY = 6;
    public static final int SETTING_TAG = 1;
    public static final int SETTING_CATEGORY_INCOME = 7;


    private Long id;
    private String title;
    private String targetClass;

    private int settingId;

    private Bundle args;

    private OnSettingItemClickListener onClickListener;

    public void onSettingItemClick() {
        if (onClickListener != null) {
            onClickListener.OnClick(settingId);
        }
    }

    public SettingItem(String title, int settingId, OnSettingItemClickListener onClickListener) {
        this.title = title;
        this.settingId = settingId;
        this.onClickListener = onClickListener;
    }

    public void setOnClickListener(OnSettingItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public Integer getSettingId() {
        return settingId;
    }

    public void setSettingId(Integer settingId) {
        this.settingId = settingId;
    }

    public void setArgs(Bundle args) {
        this.args = args;
    }

    public Bundle getArgs() {
        return args;
    }

    public SettingItem(String title, String targetClass) {
        this.title = title;
        this.targetClass = targetClass;
    }

    public SettingItem(String title, String targetClass, Bundle args) {
        this.title = title;
        this.targetClass = targetClass;
        this.args = args;
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

    @NonNull
    @Override
    public String toString() {
        return title;
    }
}
