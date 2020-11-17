package com.ledungcobra.cafo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageButton;

import com.ledungcobra.cafo.database.Repository;
import com.ledungcobra.cafo.models.restaurants.RestaurantArray;
import com.ledungcobra.cafo.ui_calllback.RestaurantClickListener;
import com.ledungcobra.cafo.view_adapter.RestaurantOverviewItemAdapter;

public class RestaurantsOverviewScreen extends AppCompatActivity implements RestaurantClickListener {
    RecyclerView recyclerView;
    RestaurantOverviewItemAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ImageButton btnInfo;

    public static String DATA_KEY = "DATA";
    public static String EXTRA_KEY = "RESTAURANT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_overview);
//        btnInfo = findViewById(R.id.btnInfo);
        recyclerView = findViewById(R.id.restaurantOverviewRecyclerView);

        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RestaurantOverviewItemAdapter(this);
        recyclerView.setAdapter(adapter);

        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY-oldScrollY>0){
                    hideComponents();
                }else if(scrollY-oldScrollY<0){
                   showComponents();
                }
            }
        });

        Repository.getInstance().getAllRestaurants().observe(this, new Observer<RestaurantArray>() {
            @Override
            public void onChanged(RestaurantArray restaurantArray) {
                adapter.setRestaurants(restaurantArray.getRestaurants());
            }
        });
        adapter.setOnRestaurantClickListener(this);
    }
    private void showComponents(){
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this, R.layout.activity_restaurants_overview);


        ChangeBounds transition = new ChangeBounds();

        transition.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        transition.setDuration(1200);


        TransitionManager.beginDelayedTransition((ConstraintLayout)findViewById(R.id.constraint), transition);
        constraintSet.applyTo((ConstraintLayout)findViewById(R.id.constraint));

    }

    private void hideComponents(){
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this, R.layout.activity_restaurants_overview_up);


        ChangeBounds transition = new ChangeBounds();

        transition.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        transition.setDuration(1200);


        TransitionManager.beginDelayedTransition((ConstraintLayout)findViewById(R.id.constraint), transition);
        constraintSet.applyTo((ConstraintLayout)findViewById(R.id.constraint));

    }
    @Override
    public void onClick(String restaurantID) {

        Intent intent = new Intent(RestaurantsOverviewScreen.this,RestaurantDetailScreen.class);


        intent.putExtra(EXTRA_KEY,restaurantID);

        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shop_selected_menu, menu);
        return true;

    }
}