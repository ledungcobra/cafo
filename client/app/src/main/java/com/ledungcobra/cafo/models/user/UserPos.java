package com.ledungcobra.cafo.models.user;

import java.io.Serializable;

public class UserPos implements Serializable {
    private double latitude;
    private double longitude;

    public UserPos(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
