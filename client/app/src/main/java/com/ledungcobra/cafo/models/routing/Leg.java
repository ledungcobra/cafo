
package com.ledungcobra.cafo.models.routing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Leg {

    @SerializedName("distance")
    @Expose
    private Float distance;
    @SerializedName("time")
    @Expose
    private Float time;
    @SerializedName("steps")
    @Expose
    private List<Step> steps = null;

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public Float getTime() {
        return time;
    }

    public void setTime(Float time) {
        this.time = time;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "Leg{" +
                "distance=" + distance +
                ", time=" + time +
                ", steps=" + steps +
                '}';
    }
}
