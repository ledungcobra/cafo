
package com.ledungcobra.cafo.models.restaurants_new;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Operating  implements Serializable {

    @SerializedName("close_time")
    @Expose
    private String closeTime;
    @SerializedName("open_time")
    @Expose
    private String openTime;

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

}
