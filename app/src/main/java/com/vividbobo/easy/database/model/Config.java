package com.vividbobo.easy.database.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "configs")
public class Config extends ServerBaseEntity {
    public static final int TYPE_LEGER = 0x0001;
    public static final int TYPE_ROLE = 0x0002;
    public static final int TYPE_ACCOUNT = 0x0003;
    public static final int TYPE_TAR_ACCOUNT = 0x0004;


    public static final int TYPE_CATEGORY_EXPENDITURE = 0x0040;
    public static final int TYPE_CATEGORY_INCOME = 0x0041;

    public static final int TYPE_AUTO_BILLING_LEGER = 0x0010;
    public static final int TYPE_AUTO_BILLING_ACCOUNT_WECHAT = 0x0030;
    public static final int TYPE_AUTO_BILLING_ACCOUNT_ALIPAY = 0x0031;
    public static final int TYPE_IMPORT_ACCOUNT_WECHAT = 0x0032;
    public static final int TYPE_IMPORT_ACCOUNT_ALIPAY = 0x0033;


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
