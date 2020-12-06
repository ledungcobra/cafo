package com.ledungcobra.cafo.network;

import com.ledungcobra.cafo.models.order.OrderInfo;
import com.ledungcobra.cafo.models.order.customer.OrderResponse;
import com.ledungcobra.cafo.models.order.shipper.DetailOrderResponse;
import com.ledungcobra.cafo.models.user.DetailUserInfo;
import com.ledungcobra.cafo.models.user.UserInfo;
import com.ledungcobra.cafo.models.user.UserLogin;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService  {

    @GET("user")
    Call <DetailUserInfo> getUser(@Header("x-cafo-client-access-token") String token);

    @POST("auth/signup")
    Call<Object> signUp(
            @Body UserLogin userLogin
            );



    @POST("auth/signin")
    @FormUrlEncoded
    Call<UserInfo> signIn(@Field("username") String username,
                        @Field("password") String password);

    @POST("order")
    Call<OrderResponse> order(@Body OrderInfo body, @Header("x-cafo-client-access-token") String token);

    @GET("order")
    Call<ArrayList<DetailOrderResponse>> getMyOrders(@Header("x-cafo-client-access-token") String token);

    @GET("order/id/{id}")
    Call<DetailOrderResponse> getOrder(@Header("x-cafo-client-access-token") String token, @Path("id") String id);

    @POST("order/cancel")
    @FormUrlEncoded
    Call<OrderResponse> cancelOrderByCustomer(@Header("x-cafo-client-access-token") String token, @Field("order_id") String orderID);

    @GET("shipper/search")
    Call<List<DetailOrderResponse>> getCustomerOrders(@Header("x-cafo-client-access-token") String token,
                                                      @Query("long") double longitude, @Query("lat") double latitude);

    @POST("shipper/get-order")
    @FormUrlEncoded
    Call<OrderResponse> shipperAcceptAnOrder(
        @Header("x-cafo-client-access-token")
        String token,
        @Field("order_id") String orderID
    );

    @GET("shipper/orders")
    Call<List<DetailOrderResponse>> getAcceptedOrdersByShipper(@Header("x-cafo-client-access-token") String token);


    
}
