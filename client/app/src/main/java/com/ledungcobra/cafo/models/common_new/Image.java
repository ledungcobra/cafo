
package com.ledungcobra.cafo.models.common_new;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("width")
    @Expose
    private Integer width;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("height")
    @Expose
    private Integer height;

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "Image{" +
                "width=" + width +
                ", value='" + value + '\'' +
                ", height=" + height +
                '}';
    }
}
