package com.ledungcobra.cafo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.ledungcobra.cafo.database.Repository;
import com.ledungcobra.cafo.models.restaurant_detail.RestaurantDetail;
import com.ledungcobra.cafo.ui_calllback.UIThreadCallBack;

public class CallApi extends AppCompatActivity {
    public  String TAG = "CALLAPI";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_api);

        Repository repository = new Repository(getApplication());

        repository.getRestaurant("5f97a22be5123349e0a2784f",new UIThreadCallBack<RestaurantDetail,Error>() {

            @Override
            public void onResult(RestaurantDetail result) {
                Log.d(TAG, "onResult: "+result.toString());
            }

            @Override
            public void onFailure(Error error) {
                Log.d(TAG, "onFailure: "+error.getMessage());
            }

            @Override
            public void stopProgressIndicator() {
                Log.d(TAG, "stopProgressIndicator: ");
            }

            @Override
            public void startProgressIndicator() {
                Log.d(TAG, "startProgressIndicator: ");
            }
        });






    }
}