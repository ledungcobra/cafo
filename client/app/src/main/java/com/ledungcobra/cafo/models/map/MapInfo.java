package com.ledungcobra.cafo.models.map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MapInfo implements Serializable {

    @SerializedName("place_id")
    @Expose
    private Integer placeId;
    @SerializedName("licence")
    @Expose
    private String licence;
    @SerializedName("osm_type")
    @Expose
    private String osmType;
    @SerializedName("osm_id")
    @Expose
    private Integer osmId;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lon")
    @Expose
    private String lon;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("boundingbox")
    @Expose
    private List<String> boundingbox = null;

    public Integer getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Integer placeId) {
        this.placeId = placeId;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getOsmType() {
        return osmType;
    }

    public void setOsmType(String osmType) {
        this.osmType = osmType;
    }

    public Integer getOsmId() {
        return osmId;
    }

    public void setOsmId(Integer osmId) {
        this.osmId = osmId;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<String> getBoundingbox() {
        return boundingbox;
    }

    public void setBoundingbox(List<String> boundingbox) {
        this.boundingbox = boundingbox;
    }

    @Override
    public String toString() {
        return "MapInfo{" +
                "placeId=" + placeId +
                ", licence='" + licence + '\'' +
                ", osmType='" + osmType + '\'' +
                ", osmId=" + osmId +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", displayName='" + displayName + '\'' +
                ", address=" + address +
                ", boundingbox=" + boundingbox +
                '}';
    }
}
