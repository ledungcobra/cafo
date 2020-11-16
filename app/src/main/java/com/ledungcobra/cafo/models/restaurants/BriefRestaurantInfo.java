
package com.ledungcobra.cafo.models.restaurants;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ledungcobra.cafo.models.common.Address;
import com.ledungcobra.cafo.models.common.Operating;
import com.ledungcobra.cafo.models.common.Photo;
import com.ledungcobra.cafo.models.common.Position;


import java.util.List;

public class BriefRestaurantInfo {

    @SerializedName("photos")
    @Expose
    private List<Photo> photos = null;
    @SerializedName("phones")
    @Expose
    private List<String> phones = null;
    @SerializedName("operating")
    @Expose
    private List<Operating> operating = null;
    @SerializedName("position")
    @Expose
    private List<Position> position = null;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("menu_id")
    @Expose
    private String menuId;
    @SerializedName("address_id")
    @Expose
    private Address addressId;
    @SerializedName("single_address")
    @Expose
    private String singleAddress;
    @SerializedName("restaurant_url")
    @Expose
    private String restaurantUrl;
    @SerializedName("name")
    @Expose
    private String name;

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public List<Operating> getOperating() {
        return operating;
    }

    public void setOperating(List<Operating> operating) {
        this.operating = operating;
    }

    public List<Position> getPosition() {
        return position;
    }

    public void setPosition(List<Position> position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public Address getAddressId() {
        return addressId;
    }

    public void setAddressId(Address addressId) {
        this.addressId = addressId;
    }

    public String getSingleAddress() {
        return singleAddress;
    }

    public void setSingleAddress(String singleAddress) {
        this.singleAddress = singleAddress;
    }

    public String getRestaurantUrl() {
        return restaurantUrl;
    }

    public void setRestaurantUrl(String restaurantUrl) {
        this.restaurantUrl = restaurantUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Restaurant_{" +
                "photos=" + photos +
                ", phones=" + phones +
                ", operating=" + operating +
                ", position=" + position +
                ", id='" + id + '\'' +
                ", menuId='" + menuId + '\'' +
                ", addressId=" + addressId +
                ", singleAddress='" + singleAddress + '\'' +
                ", restaurantUrl='" + restaurantUrl + '\'' +
                ", name='" + name + '\'' +
                '}';
    }


}
