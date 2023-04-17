package com.vividbobo.easy.database.model;

import androidx.recyclerview.widget.RecyclerView;

import com.vividbobo.easy.ui.others.ChildRvAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChildRvItem {
    private String title;
    private List<Resource> childItems;

    public ChildRvItem() {
        childItems = new ArrayList<>();
    }

    public ChildRvItem(String title) {
        this();
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Resource> getChildItems() {
        return childItems;
    }

    public void setChildItems(List<Resource> childItems) {
        this.childItems = childItems;
    }
}
