package com.ledungcobra.cafo.sampledata;

public class Food {
    private String id;
    private String description;
    private String price;
    private Photo[] photos;

    public Food(String id, String description, String price, Photo[] photos) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.photos = photos;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public Photo[] getPhotos() {
        return photos;
    }
}
