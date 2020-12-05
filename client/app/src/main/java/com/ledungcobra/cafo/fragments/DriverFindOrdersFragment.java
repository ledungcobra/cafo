package com.ledungcobra.cafo.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

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
import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.service.UserApiHandler;
import com.ledungcobra.cafo.models.order.shipper.DetailOrderResponse;
import com.ledungcobra.cafo.ui_calllback.UIThreadCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DriverFindOrdersFragment extends Fragment implements OnMapReadyCallback, ViewPager.PageTransformer {

    //VIEW
    private GoogleMap mMap;
    ViewPager viewPager;

    //DATA
    MutableLiveData<Integer> currentPage = new MutableLiveData<>(-1);
    MutableLiveData<ArrayList<DetailOrderResponse>> listCustomerOrders = new MutableLiveData<>(new ArrayList<DetailOrderResponse>());

    public DriverFindOrdersFragment() {
    }


    public static DriverFindOrdersFragment newInstance() {
        DriverFindOrdersFragment fragment = new DriverFindOrdersFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("CALL_API", "onCreate: PERMISSION PROBEM");

            return;
        }
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location userLocation = locationManager != null ? locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) : null;
        UserApiHandler.getInstance().fetchFiveOrdersNearCustomerByShipper(userLocation.getLatitude(),
                userLocation.getLongitude(), new UIThreadCallBack<List<DetailOrderResponse>, Error>() {
                    @Override
                    public void stopProgressIndicator() {

                    }

                    @Override
                    public void startProgressIndicator() {

                    }

                    @Override
                    public void onResult(List<DetailOrderResponse> result) {
                        listCustomerOrders.setValue((ArrayList<DetailOrderResponse>) result);
                    }

                    @Override
                    public void onFailure(Error error) {

                    }
                });


        currentPage.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(final Integer page) {

                if (page != -1 && listCustomerOrders.getValue() != null) {

                    getNewLocation(listCustomerOrders.getValue().get(page).getRestaurant().getAddress());

                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_driver_find_orders, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.myMap2);
        mapFragment.getMapAsync(this);

        viewPager = view.findViewById(R.id.pager);

        final ScreenSlidePagerAdapter adapter = new ScreenSlidePagerAdapter(requireActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true, this);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("VIEWPAGER", "onPageScrolled: " + position);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("CALL_API", "onPageScrolled: " + position);
                currentPage.setValue(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        listCustomerOrders.observe(getViewLifecycleOwner(), new Observer<ArrayList<DetailOrderResponse>>() {
            @Override
            public void onChanged(ArrayList<DetailOrderResponse> detailOrderResponses) {
                adapter.setListCustomerOrders(detailOrderResponses);
            }
        });

        return view;
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        private ArrayList<DetailOrderResponse> orders = new ArrayList<>();

        @Override
        public Fragment getItem(int position) {
            OrderViewPager viewPager = OrderViewPager.newInstance(orders.get(position));
            viewPager.setCallback((OrderViewPager.OrderViewPagerCallback) getActivity());
            return viewPager;
        }

        @Override
        public int getCount() {
            return orders.size();
        }

        public void setListCustomerOrders(ArrayList<DetailOrderResponse> orders) {
            this.orders = orders;
            notifyDataSetChanged();
        }
    }


    @Override
    public void transformPage(@NonNull View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0f);

        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(0.85f, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                view.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                view.setTranslationX(-horzMargin + vertMargin / 2);
            }

            // Scale the page down (between MIN_SCALE and 1)
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            // Fade the page relative to its size.
            view.setAlpha(0.85f +
                    (scaleFactor - 0.85f) /
                            (1 - 0.85f) * (1 - 0.85f));

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0f);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(11, 106), 50));
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(11, 106))
                .title("Marker in Sydney"));

    }


    public void moveCamera(double latitude, double longitude, String title) {

        final LatLng pos = new LatLng(latitude, longitude);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 50));
        mMap.addMarker(new MarkerOptions()
                .position(pos)
                .title(title));

    }

    public void getNewLocation(final String searchTerm) {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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
}