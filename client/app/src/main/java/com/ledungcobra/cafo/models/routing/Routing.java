
package com.ledungcobra.cafo.models.routing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Routing {

    @SerializedName("features")
    @Expose
    private List<Feature> features = null;
    @SerializedName("properties")
    @Expose
    private Properties_ properties;
    @SerializedName("type")
    @Expose
    private String type;

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public Properties_ getProperties() {
        return properties;
    }

    public void setProperties(Properties_ properties) {
        this.properties = properties;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Routing{" +
                "features=" + features +
                ", properties=" + properties +
                ", type='" + type + '\'' +
                '}';
    }
}
