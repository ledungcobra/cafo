package com.ledungcobra.cafo.network;

import com.ledungcobra.cafo.models.city.City;
import com.ledungcobra.cafo.models.menu_category_new.MenuCategory;
import com.ledungcobra.cafo.models.restaurant_detail.RestaurantWrapper;
import com.ledungcobra.cafo.models.restaurant_detail_new.RestaurantDetail;
import com.ledungcobra.cafo.models.restaurants.RestaurantArray;
import com.ledungcobra.cafo.models.restaurants_new.BriefRestaurantInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface RestaurantService {
    @Deprecated
    @GET("restaurants")
    Call<RestaurantArray> getRestaurants();

    @Deprecated
    @GET("restaurants/id/{restaurant_id}")
    Call<RestaurantWrapper> getRestaurant(@Path("restaurant_id") String id);



    @GET("cities")
    Call<List<City>> getCities();
    //DONE
    @GET("restaurants/id/{restaurant_id}")
    Call<RestaurantDetail> getRestaurant_(@Path("restaurant_id") String id);

    //DONE
    @GET("restaurants")
    Call<List<BriefRestaurantInfo>> getRestaurants(@Query("page") int page, @Query("limit") int limit);


    //DONE
    @GET("menus")
    Call<List<MenuCategory>> getMenus(@Query("res_id") String restaurantId, @Query("limit") int limit);

    //DONE!!
    @POST("search")
    @FormUrlEncoded
    Call<List<String>> searchRestaurant(@Field("keyword") String keyword,
                                        @Field("page") int page,
                                        @Field("limit") int limit,
                                        @Field("city_id")String cityId
                                        );

    //DONE!!
    @POST("restaurants")
    @FormUrlEncoded
    Call<List<BriefRestaurantInfo>> findRestaurantsByListRestaurantID(@Field("ids") List<String> ids);





}
