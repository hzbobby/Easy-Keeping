package com.vividbobo.easy.database.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "currencies")
public class Currency extends BaseEntity implements Serializable {
    @PrimaryKey
    @NonNull
    private String code;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public Currency() {
    }

    @Ignore
    public Currency(@NonNull String code, boolean selected, String title, String desc, String iconResName) {
        super(title, desc, iconResName);
        this.code = code;
        this.selected = selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
