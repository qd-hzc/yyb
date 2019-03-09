package com.jeeplus.modules.api.usage.entity;

import java.io.Serializable;

public class YybUsageDto implements Serializable {
    private String id;

    private String name;

    private String rate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
