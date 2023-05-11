package com.vividbobo.easy.database.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.vividbobo.easy.adapter.Itemzable;

import java.io.Serializable;
import java.security.SecureRandom;

@Entity(tableName = "roles")
public class Role extends BaseEntity implements Serializable, Itemzable {
    @PrimaryKey(autoGenerate = true)
    private Integer id;

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
