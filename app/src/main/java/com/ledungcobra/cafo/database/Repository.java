package com.ledungcobra.cafo.database;

import android.app.Application;

import com.google.gson.GsonBuilder;
import com.ledungcobra.cafo.models.city.CityArray;
import com.ledungcobra.cafo.models.restaurant_detail.RestaurantDetail;
import com.ledungcobra.cafo.models.restaurants.RestaurantArray;
import com.ledungcobra.cafo.network.NetworkService;
import com.ledungcobra.cafo.ui_calllback.UIThreadCallBack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {

    private static final String BASE_URL = "http://cafo-dev-api.herokuapp.com/";

    private NetworkService networkService;
    public Repository(Application application){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder()
                        .create()))
                .build();
        networkService = retrofit.create(NetworkService.class);

    }

    public void getAllRestaurants(final UIThreadCallBack callBack){


        callBack.startProgressIndicator();
        networkService.getRestaurants().enqueue(new Callback<RestaurantArray>() {
            @Override
            public void onResponse(Call<RestaurantArray> call, Response<RestaurantArray> response) {
                callBack.stopProgressIndicator();
                callBack.onResult(response.body());
            }

            @Override
            public void onFailure(Call<RestaurantArray> call, Throwable t) {
                callBack.onFailure(new Error(t.getMessage()));
            }
        });
    }

    public void getAllCities(final UIThreadCallBack callBack){

        callBack.startProgressIndicator();
        networkService.getCities().enqueue(new Callback<CityArray>() {
            @Override
            public void onResponse(Call<CityArray> call, Response<CityArray> response) {
                callBack.stopProgressIndicator();
                callBack.onResult(response.body());
            }

            @Override
            public void onFailure(Call<CityArray> call, Throwable t) {
                callBack.onFailure(new Error(t.getMessage()));
            }
        });

    }

    public void getRestaurant(String id, final UIThreadCallBack callback){
       callback.startProgressIndicator();
       networkService.getRestaurant(id).enqueue(new Callback<RestaurantDetail>() {
           @Override
           public void onResponse(Call<RestaurantDetail> call, Response<RestaurantDetail> response) {
               callback.stopProgressIndicator();
               callback.onResult(response.body());
           }

           @Override
           public void onFailure(Call<RestaurantDetail> call, Throwable t) {
               callback.onFailure(new Error(t.getMessage()));
           }
       });
    }

}
