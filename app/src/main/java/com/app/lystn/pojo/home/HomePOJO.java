package com.app.lystn.pojo.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class HomePOJO implements Serializable {
    @SerializedName("bkId")
    @Expose
    private String bkId;
    @SerializedName("bkName")
    @Expose
    private String bkName;
    @SerializedName("bkType")
    @Expose
    private String bkType;
    @SerializedName("shapeType")
    @Expose
    private String shapeType;
    @SerializedName("contents")
    @Expose
    private List<HomeContentPOJO> homeContentPOJOS;

    public String getBkId() {
        return bkId;
    }

    public void setBkId(String bkId) {
        this.bkId = bkId;
    }

    public String getBkName() {
        return bkName;
    }

    public void setBkName(String bkName) {
        this.bkName = bkName;
    }

    public String getShapeType() {
        return shapeType;
    }

    public void setShapeType(String shapeType) {
        this.shapeType = shapeType;
    }

    public List<HomeContentPOJO> getHomeContentPOJOS() {
        return homeContentPOJOS;
    }

    public void setHomeContentPOJOS(List<HomeContentPOJO> homeContentPOJOS) {
        this.homeContentPOJOS = homeContentPOJOS;
    }

    public String getBkType() {
        return bkType;
    }

    public void setBkType(String bkType) {
        this.bkType = bkType;
    }
}
