
package com.ledungcobra.cafo.models.restaurants;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestaurantArray {

    @SerializedName("restaurants")
    @Expose
    private List<BriefRestaurantInfo> restaurants = null;

    public List<BriefRestaurantInfo> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<BriefRestaurantInfo> restaurants) {
        this.restaurants = restaurants;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "restaurants=" + restaurants +
                '}';
    }
}
