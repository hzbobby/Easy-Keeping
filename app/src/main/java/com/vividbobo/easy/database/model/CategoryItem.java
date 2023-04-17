package com.vividbobo.easy.database.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CategoryItem extends Category implements Serializable {
    private List<Category> children;
    private boolean expanded;

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public CategoryItem() {
        super();
        children = new ArrayList<>();
    }

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }


}
