package com.ledungcobra.cafo.network;

import com.ledungcobra.cafo.database.OrderInfo;
import com.ledungcobra.cafo.models.order.OrderDetail;
import com.ledungcobra.cafo.models.order.OrderResponse;
import com.ledungcobra.cafo.models.user.UserInfo;

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

    @POST("auth/signup")
    @FormUrlEncoded
    Call<Object> signUp(@Field("username")String username,
                        @Field("password") String password,
                        @Field("email") String email,
                        @Field("roles")String roles,
                        @Field("phone_number") String phoneNumber);

    @POST("auth/signin")
    @FormUrlEncoded
    Call<UserInfo> signIn(@Field("username") String username,
                        @Field("password") String password);

    @POST("order")
    Call<OrderResponse> order(@Body OrderInfo body, @Header("x-cafo-client-access-token") String token);

    @GET("order/get")
    Call<List<OrderDetail>> getMyOrders(@Header("x-cafo-client-access-token") String token);

    @GET("order/id/{id}")
    Call<OrderDetail> getOrder(@Header("x-cafo-client-access-token") String token,@Path("id") String id);

    //OBJECT TODO:
    @POST("order/cancel")
    @FormUrlEncoded
    Call<Object> cancelOrderByCustomer(@Header("x-cafo-client-access-token") String token, @Field("order_id") String orderID);

    @GET("shipper/search")
    Call<OrderDetail> getCustomerOrders(@Header("x-cafo-client-access-token") String token,
                                        @Query("long") double longitude,@Query("lat") double latitude);

    @POST("shipper/get-order")
    @FormUrlEncoded
    Call<OrderResponse> shipperAcceptAnOrder(
        @Header("x-cafo-client-access-token")
        String token,
        @Field("order_id") String orderID
    );

    
}
