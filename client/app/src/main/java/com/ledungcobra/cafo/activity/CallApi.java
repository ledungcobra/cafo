package com.ledungcobra.cafo.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.models.order.shipper.DetailOrderResponse;
import com.ledungcobra.cafo.models.user.UserInfo;
import com.ledungcobra.cafo.service.UserApiHandler;
import com.ledungcobra.cafo.ui_calllback.UIThreadCallBack;

import java.util.List;

public class CallApi extends AppCompatActivity {

    final String TAG = "CALL_API";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_api2);
        UserApiHandler.getInstance().signIn("shipper0123", "a", new UIThreadCallBack<UserInfo, Error>() {
            @Override
            public void stopProgressIndicator() {

            }

            @Override
            public void startProgressIndicator() {

            }

            @Override
            public void onResult(UserInfo result) {

                UserApiHandler.getInstance().getAcceptedOrdersByShipper(new UIThreadCallBack<List<DetailOrderResponse>, Error>() {
                    @Override
                    public void stopProgressIndicator() {

                    }

                    @Override
                    public void startProgressIndicator() {

                    }

                    @Override
                    public void onResult(List<DetailOrderResponse> result) {
                        Log.d(TAG, "onResult: "+result);
                    }

                    @Override
                    public void onFailure(Error error) {
                        Log.d(TAG, "onFailure: "+error);
                    }
                });

            }

            @Override
            public void onFailure(Error error) {

            }
        });

    }

}