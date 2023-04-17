package com.vividbobo.easy.database.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "configs")
public class Config {
    public static final int CONFIG_LEGER_SELECTED = 1;
    public static final int CONFIG_ROLE_SELECTED = 2;
    public static final int CONFIG_ACCOUNT_SELECTED = 3;

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int type;   //配置类型
    private int selected_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSelected_id() {
        return selected_id;
    }

    public void setSelected_id(int selected_id) {
        this.selected_id = selected_id;
    }
}
