package com.ledungcobra.cafo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.ledungcobra.cafo.database.Repository;
import com.ledungcobra.cafo.models.restaurants_new.BriefRestaurantInfo;
import com.ledungcobra.cafo.ui_calllback.UIThreadCallBack;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    View appTitle;

    private static String TAG = "CALL_API";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appTitle = findViewById(R.id.app_title);
        appTitle.setAlpha(0);

        appTitle.animate().alphaBy(1).setDuration(3000).start();

        final Repository repository = Repository.getInstance();

        LiveData<ArrayList<BriefRestaurantInfo>> restaurants = repository.fetchAllRestaurants(1, 10, new UIThreadCallBack<ArrayList<BriefRestaurantInfo>, Error>() {

            @Override
            public void onResult(ArrayList<BriefRestaurantInfo> result) {
                //Having
                Intent intent = new Intent(MainActivity.this,RestaurantsOverviewScreen.class);
                finish();
                startActivity(intent);
            }

            @Override
            public void onFailure(Error error) {
                //Handle Error
                Log.d(TAG, "onFailure: " + error);
            }

            @Override
            public void stopProgressIndicator() {
                //Handle stop progress indicator
            }

            @Override
            public void startProgressIndicator() {
                //Handle start progress indicator
            }
        });



    }

}