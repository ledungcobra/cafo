package com.ledungcobra.cafo.service;

import android.app.Application;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.GsonBuilder;
import com.ledungcobra.cafo.dao.TrackingRestaurantDao;
import com.ledungcobra.cafo.models.city.City;
import com.ledungcobra.cafo.models.restaurant_detail_new.RestaurantDetail;
import com.ledungcobra.cafo.models.restaurants_new.BriefRestaurantInfo;
import com.ledungcobra.cafo.models.user.TrackingRestaurant;
import com.ledungcobra.cafo.network.RestaurantService;
import com.ledungcobra.cafo.ui_calllback.UIThreadCallBack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {
    private TrackingRestaurantDao trackingRestaurantDao;
    private LiveData<List<TrackingRestaurant>> trackingRestaurants;

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

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(80, TimeUnit.SECONDS)
                .readTimeout(80, TimeUnit.SECONDS)
                .writeTimeout(80, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder().create()))
                .client(okHttpClient)
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
                } else {
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
                if (response.code() == 200) {

                    cites.setValue(response.body());

                } else {
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

                if (response.code() == 200) {
                    callback.onResult(response.body());
                } else {
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

    public void initDb(Application app) {
        TrackingRestaurantRoomDatabase db = TrackingRestaurantRoomDatabase.getDatabase(app);
        this.trackingRestaurantDao = db.trackingRestaurantDao();
        trackingRestaurants = trackingRestaurantDao.getAllTrackingRestaurants();
    }

    public LiveData<List<TrackingRestaurant>> getAllTrackingRestaurants() {
        return trackingRestaurants;
    }

    public void insert(final TrackingRestaurant restaurant) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                trackingRestaurantDao.insert(restaurant);
            }
        }).start();
    }

    public LiveData<ArrayList<RestaurantDetail>> searchRestaurant(String searchKeyword, int page, int limit, final AppCompatActivity activity) {
        final MutableLiveData<ArrayList<RestaurantDetail>> result = new MutableLiveData<>(new ArrayList<RestaurantDetail>());

        restaurantService.searchRestaurant(searchKeyword, page, limit, "5fbd364afe0a1616a91994bb").enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, final Response<List<String>> response) {
                if (response.code() == 200) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            final ArrayList<RestaurantDetail> list = new ArrayList<>();
                            for (String resID : response.body()) {
                                try {
                                    Log.d("SEARCHING", "run: "+resID);

                                    Response<RestaurantDetail> resDetail= restaurantService.getRestaurant(resID).execute();

                                    if(resDetail.code() == 200){
                                        list.add(resDetail.body());
                                    }else{
                                        return;
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("SEARCHING", "run: "+list.size());
                                    result.setValue(list);
                                }
                            });
                        }
                    }).start();


                } else {
                    result.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                result.setValue(null);
            }
        });
        return result;
    }
}
