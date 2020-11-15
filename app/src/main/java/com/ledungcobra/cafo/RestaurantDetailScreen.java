package com.ledungcobra.cafo;


import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;

import com.ledungcobra.cafo.database.Repository;
import com.ledungcobra.cafo.models.common.Food;
import com.ledungcobra.cafo.models.common.Restaurant;
import com.ledungcobra.cafo.ui_calllback.UIThreadCallBack;
import com.ledungcobra.cafo.view_adapter.MenuListViewAdapter;

import java.util.ArrayList;

import static com.ledungcobra.cafo.RestaurantsOverviewScreen.DATA_KEY;
import static com.ledungcobra.cafo.RestaurantsOverviewScreen.EXTRA_KEY;

public class RestaurantDetailScreen extends Activity {

    ListView lvMenu;
    ImageView ivLoc;
    ImageView ivDist;
    MenuListViewAdapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail_screen);

        Intent intent = getIntent();
        String restaurantID =intent.getStringExtra(EXTRA_KEY);
        adapter = new MenuListViewAdapter(this,new ArrayList<Food>());
        lvMenu = findViewById(R.id.lvMenu);
        lvMenu.setAdapter(adapter);

        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Do something
            }
        });

        ivLoc = findViewById(R.id.ivLoc);
        ivDist = findViewById(R.id.ivDist);
        ivLoc.setImageResource(R.drawable.location);
        ivDist.setImageResource(R.drawable.cursor);

        findViewById(R.id.btnMap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantDetailScreen.this,MapScreen.class);
                startActivity(intent);
            }
        });


        LayoutInflater layoutInflater = getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.progress_indicator,null,false);
        final ProgressBar progressBar = view.findViewById(R.id.progress_circular);

        final ViewGroup detailViewGroup = ((ViewGroup)findViewById(R.id.restaurant_detail_view));


        Repository.getInstance().getRestaurant(restaurantID, new UIThreadCallBack<Restaurant, Error>() {
            @Override
            public void stopProgressIndicator() {
               detailViewGroup.removeView(view);
               detailViewGroup.addView(lvMenu);
            }

            @Override
            public void startProgressIndicator() {
                ((ViewGroup)lvMenu.getParent()).removeView(lvMenu);
                detailViewGroup.addView(view);
            }

            @Override
            public void onResult(Restaurant result) {
                adapter.setFoods(result.getMenuId().getFoodsId());
            }

            @Override
            public void onFailure(Error error) {

            }
        });

    }

}