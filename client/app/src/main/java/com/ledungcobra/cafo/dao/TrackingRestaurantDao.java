package com.ledungcobra.cafo.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ledungcobra.cafo.models.user.TrackingRestaurant;

import java.util.List;

@Dao
public interface TrackingRestaurantDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(TrackingRestaurant trackingRestaurant);


    @Query("DELETE FROM tracking_restaurant_table")
    void deleteAll();



    @Query("SELECT * FROM tracking_restaurant_table")
    LiveData<List<TrackingRestaurant>> getAllTrackingRestaurants();
}
