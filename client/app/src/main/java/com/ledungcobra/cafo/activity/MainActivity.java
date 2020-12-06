package com.ledungcobra.cafo.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.service.Repository;
import com.ledungcobra.cafo.models.restaurants_new.BriefRestaurantInfo;
import com.ledungcobra.cafo.ui_calllback.UIThreadCallBack;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    //VIEW
    private View appTitle;

    //DATA
    private static String TAG = "CALL_API";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnimationDrawable animationDrawable;
        ImageView mProgressBar = findViewById(R.id.gif_progress_bar);
        animationDrawable = (AnimationDrawable)mProgressBar.getDrawable();
        mProgressBar.setVisibility(View.VISIBLE);
        animationDrawable.start();

        appTitle = findViewById(R.id.app_title);
        appTitle.setAlpha(0);

        appTitle.animate().alphaBy(1).setDuration(3000).start();

        final Repository repository = Repository.getInstance();

        LiveData<ArrayList<BriefRestaurantInfo>> restaurants = repository.fetchAllRestaurants(1, 10, new UIThreadCallBack<ArrayList<BriefRestaurantInfo>, Error>() {

            @Override
            public void onResult(ArrayList<BriefRestaurantInfo> result) {
                //Having
                Intent intent = new Intent(MainActivity.this, RestaurantsOverviewScreen.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(getString(R.string.list_restaurants),result);
                intent.putExtras(bundle);

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