
package com.ledungcobra.cafo.models.restaurant_detail_new;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ledungcobra.cafo.models.common_new.Food;

import java.io.Serializable;
import java.util.List;

public class Menu implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("foods")
    @Expose
    private List<Food> foods = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", foods=" + foods +
                '}';
    }
}
