package com.ledungcobra.cafo.sampledata;

public class Menu {
    private String id;
    private String[] foodId;

    public Menu(String id, String[] foodId) {
        this.id = id;
        this.foodId = foodId;
    }

    public String getId() {
        return id;
    }

    public String[] getFoodId() {
        return foodId;
    }
}
