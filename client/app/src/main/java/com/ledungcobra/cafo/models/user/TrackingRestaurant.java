package com.ledungcobra.cafo.models.user;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tracking_restaurant_table")
public class TrackingRestaurant {
    final public static int FAVORITE = 0;
    final public static int VISITED = 1;
    @PrimaryKey
    @NonNull
    @ColumnInfo(name="id")
    private String id;
    @ColumnInfo(name = "type")
    private int type;

    public TrackingRestaurant(@NonNull String id, int type) {
        this.id = id;
        this.type = type;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "TrackingRestaurant{" +
                "id='" + id + '\'' +
                ", type=" + type +
                '}';
    }
}