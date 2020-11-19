
package com.ledungcobra.cafo.models.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Address {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("city_url")
    @Expose
    private String cityUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityUrl() {
        return cityUrl;
    }

    public void setCityUrl(String cityUrl) {
        this.cityUrl = cityUrl;
    }

    @Override
    public String toString() {
        return "AddressId{" +
                "id='" + id + '\'' +
                ", district='" + district + '\'' +
                ", city='" + city + '\'' +
                ", cityUrl='" + cityUrl + '\'' +
                '}';
    }
}
