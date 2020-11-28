package com.ledungcobra.cafo.models.order;

import java.io.Serializable;

public class FoodOrderItem implements Serializable {
    private String foodID;
    private int count;

    public FoodOrderItem(String foodID, int count) {
        this.foodID = foodID;
        this.count = count;
    }

    public String getFoodID() {
        return foodID;
    }

    public void setFoodID(String foodID) {
        this.foodID = foodID;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "FoodOrderItem{" +
                "foodID='" + foodID + '\'' +
                ", count=" + count +
                '}';
    }
}
