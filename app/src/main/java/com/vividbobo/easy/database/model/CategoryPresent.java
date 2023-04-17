package com.vividbobo.easy.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * the Model to present Category
 */
public class CategoryPresent extends Category implements Parcelable {
    private boolean expanded;
    private Category parent;
    private List<Category> children = new ArrayList<>();


    public CategoryPresent(Category item) {
        super();
        setCategory(item);
    }

    public void setPresentCategory(Category category) {
        super.setCategory(category);
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    protected CategoryPresent(Parcel in) {
        expanded = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelableList(children, flags);
        dest.writeByte((byte) (expanded ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategoryPresent> CREATOR = new Creator<CategoryPresent>() {
        @Override
        public CategoryPresent createFromParcel(Parcel in) {
            return new CategoryPresent(in);
        }

        @Override
        public CategoryPresent[] newArray(int size) {
            return new CategoryPresent[size];
        }
    };

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }

    public void addChild(Category category) {
        children.add(category);
    }
}
