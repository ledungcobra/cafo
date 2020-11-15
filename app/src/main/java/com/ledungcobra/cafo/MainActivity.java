package com.ledungcobra.cafo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ledungcobra.cafo.database.Repository;
import com.ledungcobra.cafo.models.common.Restaurant;
import com.ledungcobra.cafo.models.restaurants.BriefRestaurantInfo;
import com.ledungcobra.cafo.ui_calllback.UIThreadCallBack;

import java.util.List;


public class MainActivity extends Activity {
    View appTitle;

    private static String TAG = "CALLAPI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appTitle = findViewById(R.id.app_title);
        appTitle.setAlpha(0);

        appTitle.animate().alphaBy(1).setDuration(3000).start();

        Repository repository = Repository.getInstance();

        repository.getAllRestaurants(new UIThreadCallBack<List<BriefRestaurantInfo>,Error>(){

            @Override
            public void onResult(List<BriefRestaurantInfo> result) {
                //Having
                Intent intent = new Intent(MainActivity.this,RestaurantsOverviewScreen.class);

                intent.putExtra("DONE","DOne");
                startActivity(intent);

            }

            @Override
            public void onFailure(Error error) {
                //Handle Error
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