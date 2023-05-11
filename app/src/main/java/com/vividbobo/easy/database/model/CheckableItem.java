package com.vividbobo.easy.database.model;

import com.vividbobo.easy.adapter.Itemzable;

public class CheckableItem<T extends Itemzable> {
    private boolean checked;
    private T data;

    public CheckableItem(T data, boolean isChecked) {
        this.data = data;
        this.checked = isChecked;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }
}
