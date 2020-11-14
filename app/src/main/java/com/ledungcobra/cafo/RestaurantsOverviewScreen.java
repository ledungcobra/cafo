package com.ledungcobra.cafo;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

public class RestaurantsOverviewScreen extends Activity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ImageButton btnInfo;

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
        //TODO: Them search trên appbar(navbar)





    }
}