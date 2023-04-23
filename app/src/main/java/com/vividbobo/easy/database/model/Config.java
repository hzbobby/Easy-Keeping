package com.vividbobo.easy.database.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "configs")
public class Config extends ServerBaseEntity {
    public static final int CONFIG_TYPE_LEGER = 1;
    public static final int CONFIG_TYPE_ROLE = 2;
    public static final int CONFIG_TYPE_ACCOUNT = 3;

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int type;   //配置类型
    private int selected_id;

    public Config() {
    }

    @Ignore
    public Config(int type, int selected_id) {
        this.type = type;
        this.selected_id = selected_id;
    }

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
