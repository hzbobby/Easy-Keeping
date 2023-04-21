package com.vividbobo.easy.database.model;

import java.io.Serializable;

public class TagPresent extends Tag implements Serializable {
    private boolean checked;

    public TagPresent(Tag tag) {
        super(tag);
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
