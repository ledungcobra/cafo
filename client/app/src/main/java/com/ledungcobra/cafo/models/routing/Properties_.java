
package com.ledungcobra.cafo.models.routing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Properties_ {

    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("waypoints")
    @Expose
    private List<Waypoint_> waypoints = null;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<Waypoint_> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<Waypoint_> waypoints) {
        this.waypoints = waypoints;
    }


    @Override
    public String toString() {
        return "Properties_{" +
                "mode='" + mode + '\'' +
                ", waypoints=" + waypoints +
                '}';
    }
}
