package com.ledungcobra.cafo.database;

import com.google.gson.GsonBuilder;
import com.ledungcobra.cafo.models.city.CityArray;
import com.ledungcobra.cafo.models.common.Restaurant;
import com.ledungcobra.cafo.models.restaurant_detail.RestaurantWrapper;
import com.ledungcobra.cafo.models.restaurants.BriefRestaurantInfo;
import com.ledungcobra.cafo.models.restaurants.RestaurantArray;
import com.ledungcobra.cafo.network.NetworkService;
import com.ledungcobra.cafo.ui_calllback.UIThreadCallBack;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {

    private static final String BASE_URL = "http://cafo-dev-api.herokuapp.com/";
    private RestaurantArray arrayRestaurants;
    private static Repository INSTANCE;

    private NetworkService networkService;
    public static Repository getInstance(){
        if(INSTANCE == null){
            synchronized (Repository.class){
                if(INSTANCE == null){
                    INSTANCE = new Repository();
                }
            }
        }

        return INSTANCE;
    }
    private Repository(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder()
                        .create()))
                .build();
        networkService = retrofit.create(NetworkService.class);

    }

    public void getAllRestaurants(final UIThreadCallBack<List<BriefRestaurantInfo>, Error> callBack){

        if(arrayRestaurants == null){

            callBack.startProgressIndicator();
            networkService.getRestaurants().enqueue(new Callback<RestaurantArray>() {
                @Override
                public void onResponse(Call<RestaurantArray> call, Response<RestaurantArray> response) {
                    callBack.stopProgressIndicator();
                    arrayRestaurants = response.body();
                    callBack.onResult(response.body().getRestaurants());
                }

                @Override
                public void onFailure(Call<RestaurantArray> call, Throwable t) {
                    callBack.onFailure(new Error(t.getMessage()));
                }
            });

        }else{
            callBack.onResult(arrayRestaurants.getRestaurants());
        }
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

    public void getRestaurant(String id, final UIThreadCallBack<Restaurant,Error> callback){
       callback.startProgressIndicator();
       networkService.getRestaurant(id).enqueue(new Callback<RestaurantWrapper>() {
           @Override
           public void onResponse(Call<RestaurantWrapper> call, Response<RestaurantWrapper> response) {
               callback.stopProgressIndicator();
               callback.onResult(response.body().getRestaurant());
           }

           @Override
           public void onFailure(Call<RestaurantWrapper> call, Throwable t) {
               callback.onFailure(new Error(t.getMessage()));
           }
       });
    }

    public List<BriefRestaurantInfo> getAllRestaurants(){
        return arrayRestaurants.getRestaurants();
    }

}
