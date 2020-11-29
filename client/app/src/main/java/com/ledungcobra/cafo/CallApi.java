package com.ledungcobra.cafo;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ledungcobra.cafo.database.UserApiHandler;
import com.ledungcobra.cafo.models.order.shipper.DetailOrderResponse;
import com.ledungcobra.cafo.ui_calllback.UIThreadCallBack;

import java.util.List;

public class CallApi extends AppCompatActivity {

    final String TAG = "CALL_API";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_api2);

        UserApiHandler.getInstance().setUserAccessToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVmYzMzMTQyZDNhOWMyMDAxNzdlNGI0MSIsImlhdCI6MTYwNjYyNzY3NSwiZXhwIjoxNjA3MjMyNDc1fQ.IeCJMLre7o2MgFwIyFgcJrlQ-9Afxdkw3uWy3Arn1fc");
        UserApiHandler.getInstance().fetchFiveOrdersNearCustomerByShipper(10.8899,
                106.999, new UIThreadCallBack<List<DetailOrderResponse>, Error>() {
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

                    }
                }
        );

    }

}