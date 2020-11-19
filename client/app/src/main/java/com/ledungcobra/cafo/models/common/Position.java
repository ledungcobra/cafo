
package com.ledungcobra.cafo.models.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Position {

    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("is_verified")
    @Expose
    private Boolean isVerified;
    @SerializedName("longitude")
    @Expose
    private Double longitude;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Position{" +
                "latitude=" + latitude +
                ", isVerified=" + isVerified +
                ", longitude=" + longitude +
                '}';
    }
}
