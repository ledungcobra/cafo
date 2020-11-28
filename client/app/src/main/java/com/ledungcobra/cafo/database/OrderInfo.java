package com.ledungcobra.cafo.database;

import com.ledungcobra.cafo.models.order.FoodOrderItem;

import java.util.List;

public class OrderInfo {
    private String resID;
    private UserPos userPos;
    private List<FoodOrderItem> food;

    public OrderInfo(String resID, UserPos userPos, List<FoodOrderItem> food) {
        this.resID = resID;
        this.userPos = userPos;
        this.food = food;
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

    public List<FoodOrderItem> getFood() {
        return food;
    }

    public void setFood(List<FoodOrderItem> food) {
        this.food = food;
    }
}
