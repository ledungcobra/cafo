package com.ledungcobra.cafo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ledungcobra.cafo.database.Repository;
import com.ledungcobra.cafo.network.RestaurantService;

public class CallApi extends AppCompatActivity {

    final String TAG = "CALL_API";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_api2);

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://cafo-api.herokuapp.com/")
//                .addConverterFactory(GsonConverterFactory.create(
//                        new GsonBuilder()
//                                .create()))
//                .build();
//
//        UserService userService = retrofit.create(UserService.class);

//        final UserApiHandler user = new UserApiHandler();
//        user.signIn("lunxx","xxxxxxx").observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                if(s!=null){
//                    Log.d(TAG, "onChanged: "+s);
//                    ArrayList<FoodOrderItem> orders = new ArrayList<>();
//                    Collections.addAll(orders,new FoodOrderItem("5fbd3704c2105b4094990583",29));
//                    user.order("5fbd3704c2105b4094990582","12","10",
//                            orders
//                    ).enqueue(new Callback<OrderResponse>() {
//                        @Override
//                        public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
//                            Log.d(TAG, "onResponse: "+response.body());
//                        }
//
//                        @Override
//                        public void onFailure(Call<OrderResponse> call, Throwable t) {
//                            Log.d(TAG, "onFailure: "+t);
//                        }
//                    });
//                }else{
//                    Log.d(TAG, "Unauthorized");
//                }
//            }
//        });

        Repository repository = Repository.getInstance();
        RestaurantService service = repository.getRestaurantService();
//        service.getRestaurant("5fbd3704c2105b4094990582").enqueue(new Callback<RestaurantDetail>() {
//            @Override
//            public void onResponse(Call<RestaurantDetail> call, Response<RestaurantDetail> response) {
//                Log.d(TAG, "onResponse: "+response.body());
//            }
//
//            @Override
//            public void onFailure(Call<RestaurantDetail> call, Throwable t) {
//                Log.d(TAG, "onFailure: "+t);
//            }
//        });

       


    }

}