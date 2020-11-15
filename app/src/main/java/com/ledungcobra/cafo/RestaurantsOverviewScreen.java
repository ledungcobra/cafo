package com.ledungcobra.cafo;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.ledungcobra.cafo.database.Repository;
import com.ledungcobra.cafo.ui_calllback.RestaurantClickListener;
import com.ledungcobra.cafo.view_adapter.RestaurantOverviewItemAdapter;

public class RestaurantsOverviewScreen extends Activity implements RestaurantClickListener {
    RecyclerView recyclerView;
    RestaurantOverviewItemAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ImageButton btnInfo;

    public static String DATA_KEY = "DATA";
    public static String EXTRA_KEY = "RESTAURANT";

    private boolean isLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_overview);
        btnInfo = findViewById(R.id.btnInfo);
        recyclerView = findViewById(R.id.restaurantOverviewRecyclerView);

        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RestaurantOverviewItemAdapter(this);
        recyclerView.setAdapter(adapter);

        adapter.setRestaurants(Repository.getInstance().getAllRestaurants());


        adapter.setOnRestaurantClickListener(this);

    }

    @Override
    public void onClick(String restaurantID) {

        Intent intent = new Intent(RestaurantsOverviewScreen.this,RestaurantDetailScreen.class);


        intent.putExtra(EXTRA_KEY,restaurantID);

        startActivity(intent);

    }
}