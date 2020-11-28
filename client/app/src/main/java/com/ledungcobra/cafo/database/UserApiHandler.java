package com.ledungcobra.cafo.database;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.GsonBuilder;
import com.ledungcobra.cafo.models.order.FoodOrderItem;
import com.ledungcobra.cafo.models.user.UserInfo;
import com.ledungcobra.cafo.models.order.OrderResponse;
import com.ledungcobra.cafo.network.UserService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserApiHandler {
    private UserService userService;
    private final String BASE_URL = "https://cafo-api.herokuapp.com/";
    private final MutableLiveData<Boolean> regSuccess = new MutableLiveData<>(false);
    private final MutableLiveData<String> userAccessToken = new MutableLiveData<>(null);
    public UserApiHandler(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder()
                                .create()))
                .build();
        userService = retrofit.create(UserService.class);
    }

    public LiveData<String> signIn(final String username, String password){

        Call<UserInfo> call = userService.signIn(username,password);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                userAccessToken.setValue(response.body() != null ? response.body().getAccessToken() : null);
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {

            }
        });
        return userAccessToken;
    }

    public LiveData<Boolean> signUp(final String username,String password,String email,String roles,
                                    String phoneNumber){


        Call<Object> call = userService.signUp(username,password,email,roles,phoneNumber);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                regSuccess.setValue(true);
                Log.d("Success", "Success");
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                regSuccess.setValue(false);
                Log.d("DEBUG_LOGIN", "Fail");
            }
        });
        return regSuccess;
    }

    public UserService getUserService(){
        return this.userService;
    }


    public Call<OrderResponse> order(String restaurantID,String latitude,String longitude,
                                ArrayList<FoodOrderItem> listOrderItems
                              ){
        OrderInfo orderInfo = new OrderInfo(restaurantID,new UserPos(latitude,longitude),listOrderItems);
        return userService.order(orderInfo,userAccessToken.getValue());

    }


}

