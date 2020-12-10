package com.ledungcobra.cafo.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.GsonBuilder;
import com.google.maps.android.ui.IconGenerator;
import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.models.routing.Routing;
import com.ledungcobra.cafo.network.MapService;
import com.ledungcobra.cafo.service.UserApiHandler;
import com.ledungcobra.cafo.ui_calllback.UIThreadCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MapScreen extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    //VIEW
    private GoogleMap mMap;
    private LocationManager locationManager;
    private Button btnRecenter;

    //DATA
    private MutableLiveData<ArrayList<String>> listAddresses = new MutableLiveData<>(new ArrayList<String>());
    String TAG = "GOOGLE_MAP";
    private MutableLiveData<Location> userLocation = new MutableLiveData<>(null);
    private final int maxTimeLocUpdateMilis = 300;
    private LatLng locDest;
    private MutableLiveData<ArrayList<LatLng>> downloadedLocations = new MutableLiveData<>(null);
    private final int REQUEST_CODE = 9999;
    private boolean firstLoad = true;
    private boolean completedTheRouting = false;
    public static  final int CODE = 9099;
    private ActivityManager activityManager;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_screen);

        ActivityManager activityManager = (ActivityManager)getBaseContext().getSystemService (Context.ACTIVITY_SERVICE);

        ArrayAdapter<String> adapter
                = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listAddresses.getValue());


        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.edtTextField);
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getNewLocation(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.myMap);
        assert mapFragment != null;
        mapFragment.getMapAsync((OnMapReadyCallback) this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;

        }


        runIfHasPermission();

    }

    @SuppressLint("MissingPermission")
    private void runIfHasPermission() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, maxTimeLocUpdateMilis, 5, this);

        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("lat", 0);
        double long_ = intent.getDoubleExtra("long", 0);
        locDest = new LatLng(lat, long_);

        btnRecenter = findViewById(R.id.btnRecenter);
        btnRecenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userLocation.getValue() != null) {
                    moveCamera(userLocation.getValue().getLatitude(), userLocation.getValue().getLongitude(), "", false);
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.d("GOOGLE_MAP", "onMapClick: " + latLng);

            }
        });

        userLocation.observe(this, new Observer<Location>() {
            @Override
            public void onChanged(Location loc) {
                if (loc != null) {

                    if (firstLoad) {
                        downloadLocations(new LatLng(loc.getLatitude(), loc.getLongitude()), locDest);
                        renderRoute();

                    } else {
                        renderRoute();
                    }

                }
            }
        });

        downloadedLocations.observe(this, new Observer<ArrayList<LatLng>>() {
            @Override
            public void onChanged(ArrayList<LatLng> latLngs) {
                if (userLocation.getValue() != null) {
                    renderRoute();
                }
            }
        });

    }

    private void downloadLocations(LatLng userLocation, LatLng destLocation) {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.geoapify.com/")
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder()
                                .create()))
                .build();


        MapService mapService = retrofit.create(MapService.class);
        if (userLocation == null || destLocation == null) return;
        mapService.getRoute(userLocation.latitude + "," + userLocation.longitude + "|"
                        + destLocation.latitude + "," + destLocation.longitude,
                "drive", getString(R.string.api_geoapify))
                .enqueue(new Callback<Routing>() {
                             @Override
                             public void onResponse(Call<Routing> call, retrofit2.Response<Routing> response) {

                                 ArrayList<LatLng> locs = new ArrayList<>();
                                 try {
                                     Log.d(TAG, "onResponse: " + response.body().getFeatures().get(0).getGeometry().getCoordinates().size());
                                     Log.d(TAG, "onResponse: " + response.body().toString());
                                     for (List<Double> cord : response.body().getFeatures().get(0).getGeometry().getCoordinates().get(0)) {
                                         locs.add(new LatLng(cord.get(1), cord.get(0)));
                                     }

                                     downloadedLocations.setValue(locs);
                                     firstLoad = false;

                                 } catch (Exception e) {
                                     Log.d(TAG, "Error" + e);
                                 }

                             }

                             @Override
                             public void onFailure(Call<Routing> call, Throwable t) {
                                 Toast.makeText(MapScreen.this,getString(R.string.network_error),Toast.LENGTH_SHORT).show();
                             }
                         }
                );


    }

    public void renderRoute() {

        if (mMap != null) {
            mMap.clear();

        }else{
            return;
        }

        if (downloadedLocations.getValue() != null) {

            if (userLocation.getValue() != null) {
                moveCamera(userLocation.getValue().getLatitude(), userLocation.getValue().getLongitude(), "", true);
            }

            if (downloadedLocations.getValue().size() < 2) {
                return;
            }

            PolylineOptions options = new PolylineOptions();

            options.color(Color.parseColor("#CC0000FF"));
            options.width(10);
            options.visible(true);


            for (LatLng locRecorded : downloadedLocations.getValue()) {
                options.add(locRecorded);
            }


            mMap.addPolyline(options);

            if (userLocation.getValue() != null) {
                IconGenerator iconGen = new IconGenerator(this);
                iconGen.setBackground(getDrawable(R.drawable.driver));
                MarkerOptions markerOptions = new MarkerOptions().
                        icon(BitmapDescriptorFactory.fromBitmap(iconGen.makeIcon("")));

                markerOptions.position(new LatLng(userLocation.getValue().getLatitude(), userLocation.getValue().getLongitude()));

                mMap.addMarker(markerOptions);

                mMap.addMarker(new MarkerOptions().position(locDest).title(""));


            }



        }


    }

    public void getNewLocation(final String searchTerm) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET,
                        "https://ledung-google-map-scraping.herokuapp.com/?search=" + Uri.encode(searchTerm.toString()), null,
                        new Response.Listener<JSONArray>() {


                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    final double latitude = Double.parseDouble((String) (
                                            ((JSONObject) response.get(0)).get("lat")
                                    ));
                                    final double longitude = Double.parseDouble((String) (
                                            ((JSONObject) response.get(0)).get("long")
                                    ));

                                    moveCamera(latitude, longitude, searchTerm, true);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(jsonArrayRequest);


    }

    public void moveCamera(double latitude, double longitude, String title, boolean clear) {

        final LatLng pos = new LatLng(latitude, longitude);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));
        mMap.addMarker(new MarkerOptions()
                .position(pos)
                .title(title));

        if (clear) {
            mMap.clear();
        }


    }

    public static String getAddress(Context context, double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);

            String add = obj.getAddressLine(0);


            return add;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE &&
                grantResults.length == 2 &&
                grantResults[0] == PERMISSION_GRANTED &&
                grantResults[1] == PERMISSION_GRANTED) {
            runIfHasPermission();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        userLocation.setValue(location);

        Log.d("MEM_LEAKS", "onLocationChanged: ");
        if (location != null && locDest != null) {

            if (calcDistanceBetweenTwoLocationInKm(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(locDest.latitude, locDest.longitude)) < 0.09){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                Intent intent = getIntent();
                String orderID = intent.getStringExtra(getString(R.string.order_id));
                try{

                    if(orderID!=null){
                        UserApiHandler.getInstance().finishOrderByShipper(orderID, new UIThreadCallBack<Object, Error>() {
                            @Override
                            public void stopProgressIndicator() {

                            }

                            @Override
                            public void startProgressIndicator() {

                            }

                            @Override
                            public void onResult(Object result) {
                                Toast.makeText(MapScreen.this,getString(R.string.finish_order_successfully),Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                intent.putExtra(getString(R.string.result),getString(R.string.finish));
                                setResult(CODE,intent);
                                finish();
                            }

                            @Override
                            public void onFailure(Error error) {
                                Toast.makeText(MapScreen.this,getString(R.string.network_error),Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                    builder.setTitle("You completed the route")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create().show();

                }catch (Exception e){

                    Log.d(TAG, "onLocationChanged: "+e);

                }



            }
        }


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "onProviderEnabled: ");

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private double degreeToRadian(double deg) {
        return deg * Math.PI / 180;
    }

    private double calcDistanceBetweenTwoLocationInKm(LatLng loc1, LatLng loc2) {

        final int earthRadius = 6371;

        double dLat = degreeToRadian(loc2.latitude - loc1.latitude);
        double dLong = degreeToRadian(loc2.longitude - loc2.longitude);

        double lat1 = degreeToRadian(loc1.latitude);
        double lat2 = degreeToRadian(loc2.latitude);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLong / 2) * Math.sin(dLong / 2)
                * Math.cos(lat1) * Math.cos(lat2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(this);
    }
}