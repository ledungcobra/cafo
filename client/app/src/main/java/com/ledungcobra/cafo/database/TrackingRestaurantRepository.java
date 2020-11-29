package com.ledungcobra.cafo.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.ledungcobra.cafo.dao.TrackingRestaurantDao;
import com.ledungcobra.cafo.models.user.TrackingRestaurant;

import java.util.List;

public class TrackingRestaurantRepository {
    private TrackingRestaurantDao trackingRestaurantDao;
    private LiveData<List<TrackingRestaurant>> trackingRestaurants;
    public TrackingRestaurantRepository(Application app){
        TrackingRestaurantRoomDatabase db = TrackingRestaurantRoomDatabase.getDatabase(app);
        this.trackingRestaurantDao = db.trackingRestaurantDao();
        trackingRestaurants = trackingRestaurantDao.getAllTrackingRestaurants();
    }

    public LiveData<List<TrackingRestaurant>> getAllRestaurants(){
        return trackingRestaurants;
    }

    public void insert(final TrackingRestaurant restaurant){
        new Thread(new Runnable() {
            @Override
            public void run() {
                trackingRestaurantDao.insert(restaurant);
            }
        }) .start();
    }

}
