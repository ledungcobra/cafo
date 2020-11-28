package com.ledungcobra.cafo.models.map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PositionMap implements Serializable {
    @SerializedName("long")
    @Expose
    public Double _long;
    @SerializedName("lat")
    @Expose
    public Double lat;

    public Double get_long() {
        return _long;
    }

    public void set_long(Double _long) {
        this._long = _long;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "PositionMap{" +
                "_long=" + _long +
                ", lat=" + lat +
                '}';
    }
}
