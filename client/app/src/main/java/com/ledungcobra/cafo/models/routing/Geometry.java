
package com.ledungcobra.cafo.models.routing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Geometry {

    @SerializedName("coordinates")
    @Expose
    private List<List<List<Double>>> coordinates = null;
    @SerializedName("type")
    @Expose
    private String type;

    public List<List<List<Double>>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<List<List<Double>>> coordinates) {
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Geometry{" +
                "coordinates=" + coordinates +
                ", type='" + type + '\'' +
                '}';
    }
}
