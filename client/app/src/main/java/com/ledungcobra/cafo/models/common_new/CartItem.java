package com.ledungcobra.cafo.models.common_new;


import java.io.Serializable;

public class CartItem implements Serializable {
    Food food;
    int number;


    public CartItem(Food food, int number) {
        this.food = food;
        this.number = number;

    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

}

