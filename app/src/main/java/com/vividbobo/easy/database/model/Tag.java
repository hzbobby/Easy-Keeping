package com.vividbobo.easy.database.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.opencsv.bean.CsvBindByName;
import com.vividbobo.easy.adapter.Itemzable;

import java.io.Serializable;
import java.sql.Timestamp;

@Entity(tableName = "tags")
public class Tag extends ServerBaseEntity implements Serializable, Itemzable {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String title;
    private String hexCode;

    public Tag() {
    }

    @Ignore
    public Tag(Tag tag) {
        this.id = tag.getId();
        this.title = tag.title;
        this.hexCode = tag.hexCode;
    }

    @Ignore
    public Tag(String title, String hexCode) {
        this.title = title;
        this.hexCode = hexCode;
        setCreateTime(new Timestamp(System.currentTimeMillis()));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHexCode() {
        return hexCode;
    }

    public void setHexCode(String hexCode) {
        this.hexCode = hexCode;
    }

    @Override
    public String getItemTitle() {
        return getTitle();
    }

    @Override
    public String getItemIconResName() {
        return null;
    }

    @Override
    public String getItemDesc() {
        return getHexCode();
    }
}

