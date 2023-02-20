package com.vividbobo.easy.model;

import android.view.inputmethod.BaseInputConnection;

import com.vividbobo.easy.BaseActivity;

public class ImageItem extends BaseItem {
    private String coverPath;

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }
}
