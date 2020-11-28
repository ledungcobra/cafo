package com.ledungcobra.cafo.database;

import java.io.Serializable;

class UserPos implements Serializable {
    private String latitude;
    private String longitude;

    public UserPos(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
