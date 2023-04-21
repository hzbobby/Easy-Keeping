package com.vividbobo.easy.database.model;

import android.content.pm.LauncherActivityInfo;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.vividbobo.easy.adapter.DropdownMenuAdapter;

import java.io.Serializable;

@Entity(tableName = "currencies")
public class Currency implements Serializable, DropdownMenuAdapter.DropdownMenuItem {
    @PrimaryKey
    @NonNull
    private String code;
    private Integer code_num;
    private String title;
    private String country;
    private Float rate;     //相比于本位货币的简历

    private Float localRate;    //自己手动设置的利率
    private boolean enable;

    private boolean autoUpdate;

    public boolean isAutoUpdate() {
        return autoUpdate;
    }

    public void setAutoUpdate(boolean autoUpdate) {
        this.autoUpdate = autoUpdate;
    }

    public Currency() {
    }

    @Ignore
    public Currency(@NonNull String code, Integer code_num, String title, String country) {
        this.code = code;
        this.code_num = code_num;
        this.title = title;
        this.country = country;
        enable = false;
        autoUpdate = true;
        rate = 0f;
        localRate = 0f;
    }

    public Float getLocalRate() {
        return localRate;
    }

    public void setLocalRate(Float localRate) {
        this.localRate = localRate;
    }

    public Integer getCode_num() {
        return code_num;
    }

    public void setCode_num(Integer code_num) {
        this.code_num = code_num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getItemIconResName() {
        return null;
    }

    @Override
    public String getItemTitle() {
        return String.format("%s - %s", getTitle(), getCode());
    }

    @Override
    public String getItemDesc() {
        return null;
    }
}
