package com.ledungcobra.cafo.service;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.ledungcobra.cafo.dao.TrackingRestaurantDao;
import com.ledungcobra.cafo.models.user.TrackingRestaurant;

@Database(entities = {TrackingRestaurant.class},version = 2,exportSchema = false)
public abstract class TrackingRestaurantRoomDatabase extends RoomDatabase {
    private static TrackingRestaurantRoomDatabase INSTANCE = null;
    public abstract TrackingRestaurantDao trackingRestaurantDao();
    private static RoomDatabase.Callback callback = new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            Log.d("CALL_API", "onOpen: ");
        }
    };
    public static TrackingRestaurantRoomDatabase getDatabase(final  Context ctx){
        if(INSTANCE == null){
            synchronized (TrackingRestaurantRoomDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(ctx,TrackingRestaurantRoomDatabase.class,"user_tracking")
                            .fallbackToDestructiveMigration().addCallback(callback).build();
                }
            }
        }
        return INSTANCE;
    }

}
