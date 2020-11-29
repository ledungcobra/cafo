package com.ledungcobra.cafo.models.order.shipper;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ledungcobra.cafo.models.common_new.Image;

import java.io.Serializable;
import java.util.List;

public class Restaurant implements Serializable {

    @SerializedName("phones")
    @Expose
    private List<String> phones = null;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("image")
    @Expose
    private Image image;

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "phones=" + phones +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", image=" + image +
                '}';
    }
}