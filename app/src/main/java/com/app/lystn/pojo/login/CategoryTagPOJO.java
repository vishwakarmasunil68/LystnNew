package com.app.lystn.pojo.login;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CategoryTagPOJO implements Serializable {

    @SerializedName("catId")
    String catId;
    @SerializedName("category")
    String category;
    boolean active;


    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
