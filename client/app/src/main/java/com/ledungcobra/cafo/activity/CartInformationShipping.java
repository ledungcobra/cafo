package com.ledungcobra.cafo.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.service.UserApiHandler;
import com.ledungcobra.cafo.models.common_new.CartItem;
import com.ledungcobra.cafo.models.order.FoodOrderItem;
import com.ledungcobra.cafo.models.order.customer.OrderResponse;
import com.ledungcobra.cafo.models.user.DetailUserInfo;
import com.ledungcobra.cafo.ui_calllback.UIThreadCallBack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class CartInformationShipping extends AppCompatActivity {


    private LocationManager locationManager;
    private  EditText
            edtFullname,
            edtAddress,
            edtPhoneNumber,
            edtNote,
            edtCode;
    private  TextView
            tvCostFood,
            tvShippingFee,
            tvTotalCost;
    protected Button btnOrderShip;

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

    String resID;
    private static final int REQUEST_LOCATION_CODE = 12345;
    ArrayList<FoodOrderItem> foodOrderItems = new ArrayList<>();

    //Data
    ArrayList<CartItem> listCartShop;
    int foodCost = 0;
    int shippingFeeCost = 0;
    int totalCost = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_infomation_shipping);

        initUI();

        //GET DATA
        Intent intent = getIntent();
        listCartShop = (ArrayList<CartItem>) intent.getSerializableExtra("Info");
        resID = intent.getStringExtra(getString(R.string.res_id));
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //CALC TOTAL COST FOR EVERY ORDER
        if (listCartShop != null && listCartShop.size() > 0) {
            for (CartItem cartShop: listCartShop){
                foodCost += cartShop.getFood().getPrice().getValue() * cartShop.getNumber();
            }
        }
        //TODO: Move this to make formatter for currency
        tvCostFood.setText(
                String.format("%,d", foodCost) + getResources().getString(R.string.currency));

        calcShippingFee();
        tvShippingFee.setText(
                String.format("%,d", shippingFeeCost) + getResources().getString(R.string.currency));

        calcTotalCost();
        tvTotalCost.setText(
                String.format("%,d", totalCost) + getResources().getString(R.string.currency));


        if (ActivityCompat.checkSelfPermission(CartInformationShipping.this, Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CartInformationShipping.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CartInformationShipping.this,new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION_CODE);

        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 5, locationListener);
            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(loc != null) edtAddress.setText(getAddress(this,loc.getLatitude(),loc.getLongitude()));

        }

        //Get user information
        getUserInformation();

    }

    private void getUserInformation(){
        UserApiHandler.getInstance().getUser(new UIThreadCallBack<DetailUserInfo, Error>() {
            @Override
            public void stopProgressIndicator() {

            }

            @Override
            public void startProgressIndicator() {

            }

            @Override
            public void onResult(DetailUserInfo result) {
                edtFullname.setText(result.getUsername());
                edtPhoneNumber.setText(result.getPhoneNumber());
            }

            @Override
            public void onFailure(Error error) {

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
//                doOrder();
                Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(loc != null) edtAddress.setText(getAddress(this,loc.getLatitude(),loc.getLongitude()));

            }else{
                //User refuse to location
                Toast.makeText(this,"Cannot get your location",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getAddress(Context context, double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);

            String add = obj.getAddressLine(0);


            return add;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void calcShippingFee() {
        if (foodCost > 0) {
            shippingFeeCost = 20000;
        }
        //TODO: Special shipping codes
        if (edtCode.getText().toString().equals("FREE_SHIPPING_CODE")) {
            shippingFeeCost = 0;
        }
    }

    private void calcTotalCost() {
        totalCost = foodCost + shippingFeeCost;
    }

    private void initUI() {
        edtFullname = findViewById(R.id.editFullNameShip);
        edtAddress = findViewById(R.id.editAddressShip);
        edtPhoneNumber = findViewById(R.id.editPhone);
        edtNote = findViewById(R.id.editNote);
        edtCode = findViewById(R.id.editFreeShipCode);

        tvCostFood = findViewById(R.id.tvCostFoodShip);
        tvShippingFee = findViewById(R.id.tvFeeShip);
        tvTotalCost = findViewById(R.id.tvTotalCost);

        btnOrderShip = findViewById(R.id.btnOrderShip);
        btnOrderShip.setEnabled(false);

        //Add UI Listener
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                //validate input
                for (EditText edt  : new EditText[] {edtFullname, edtAddress, edtPhoneNumber}) {
                    if (edt.getText().toString().trim().isEmpty()) {
                        btnOrderShip.setEnabled(false);
                        return;
                    }
                }
                btnOrderShip.setEnabled(true);
            }
        };

        edtFullname.addTextChangedListener(textWatcher);
        edtAddress.addTextChangedListener(textWatcher);
        edtPhoneNumber.addTextChangedListener(textWatcher);

        btnOrderShip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = edtFullname.getText().toString();
                String address = edtAddress.getText().toString();
                String phone = edtPhoneNumber.getText().toString();
                String notes = edtNote.getText().toString();

                String message = new String(
                        "Your name: "+ fullname
                                +"\nAddress: "+ address
                                +"\nPhone: " + phone
                                +"\nNotes: " + notes
                                +"\n\nTotal Cost: " + tvTotalCost.getText().toString()
                        //Too lazy to do format number again :p
                );

                AlertDialog.Builder myBuilder =
                        new AlertDialog.Builder(CartInformationShipping.this);
                myBuilder.setTitle("Confirm Order")
                        .setMessage(message)
                        .setPositiveButton("Confirm Order", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichOne) {
                                //TODO: Call API to place order
                                for (CartItem cartItem : listCartShop) {
                                    foodOrderItems.add(new FoodOrderItem(cartItem.getFood().getId(), cartItem.getNumber()));
                                }

                                doOrder();
                                dialog.dismiss();

                                setResult(RESULT_OK,new Intent());
                                CartInformationShipping.this.finish();
                            }})
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }) //setNegativeButton
                        .show();
            }
        });

    }

}