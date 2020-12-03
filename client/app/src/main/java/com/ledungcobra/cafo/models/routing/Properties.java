
package com.ledungcobra.cafo.models.routing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Properties {

    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("waypoints")
    @Expose
    private List<Waypoint> waypoints = null;
    @SerializedName("distance")
    @Expose
    private Integer distance;
    @SerializedName("time")
    @Expose
    private Integer time;
    @SerializedName("legs")
    @Expose
    private List<Leg> legs = null;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public List<Leg> getLegs() {
        return legs;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }

    @Override
    public String toString() {
        return "Properties{" +
                "mode='" + mode + '\'' +
                ", waypoints=" + waypoints +
                ", distance=" + distance +
                ", time=" + time +
                ", legs=" + legs +
                '}';
    }
}
