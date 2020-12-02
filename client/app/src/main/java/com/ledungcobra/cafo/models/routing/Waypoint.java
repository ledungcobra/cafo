
package com.ledungcobra.cafo.models.routing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Waypoint {

    @SerializedName("location")
    @Expose
    private List<Double> location = null;
    @SerializedName("original_index")
    @Expose
    private Integer originalIndex;

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }

    public Integer getOriginalIndex() {
        return originalIndex;
    }

    public void setOriginalIndex(Integer originalIndex) {
        this.originalIndex = originalIndex;
    }


    @Override
    public String toString() {
        return "Waypoint{" +
                "location=" + location +
                ", originalIndex=" + originalIndex +
                '}';
    }
}
