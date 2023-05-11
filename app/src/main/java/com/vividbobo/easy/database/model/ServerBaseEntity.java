package com.vividbobo.easy.database.model;

import com.opencsv.bean.CsvBindByName;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 上传服务器相关字段
 */
public class ServerBaseEntity implements Serializable {

    private Boolean uploaded=false;   // 是否上传
    private Timestamp createTime;   //创建时间

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
