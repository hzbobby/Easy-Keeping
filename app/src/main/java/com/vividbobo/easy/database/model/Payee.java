package com.vividbobo.easy.database.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.vividbobo.easy.adapter.Itemzable;

import java.io.Serializable;

@Entity(tableName = "payees")
public class Payee extends BaseEntity implements Serializable, Itemzable {
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getItemTitle() {
        return getTitle();
    }

    @Override
    public String getItemIconResName() {
        return getIconResName();
    }

    @Override
    public String getItemDesc() {
        return getDesc();
    }
}
