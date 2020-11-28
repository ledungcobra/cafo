package com.ledungcobra.cafo.models.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderResponse implements Serializable {
    public OrderResponse(String message) {
        this.message = message;
    }

    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "message='" + message + '\'' +
                '}';
    }
}
