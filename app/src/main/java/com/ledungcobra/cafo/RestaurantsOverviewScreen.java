package com.ledungcobra.cafo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class RestaurantsOverviewScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_overview);

        //TODO: GridView -> Tao custom view để bind vào từng ô trong grid giống thiết kế NumColumns: auto_fit
        //TODO: Them search trên appbar(navbar)


        Toast.makeText(this,getIntent().getStringExtra("KEY"),Toast.LENGTH_SHORT).show();



    }
}