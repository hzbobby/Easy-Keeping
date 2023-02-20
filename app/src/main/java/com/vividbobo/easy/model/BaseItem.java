package com.vividbobo.easy.model;

import java.io.Serializable;

public class BaseItem implements Serializable {
    private Long Id;



    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }
}
