package com.ledungcobra.cafo.network;

import com.ledungcobra.cafo.models.city.CityArray;
import com.ledungcobra.cafo.models.restaurant_detail.RestaurantWrapper;
import com.ledungcobra.cafo.models.restaurants.RestaurantArray;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface NetworkService {

    @GET("restaurants")
    Call<RestaurantArray> getRestaurants();

    @GET("cities")
    Call<CityArray> getCities();

    @GET("restaurants/id/{restaurant_id}")
    Call<RestaurantWrapper> getRestaurant(@Path("restaurant_id") String id);


}
