package com.ledungcobra.cafo.database;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.GsonBuilder;
import com.ledungcobra.cafo.models.order.FoodOrderItem;
import com.ledungcobra.cafo.models.order.shipper.DetailOrderResponse;
import com.ledungcobra.cafo.models.order.customer.OrderResponse;
import com.ledungcobra.cafo.models.user.DetailUserInfo;
import com.ledungcobra.cafo.models.user.UserInfo;
import com.ledungcobra.cafo.models.user.UserLogin;
import com.ledungcobra.cafo.network.UserService;
import com.ledungcobra.cafo.ui_calllback.UIThreadCallBack;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserApiHandler {
    private UserService userService;
    private static UserApiHandler INSTANCE = null;
    private final String BASE_URL = "https://cafo-api.herokuapp.com/";
    private final MutableLiveData<String> userAccessToken = new MutableLiveData<>(null);

    public MutableLiveData<String> getUserAccessToken() {
        return userAccessToken;
    }

    private UserApiHandler() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder()
                                .create()))
                .build();
        userService = retrofit.create(UserService.class);
    }

    public static UserApiHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserApiHandler();
        }
        return INSTANCE;
    }

    public void setUserAccessToken(String token){
        userAccessToken.setValue(token);
    }

    public void signIn(final String username, String password, final UIThreadCallBack<UserInfo,Error> callback) {

        callback.startProgressIndicator();
        Call<UserInfo> call = userService.signIn(username, password);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if(response.code() == 200){
                    Log.d("CALL_API", "onResponse: "+response);
                    userAccessToken.setValue(response.body() != null ? response.body().getAccessToken() : null);
                    callback.stopProgressIndicator();
                    callback.onResult(response.body());
                }

            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                callback.stopProgressIndicator();
                callback.onFailure(new Error(t.getMessage()));
            }
        });
    }

    public void signUp( String username, String password, String email, List<String> roles,
                                    String phoneNumber,final UIThreadCallBack<Void,Error> callback) {
        callback.startProgressIndicator();
        Call<Object> call = userService.signUp(new UserLogin(username,
                password,email, (ArrayList<String>) roles,phoneNumber));
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d("CALL_API", "onResponse: "+response);
                callback.onResult(null);
                callback.stopProgressIndicator();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                callback.onFailure(new Error(t.getMessage()));
                callback.stopProgressIndicator();
            }
        });
    }

    public UserService getUserService() {
        return this.userService;
    }


    public Call<OrderResponse> order(String restaurantID, double latitude, double longitude,
                                     ArrayList<FoodOrderItem> listOrderItems
    ) {
        OrderInfo orderInfo = new OrderInfo(restaurantID, new UserPos(latitude, longitude), listOrderItems);
        return userService.order(orderInfo, userAccessToken.getValue());

    }

    public void fetchFiveOrdersNearCustomerByShipper(double latitude, double longitude, final UIThreadCallBack<List<DetailOrderResponse>, Error> callback) {
        callback.startProgressIndicator();
        userService.getCustomerOrders(userAccessToken.getValue(), longitude, latitude)
                .enqueue(new Callback<List<DetailOrderResponse>>() {
                             @Override
                             public void onResponse(Call<List<DetailOrderResponse>> call, Response<List<DetailOrderResponse>> response) {
                                 callback.stopProgressIndicator();
                                 callback.onResult(response.body());
                             }

                             @Override
                             public void onFailure(Call<List<DetailOrderResponse>> call, Throwable t) {
                                 callback.onFailure(new Error(t.getMessage()));
                             }
                         }
                );
    }

    public void acceptAnOrder(String id, final UIThreadCallBack<String, Error> callback) {
        callback.startProgressIndicator();
        userService.shipperAcceptAnOrder(userAccessToken.getValue(), id).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                callback.stopProgressIndicator();
                assert response.body() != null;
                callback.onResult(response.body().getMessage());
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                callback.stopProgressIndicator();
                callback.onFailure(new Error(t.getMessage()));
            }
        });
    }

    public void cancelOrderByCustomer(String id, final UIThreadCallBack<OrderResponse, Error> callback) {
        callback.startProgressIndicator();
        userService.cancelOrderByCustomer(userAccessToken.getValue(), id)
                .enqueue(new Callback<OrderResponse>() {
                    @Override
                    public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                        callback.stopProgressIndicator();
                        callback.onResult(response.body());
                    }

                    @Override
                    public void onFailure(Call<OrderResponse> call, Throwable t) {
                        callback.stopProgressIndicator();
                        callback.onFailure(new Error(t.getMessage()));
                    }
                });
    }

    public void getOrdersByCustomer(final UIThreadCallBack<ArrayList<DetailOrderResponse>, Error> callback) {
        callback.startProgressIndicator();
        userService.getMyOrders(userAccessToken.getValue())
                .enqueue(new Callback<ArrayList<DetailOrderResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<DetailOrderResponse>> call, Response<ArrayList<DetailOrderResponse>> response) {
                        callback.stopProgressIndicator();
                        callback.onResult(response.body());
                    }

                    @Override
                    public void onFailure(Call<ArrayList<DetailOrderResponse>> call, Throwable t) {
                        callback.stopProgressIndicator();
                        callback.onFailure(new Error(t.getMessage()));
                    }
                });
    }

    public void getOrderByUser(String id, final UIThreadCallBack<DetailOrderResponse, Error> callback) {
        callback.startProgressIndicator();
        userService.getOrder(userAccessToken.getValue(), id)
                .enqueue(new Callback<DetailOrderResponse>() {
                             @Override
                             public void onResponse(Call<DetailOrderResponse> call, Response<DetailOrderResponse> response) {
                                 callback.stopProgressIndicator();
                                 callback.onResult(response.body());
                             }

                             @Override
                             public void onFailure(Call<DetailOrderResponse> call, Throwable t) {
                                 callback.stopProgressIndicator();
                                 callback.onFailure(new Error(t.getMessage()));
                             }
                         }
                );
    }

    public void getUser(final UIThreadCallBack<DetailUserInfo,Error> callBack){
        callBack.startProgressIndicator();
        userService.getUser(userAccessToken.getValue()).enqueue(
                new Callback<DetailUserInfo>() {
                    @Override
                    public void onResponse(Call<DetailUserInfo> call, Response<DetailUserInfo> response) {
                        callBack.stopProgressIndicator();
                        callBack.onResult(response.body());
                        }

                    @Override
                    public void onFailure(Call<DetailUserInfo> call, Throwable t) {
                        callBack.stopProgressIndicator();
                        callBack.onFailure(new Error(t.getMessage()));
                    }
                }
        );

    }

}

