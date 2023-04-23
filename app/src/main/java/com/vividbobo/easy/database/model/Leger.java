package com.vividbobo.easy.database.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.vividbobo.easy.adapter.Itemzable;

import java.io.Serializable;

@Entity(tableName = "legers")
public class Leger extends ServerBaseEntity implements Serializable, Itemzable {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String title;       //账本名称
    private String desc;        //账本描述
    private String coverPath;   //账本封面

    private Resource.ResourceType coverType;   //system drawable or user image

    @Ignore
    private Integer billCount = 0;  //账单数量

    public Leger() {
    }

    @Ignore
    public Leger(String title, String desc, String coverPath, Resource.ResourceType coverType) {
        this.title = title;
        this.desc = desc;
        this.coverPath = coverPath;
        this.coverType = coverType;
    }

    @Ignore
    public Leger(Integer id, String title, String desc, String coverPath, Resource.ResourceType coverType) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.coverPath = coverPath;
        this.coverType = coverType;
    }

    public void setCoverType(Resource.ResourceType coverType) {
        this.coverType = coverType;
    }

    public Resource.ResourceType getCoverType() {
        return coverType;
    }

    public Integer getBillCount() {
        return billCount;
    }

    public void setBillCount(Integer billCount) {
        this.billCount = billCount;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    @Override
    public String getItemTitle() {
        return getTitle();
    }

    @Override
    public String getItemIconResName() {
        return getCoverPath();
    }

    @Override
    public String getItemDesc() {
        return String.format("共%d笔账单", getBillCount());
    }

    @Override
    public String toString() {
        return "Leger{" + "id=" + id + ", title='" + title + '\'' + ", desc='" + desc + '\'' + ", coverPath='" + coverPath + '\'' + ", coverType=" + coverType + ", billCount=" + billCount + '}';
    }
}
