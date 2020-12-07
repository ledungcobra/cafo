
package com.ledungcobra.cafo.models.common_new;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Price implements Serializable {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("value")
    @Expose
    private Integer value;

    public Price(int sumOfCost){
        this.value = sumOfCost;
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Price{" +
                "text='" + text + '\'' +
                ", unit='" + unit + '\'' +
                ", value=" + value +
                '}';
    }
}
