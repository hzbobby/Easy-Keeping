package com.vividbobo.easy.database.model;

import java.io.Serializable;

public class LegerItem implements Serializable {
    private Long id;
    private String coverPath;
    private String title;
    private String desc;

    public Long getId() {
        return id;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
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
}
