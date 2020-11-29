package com.ledungcobra.cafo.models.order.shipper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ledungcobra.cafo.models.common_new.Image;
import com.ledungcobra.cafo.models.common_new.Price;

import java.io.Serializable;

public class Food implements Serializable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("decription")
    @Expose
    private String decription;
    @SerializedName("price")
    @Expose
    private Price price;
    @SerializedName("image_url")
    @Expose
    private Image image;
    @SerializedName("amount")
    @Expose
    private String amount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Food{" +
                "name='" + name + '\'' +
                ", decription='" + decription + '\'' +
                ", price=" + price +
                ", image=" + image +
                ", amount='" + amount + '\'' +
                '}';
    }
}