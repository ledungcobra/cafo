
package com.ledungcobra.cafo.models.order.shipper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ledungcobra.cafo.models.order.OrderPosition;

import java.io.Serializable;
import java.util.List;

public class DetailOrderResponse implements Serializable {

    @Expose
    private List<Food> foods = null;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("orderPosition")
    @Expose
    private OrderPosition orderPosition;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("restaurant_id")
    @Expose
    private String restaurantId;
    @SerializedName("shipper_id")
    @Expose
    private Object shipperId;
    @SerializedName("distanceToCus")
    @Expose
    private Double distanceToCus;
    @SerializedName("distanceToRes")
    @Expose
    private Double distanceToRes;
    @SerializedName("restaurant")
    @Expose
    private Restaurant restaurant;

    @SerializedName("order_time")
    @Expose
    private String orderTime;

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OrderPosition getOrderPosition() {
        return orderPosition;
    }

    public void setOrderPosition(OrderPosition orderPosition) {
        this.orderPosition = orderPosition;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Object getShipperId() {
        return shipperId;
    }

    public void setShipperId(Object shipperId) {
        this.shipperId = shipperId;
    }

    public Double getDistanceToCus() {
        return distanceToCus;
    }

    public void setDistanceToCus(Double distanceToCus) {
        this.distanceToCus = distanceToCus;
    }

    public Double getDistanceToRes() {
        return distanceToRes;
    }

    public void setDistanceToRes(Double distanceToRes) {
        this.distanceToRes = distanceToRes;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "DetailOrderResponse{" +
                "foods=" + foods +
                ", id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", orderPosition=" + orderPosition +
                ", userId='" + userId + '\'' +
                ", restaurantId='" + restaurantId + '\'' +
                ", shipperId=" + shipperId +
                ", distanceToCus=" + distanceToCus +
                ", distanceToRes=" + distanceToRes +
                ", restaurant=" + restaurant +
                '}';
    }
}
