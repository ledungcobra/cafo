package com.ledungcobra.cafo.models.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ledungcobra.cafo.models.user.UserPos;

import java.util.List;

public class OrderInfo {

    @SerializedName("resID")
    @Expose
    private String resID;

    private UserPos userPos;
    private List<FoodOrderItem> foods;

    public OrderInfo(String resID, UserPos userPos, List<FoodOrderItem> foods) {
        this.resID = resID;
        this.userPos = userPos;
        this.foods = foods;
    }

    public String getResID() {
        return resID;
    }

    public void setResID(String resID) {
        this.resID = resID;
    }

    public UserPos getUserPos() {
        return userPos;
    }

    public void setUserPos(UserPos userPos) {
        this.userPos = userPos;
    }

    public List<FoodOrderItem> getFoods() {
        return foods;
    }

    public void setFoods(List<FoodOrderItem> foods) {
        this.foods = foods;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "resID='" + resID + '\'' +
                ", userPos=" + userPos +
                ", foods=" + foods +
                '}';
    }
}
