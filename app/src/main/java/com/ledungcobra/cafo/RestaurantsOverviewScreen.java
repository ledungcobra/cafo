package com.ledungcobra.cafo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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
        btnInfo = findViewById(R.id.btnInfo);
        recyclerView = findViewById(R.id.restaurantOverviewRecyclerView);

        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RestaurantOverviewItemAdapter(this);
        recyclerView.setAdapter(adapter);

        Repository.getInstance().getAllRestaurants().observe(this, new Observer<RestaurantArray>() {
            @Override
            public void onChanged(RestaurantArray restaurantArray) {
                adapter.setRestaurants(restaurantArray.getRestaurants());
            }
        });
        adapter.setOnRestaurantClickListener(this);
    }

    @Override
    public void onClick(String restaurantID) {

        Intent intent = new Intent(RestaurantsOverviewScreen.this,RestaurantDetailScreen.class);


        intent.putExtra(EXTRA_KEY,restaurantID);

        startActivity(intent);

    }
}