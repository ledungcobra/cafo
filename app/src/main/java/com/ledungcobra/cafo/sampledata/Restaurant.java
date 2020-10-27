package com.ledungcobra.cafo.sampledata;

public class Restaurant {
    private String id;
    private String menuId;
    private String restaurantUrl;
    private Photo[] photos;

    private String[]phones;
    private String[] operating;

    private String shortAddress;

    private double[] location;
    private String name;

    public Restaurant(String id, String menuId, String restaurantUrl, Photo[] photos, String[] phones, String[] operating, String short_address, double[] location, String name) {
        this.id = id;
        this.menuId = menuId;
        this.restaurantUrl = restaurantUrl;
        this.photos = photos;
        this.phones = phones;
        this.operating = operating;
        this.shortAddress = short_address;
        this.location = location;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getMenuId() {
        return menuId;
    }

    public String getRestaurantUrl() {
        return restaurantUrl;
    }

    public Photo[] getPhotos() {
        return photos;
    }

    public String[] getPhones() {
        return phones;
    }

    public String[] getOperating() {
        return operating;
    }

    public String getShortAddress() {
        return shortAddress;
    }

    public double[] getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }
}
