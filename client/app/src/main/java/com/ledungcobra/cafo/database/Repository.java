package com.ledungcobra.cafo.database;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.GsonBuilder;
import com.ledungcobra.cafo.models.city.City;
import com.ledungcobra.cafo.models.restaurant_detail_new.RestaurantDetail;
import com.ledungcobra.cafo.models.restaurants_new.BriefRestaurantInfo;
import com.ledungcobra.cafo.network.RestaurantService;
import com.ledungcobra.cafo.ui_calllback.UIThreadCallBack;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {

    private static final String BASE_URL = "https://cafo-api.herokuapp.com/";
    private MutableLiveData<ArrayList<BriefRestaurantInfo>> listRestaurants = new MutableLiveData<>(new ArrayList<BriefRestaurantInfo>());
    private static Repository INSTANCE;
    private RestaurantService restaurantService;
    private MutableLiveData<ArrayList<City>> cites = new MutableLiveData<>(new ArrayList<City>());

    public static Repository getInstance() {
        if (INSTANCE == null) {
            synchronized (Repository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Repository();
                }
            }
        }

        return INSTANCE;
    }

    private Repository() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder()
                                .create()))
                .build();

        restaurantService = retrofit.create(RestaurantService.class);

    }

    public LiveData<ArrayList<BriefRestaurantInfo>> fetchAllRestaurants(int page, int limit, final UIThreadCallBack<ArrayList<BriefRestaurantInfo>, Error> callback) {


        callback.startProgressIndicator();
        restaurantService.getRestaurants(page, limit).enqueue(new Callback<ArrayList<BriefRestaurantInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<BriefRestaurantInfo>> call, Response<ArrayList<BriefRestaurantInfo>> response) {
                callback.stopProgressIndicator();
                if (response.code() == 200) {
                    listRestaurants.setValue(response.body());
                    callback.onResult(response.body());
                }else{
                    callback.onFailure(new Error("Something went wrong"));
                }
            }


            @Override
            public void onFailure(Call<ArrayList<BriefRestaurantInfo>> call, Throwable t) {
                callback.onFailure(new Error(t.getMessage()));
            }
        });


        return listRestaurants;
    }

    public LiveData<ArrayList<BriefRestaurantInfo>> getAllRestaurants() {
        return this.listRestaurants;
    }

    public LiveData<ArrayList<City>> fetchAllCities(final UIThreadCallBack<ArrayList<City>, Error> callback) {

        callback.startProgressIndicator();
        restaurantService.getCities().enqueue(new Callback<ArrayList<City>>() {
            @Override
            public void onResponse(Call<ArrayList<City>> call, Response<ArrayList<City>> response) {
                callback.stopProgressIndicator();
                if(response.code() == 200){

                    cites.setValue(response.body());

                }else{
                    callback.onFailure(new Error("Something went wrong"));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<City>> call, Throwable t) {
                callback.onFailure(new Error(t.getMessage()));
            }
        });

        return cites;

    }

    public LiveData<ArrayList<City>> getCityArray() {
        return cites;
    }

    public void getRestaurant(String id, final UIThreadCallBack<RestaurantDetail, Error> callback) {
        callback.startProgressIndicator();
        Log.d("CALL_API", "getRestaurant: " + id);
        restaurantService.getRestaurant(id).enqueue(new Callback<RestaurantDetail>() {
            @Override
            public void onResponse(Call<RestaurantDetail> call, Response<RestaurantDetail> response) {
                callback.stopProgressIndicator();

                if(response.code() == 200){
                    callback.onResult(response.body());
                }else{
                    callback.onFailure(new Error("Something went wrong"));
                }
            }


            @Override
            public void onFailure(Call<RestaurantDetail> call, Throwable t) {
                callback.onFailure(new Error(t.getMessage()));
            }
        });

    }

    public RestaurantService getRestaurantService() {
        return this.restaurantService;
    }


}
