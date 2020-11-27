package com.ledungcobra.cafo.network;

import com.google.gson.JsonObject;
import com.ledungcobra.cafo.models.user.UserInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService  {

    @POST("auth/signup")
    Call<Object> signUp(@Body JsonObject body);

    @POST("auth/signin")
    Call<UserInfo> signIn(@Body JsonObject body);


}
