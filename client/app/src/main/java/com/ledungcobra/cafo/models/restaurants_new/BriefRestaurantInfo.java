
package com.ledungcobra.cafo.models.restaurants_new;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ledungcobra.cafo.models.common_new.Image;
import com.ledungcobra.cafo.models.common_new.PriceRange;

import java.io.Serializable;
import java.util.List;

public class BriefRestaurantInfo  implements Serializable {

    @SerializedName("phones")
    @Expose
    private List<String> phones = null;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("restaurant_url")
    @Expose
    private String restaurantUrl;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("image")
    @Expose
    private Image image;
    @SerializedName("operating")
    @Expose
    private Operating operating;
    @SerializedName("price_range")
    @Expose
    private PriceRange priceRange;
    @SerializedName("city_id")
    @Expose
    private String cityId;

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRestaurantUrl() {
        return restaurantUrl;
    }

    public void setRestaurantUrl(String restaurantUrl) {
        this.restaurantUrl = restaurantUrl;
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

    public Operating getOperating() {
        return operating;
    }

    public void setOperating(Operating operating) {
        this.operating = operating;
    }

    public PriceRange getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(PriceRange priceRange) {
        this.priceRange = priceRange;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    @Override
    public String toString() {
        return "BriefRestaurantInfo{" +
                "phones=" + phones +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", restaurantUrl='" + restaurantUrl + '\'' +
                ", address='" + address + '\'' +
                ", image=" + image +
                ", operating=" + operating +
                ", priceRange=" + priceRange +
                ", cityId='" + cityId + '\'' +
                '}';
    }
}
