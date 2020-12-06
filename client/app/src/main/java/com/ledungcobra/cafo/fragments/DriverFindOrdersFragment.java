package com.ledungcobra.cafo.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.PagerAdapter;
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
import com.ledungcobra.cafo.models.order.shipper.DetailOrderResponse;
import com.ledungcobra.cafo.service.UserApiHandler;
import com.ledungcobra.cafo.ui_calllback.UIThreadCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * The fragment represent for a screen in which the user roles as Driver can get orders
 */
public class DriverFindOrdersFragment extends Fragment implements OnMapReadyCallback,
        ViewPager.PageTransformer, OrderViewPager.OnAcceptAnOrderCallBack {

    //VIEW
    private GoogleMap mMap;
    private ViewPager viewPager;
    private ScreenSlidePagerAdapter adapter;
    private MenuItem refreshOrdersList;

    //DATA
    private MutableLiveData<Integer> currentPage = new MutableLiveData<>(-1);
    private final MutableLiveData<ArrayList<DetailOrderResponse>> listCustomerOrders = new MutableLiveData<>(new ArrayList<DetailOrderResponse>());
    private LocationManager locationManager;
    private Location userLocation;



    public void fetchUserOrder() {

        UserApiHandler
                .getInstance()
                .fetchFiveOrdersNearCustomerByShipper(
                        userLocation.getLatitude(),
                        userLocation.getLongitude(),
                        new UIThreadCallBack<List<DetailOrderResponse>, Error>() {

                            @Override
                            public void stopProgressIndicator() {

                            }

                            @Override
                            public void startProgressIndicator() {

                            }

                            @Override
                            public void onResult(final List<DetailOrderResponse> result) {


                                listCustomerOrders.setValue((ArrayList<DetailOrderResponse>) result);

                            }

                            @Override
                            public void onFailure(Error error) {


                                Toast.makeText(getActivity(), "Cannot fetch from server", Toast.LENGTH_SHORT).show();

                            }
                        }
                );

        if (refreshOrdersList != null) {

            refreshOrdersList.setEnabled(true);

        }

    }

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

        setHasOptionsMenu(true);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 3333);
        }
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 2, new LocationListener() {
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
            });
        }

        userLocation = null;

        if (locationManager != null) {
            userLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }


        fetchUserOrder();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_driver_find_orders, container, false);

        initUI(view);

        listCustomerOrders.observe(getViewLifecycleOwner(), new Observer<ArrayList<DetailOrderResponse>>() {
            @Override
            public void onChanged(ArrayList<DetailOrderResponse> detailOrderResponses) {

                if (detailOrderResponses != null) {
                    adapter.setListCustomerOrders(detailOrderResponses);


                }


            }
        });

        currentPage.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(final Integer page) {

                if (page != -1 && listCustomerOrders.getValue() != null && listCustomerOrders.getValue().size() > 0) {

                    getNewLocation(listCustomerOrders.getValue().get(page).getRestaurant().getAddress());

                }

            }
        });


        return view;
    }

    private void initUI(View view) {

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.myMap2);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        viewPager = view.findViewById(R.id.pager);
        adapter = new ScreenSlidePagerAdapter(requireActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true, this);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentPage.setValue(position);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void removeOrderFromArray(String orderID) {

        ArrayList<DetailOrderResponse> orders = listCustomerOrders.getValue();
        ArrayList<DetailOrderResponse> filteredOrders = new ArrayList<>();

        if (orders == null) return;
        for (DetailOrderResponse order : orders) {

            if (order.getId().equals(orderID)) {
                continue;
            }

            filteredOrders.add(order);

        }

        if (filteredOrders.size() == 0) {
            currentPage.setValue(-1);
        }

        listCustomerOrders.setValue(filteredOrders);


    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        private ArrayList<DetailOrderResponse> orders = new ArrayList<>();

        @NonNull
        @Override
        public Fragment getItem(int position) {
            OrderViewPager viewPager = OrderViewPager.newInstance(orders.get(position));
            viewPager.setCallback((OrderViewPager.OrderViewPagerCallback) getActivity());
            viewPager.setAcceptOrderCallBack(DriverFindOrdersFragment.this);
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

        @Override
        public int getItemPosition(@NonNull Object object) {
            return PagerAdapter.POSITION_NONE;
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

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 3333 && grantResults.length == 2 && grantResults[0] == PERMISSION_GRANTED && grantResults[1] == PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, new LocationListener() {
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
            });
        }
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.driver_menu, menu);

        refreshOrdersList = menu.findItem(R.id.refresh);

        refreshOrdersList.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                refreshOrdersList.setEnabled(false);

                fetchUserOrder();
                return true;
            }
        });
    }
}