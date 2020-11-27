package com.ledungcobra.cafo.database;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ledungcobra.cafo.models.user.UserInfo;
import com.ledungcobra.cafo.network.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserApiHandler {
    private UserService userService;
    private final String BASE_URL = "https://cafo-api.herokuapp.com/";
    private final MutableLiveData<UserInfo> userInfo = new MutableLiveData<>();
    private final MutableLiveData<Boolean> regSuccess = new MutableLiveData<>(false);

    public UserApiHandler(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder()
                                .create()))
                .build();
        userService = retrofit.create(UserService.class);
    }

    public LiveData<UserInfo> signIn(final String username, String password){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username",username);
        jsonObject.addProperty("password",password);
        Call<UserInfo> call = userService.signIn(jsonObject);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                userInfo.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {

            }
        });
        return userInfo;
    }

    public LiveData<Boolean> signUp(final String username,String password,String email,String roles){
        JsonObject body = new JsonObject();
        body.addProperty("username",username);
        body.addProperty("password",password);
        body.addProperty("email",email);
        body.addProperty("roles",roles);

        Call<Object> call = userService.signUp(body);
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
}

