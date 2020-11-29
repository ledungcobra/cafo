package com.ledungcobra.cafo.view_adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.database.UserApiHandler;
import com.ledungcobra.cafo.models.common_new.CartItem;
import com.ledungcobra.cafo.models.order.FoodOrderItem;
import com.ledungcobra.cafo.models.order.customer.OrderResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class CartInformationShipping extends AppCompatActivity {

    Button btnOrderShip;
    String resID;
    private static final int REQUEST_LOCATION_CODE = 12345;
    LocationManager locationManager;
    ArrayList<FoodOrderItem> foodOrderItems = new ArrayList<>();

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_infomation_shipping);

        btnOrderShip = findViewById(R.id.btnOrderShip);
        Intent intent = getIntent();
        final List<CartItem> cartItems = (List<CartItem>) intent.getSerializableExtra(getString(R.string.cart_items));

        resID = intent.getStringExtra(getString(R.string.res_id));

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        btnOrderShip.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                //TODO: CHECK
                for (CartItem cartItem : cartItems) {
                    foodOrderItems.add(new FoodOrderItem(cartItem.getFood().getId(), cartItem.getNumber()));
                }

                if (ActivityCompat.checkSelfPermission(CartInformationShipping.this, Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CartInformationShipping.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
                    Log.d("CALL_API", "LOCATION PERMISION");
                    ActivityCompat.requestPermissions(CartInformationShipping.this,new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_LOCATION_CODE);

                }else{
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 5, locationListener);
                    doOrder();
                }


            }

        });

    }

    @SuppressLint("MissingPermission")
    private void doOrder(){
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            UserApiHandler.getInstance().order(resID,location.getLatitude(),location.getLongitude(),foodOrderItems)
                    .enqueue(new Callback<OrderResponse>() {
                        @Override
                        public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                            Log.d("CALL_API", "onResponse:"+response.body());
                        }

                        @Override
                        public void onFailure(Call<OrderResponse> call, Throwable t) {
                            Log.d("CALL_API", "onResponse:"+t.getMessage());
                        }
                    });
        }

    }



    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_LOCATION_CODE){
            if(grantResults.length == 2 && grantResults[0] == PERMISSION_GRANTED && grantResults[1] == PERMISSION_GRANTED){
                //Permission granted
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 5, locationListener);
                doOrder();

            }else{
                //User refuse to location
                Toast.makeText(this,"Cannot get your location",Toast.LENGTH_SHORT).show();
            }
        }
    }
}