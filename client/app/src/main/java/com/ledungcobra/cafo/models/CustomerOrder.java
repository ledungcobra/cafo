package com.ledungcobra.cafo.models;

import java.io.Serializable;

public class CustomerOrder implements Serializable {

    String price;
    String foodName;
    String quantity;
    String totalPrice;

    public CustomerOrder(String price, String foodName, String quantity, String totalPrice) {
        this.price = price;
        this.foodName = foodName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
