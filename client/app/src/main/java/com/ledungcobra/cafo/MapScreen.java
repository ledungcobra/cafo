package com.ledungcobra.cafo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapScreen extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private GoogleMap mMap;
    MutableLiveData<ArrayList<String>> listAddresses = new MutableLiveData<>(new ArrayList<String>());
    LocationManager locationManager;
    String TAG = "GOOGLE_MAP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_screen);

//        Uri gmmIntentUri = Uri.parse("google.navigation:q=10.10,100");
//        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//        mapIntent.setPackage("com.google.android.apps.maps");
//        startActivity(mapIntent);

        ArrayAdapter<String> adapter
                = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listAddresses.getValue());


        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.edtTextField);
        autoCompleteTextView.setThreshold(5);
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
        mapFragment.getMapAsync((OnMapReadyCallback) this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean shouldContinue = true;
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 9999);
            shouldContinue = false;
        }

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 8888);
            shouldContinue = false;
        }

//        if (shouldContinue) {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
//        }




    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.d("GOOGLE_MAP", "onMapClick: " + latLng.toString());
                Log.d("GOOGLE_MAP", "Address " + getAddress(MapScreen.this, latLng.latitude, latLng.longitude));
            }
        });


        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("lat", 0);
        double long_ = intent.getDoubleExtra("long", 0);
        final LatLng pos = new LatLng(lat, long_);


        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 50));
        googleMap.addMarker(new MarkerOptions()
                .position(pos)
                .title("Marker in Sydney"));

        moveCamera(lat, long_, "Start");
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://dev.virtualearth.net/")
//                .addConverterFactory(GsonConverterFactory.create(
//                        new GsonBuilder()
//                                .create()))
//                .build();
//
//        FindRouteService findRouteService  = retrofit.create(FindRouteService.class);
//        final ArrayList<LatLng> locations = new ArrayList<>();
//        moveCamera(10.8830014,106.7795138,"dsd");
//        findRouteService.getMapRoute("10.8800706,106.8086773",
//                "10.8830014,106.7795138",
//                "Am3HEJkqwNwrNzEWDBEpvxScysCUadoI854xMNk4bfCy8Ud_HAQQEIRgRvTElxIr").enqueue(
//                new Callback<MapRouteInfo>() {
//                    @RequiresApi(api = Build.VERSION_CODES.N)
//                    @Override
//                    public void onResponse(Call<MapRouteInfo> call, retrofit2.Response<MapRouteInfo> response) {
//                        MapRouteInfo data = response.body();
//                        if(data.getResourceSets().size()>0){
//
//                            data.getResourceSets().get(0).getResources().get(0).
//                                            getRouteLegs().get(0).getItineraryItems().forEach(new Consumer<ItineraryItem>() {
//                                @Override
//                                public void accept(ItineraryItem itineraryItem) {
//                                    Double lat = itineraryItem.getManeuverPoint().getCoordinates().get(0);
//                                    Double long_ = itineraryItem.getManeuverPoint().getCoordinates().get(1);
//                                    LatLng latLng = new LatLng(lat,long_ );
//                                    locations.add(latLng);
//                                }
//                            });
//
//                            Log.d(TAG, "onResponse: "+locations);
//
//                            drawALine(locations);
//
//
//
//
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<MapRouteInfo> call, Throwable t) {
//                        Log.d(TAG, "onFailure: "+t);
//                    }
//                }
//        );

    }

    public void drawALine(ArrayList<LatLng> listLocsToDraw){

        moveCamera(listLocsToDraw.get(0).latitude,listLocsToDraw.get(0).longitude,"");
        if ( listLocsToDraw.size() < 2 )
        {
            return;
        }

        PolylineOptions options = new PolylineOptions();

        options.color( Color.parseColor( "#CC0000FF" ) );
        options.width( 5 );
        options.visible( true );

        for ( LatLng locRecorded : listLocsToDraw )
        {
            options.add(locRecorded);
        }


        mMap.addPolyline(options );

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

                                    moveCamera(latitude, longitude, searchTerm);

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

    public void moveCamera(double latitude, double longitude, String title) {

        final LatLng pos = new LatLng(latitude, longitude);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 50));
        mMap.addMarker(new MarkerOptions()
                .position(pos)
                .title(title));


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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 9999 && grantResults.length > 0 && grantResults[0] == Activity.RESULT_OK) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
        }

        if (requestCode == 8888 && grantResults.length > 0 && grantResults[0] == Activity.RESULT_OK) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: ");
        String address = getAddress(this,location.getLatitude(),location.getLongitude());

        Log.d(TAG, "Your address"+ address);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "onStatusChanged: ");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "onProviderEnabled: ");

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}