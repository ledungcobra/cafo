package com.ledungcobra.cafo;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ledungcobra.cafo.database.Repository;
import com.ledungcobra.cafo.models.restaurant_detail_new.RestaurantDetail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallApi extends AppCompatActivity {

    final String TAG="CALL_API";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_api2);
        List<String> strings = new ArrayList<>();
        Collections.addAll(strings,"5fbd3704c2105b4094990582","5fbd3704c2105b40949905fc");
        Repository.getInstance().getRestaurantService().getRestaurant_("5fbd3704c2105b4094990582").enqueue(new Callback<RestaurantDetail>() {
            @Override
            public void onResponse(Call<RestaurantDetail> call, Response<RestaurantDetail> response) {
                Log.d(TAG, "onResponse: "+response.body().toString());
            }

            @Override
            public void onFailure(Call<RestaurantDetail> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t);
            }
        });




    }

}