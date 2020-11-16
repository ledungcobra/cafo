
package com.ledungcobra.cafo.models.city;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CityArray {

    @SerializedName("cities")
    @Expose
    private List<City> cities = null;

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

}
