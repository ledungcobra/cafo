package com.ledungcobra.cafo;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapScreen extends AppCompatActivity  implements OnMapReadyCallback {
    private GoogleMap mMap;
    private EditText searchTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_screen);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.myMap);
        mapFragment.getMapAsync((OnMapReadyCallback) this);
        searchTextField = findViewById(R.id.searchTextField);

        searchTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                getNewLocation(s.toString());
            }
        });



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        final LatLng pos = new LatLng(10.8843949, 106.7491382);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 50));
        googleMap.addMarker(new MarkerOptions()
                .position(pos)
                .title("Marker in Sydney"));

    }

    public void getNewLocation(final String searchTerm){

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET,
                        "https://ledung-google-map-scraping.herokuapp.com/?search="+ Uri.encode(searchTerm.toString()), null,
                        new Response.Listener<JSONArray>() {



                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    final double latitude = Double.parseDouble((String)(
                                            ((JSONObject)response.get(0)).get("lat")
                                    ));
                                    final double longitude = Double.parseDouble((String)(
                                            ((JSONObject)response.get(0)).get("long")
                                    ));

                                    moveCamera(latitude,longitude,searchTerm);

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

    public void moveCamera(double latitude,double longitude,String title){

        final LatLng pos = new LatLng(latitude, longitude);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 50));
        mMap.addMarker(new MarkerOptions()
                .position(pos)
                .title(title));


    }
}