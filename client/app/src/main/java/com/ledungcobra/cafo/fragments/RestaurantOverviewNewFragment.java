package com.ledungcobra.cafo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.activity.RestaurantDetailScreen;
import com.ledungcobra.cafo.models.restaurants_new.BriefRestaurantInfo;
import com.ledungcobra.cafo.models.user.TrackingRestaurant;
import com.ledungcobra.cafo.service.Repository;
import com.ledungcobra.cafo.ui_calllback.RestaurantClickListener;
import com.ledungcobra.cafo.view_adapter.RestaurantOverviewItemAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.ledungcobra.cafo.activity.RestaurantsOverviewScreen.EXTRA_KEY;



public class RestaurantOverviewNewFragment extends Fragment {

    private ArrayList<BriefRestaurantInfo> restaurantList;
    private fragmentCallBack fragmentCallBack;

    public interface fragmentCallBack{
        void onNavigateToOverviewScreen(String restaurantID);
    }


    public RestaurantOverviewNewFragment() {
    }

    public static RestaurantOverviewNewFragment newInstance(List<BriefRestaurantInfo> restaurantList) {
        RestaurantOverviewNewFragment fragment = new RestaurantOverviewNewFragment();
        Bundle args = new Bundle();

        ArrayList<BriefRestaurantInfo> restaurantArrayList = new ArrayList<BriefRestaurantInfo>();
        restaurantArrayList.addAll(restaurantList);
        args.putSerializable("RestaurantNewList",restaurantArrayList);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    String TAG = "CALL_API";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_overview_new_list, container, false);

        final RestaurantOverviewItemAdapter adapter = new RestaurantOverviewItemAdapter(getContext());
        restaurantList = new ArrayList<BriefRestaurantInfo>();
        restaurantList = (ArrayList<BriefRestaurantInfo>) getArguments().getSerializable("RestaurantNewList");
        adapter.setRestaurants(restaurantList);
        RecyclerView recyclerView =   (RecyclerView)view;
//        fragmentCallBack = (RestaurantOverviewNewFragment.fragmentCallBack)getActivity();
        adapter.setOnRestaurantClickListener(new RestaurantClickListener() {
            @Override
            public void onClick(String restaurantID) {
                Intent intent = new Intent(getActivity(), RestaurantDetailScreen.class);
                Repository.getInstance().insert(new TrackingRestaurant(restaurantID,TrackingRestaurant.VISITED));
                intent.putExtra(EXTRA_KEY,restaurantID);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false));


        return view;
    }
}