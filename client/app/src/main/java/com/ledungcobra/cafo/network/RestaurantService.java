package com.ledungcobra.cafo.network;

import com.ledungcobra.cafo.models.city.City;
import com.ledungcobra.cafo.models.menu_category_new.MenuCategory;
import com.ledungcobra.cafo.models.restaurant_detail_new.RestaurantDetail;
import com.ledungcobra.cafo.models.restaurants_new.BriefRestaurantInfo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface RestaurantService {



    @GET("cities")
    Call<ArrayList<City>> getCities();
    //DONE
    @GET("restaurants/id/{restaurant_id}")
    Call<RestaurantDetail> getRestaurant(@Path("restaurant_id") String id);

    //DONE
    @GET("restaurants")
    Call<ArrayList<BriefRestaurantInfo>> getRestaurants(@Query("page") int page, @Query("limit") int limit);


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
