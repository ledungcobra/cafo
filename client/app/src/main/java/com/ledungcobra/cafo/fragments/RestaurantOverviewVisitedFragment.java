package com.ledungcobra.cafo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.RestaurantDetailScreen;
import com.ledungcobra.cafo.database.Repository;
import com.ledungcobra.cafo.models.restaurants_new.BriefRestaurantInfo;
import com.ledungcobra.cafo.ui_calllback.RestaurantClickListener;
import com.ledungcobra.cafo.ui_calllback.UIThreadCallBack;
import com.ledungcobra.cafo.view_adapter.RestaurantOverviewItemAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.ledungcobra.cafo.RestaurantsOverviewScreen.EXTRA_KEY;

/**
 * A fragment representing a list of Items.
 */
public class RestaurantOverviewVisitedFragment extends Fragment {
    ArrayList<BriefRestaurantInfo> restaurantList;

    public RestaurantOverviewVisitedFragment() {
    }


    public static RestaurantOverviewVisitedFragment newInstance(List<BriefRestaurantInfo> restaurantList) {
        RestaurantOverviewVisitedFragment fragment = new RestaurantOverviewVisitedFragment();
        Bundle args = new Bundle();
        ArrayList<BriefRestaurantInfo> restaurantArrayList = new ArrayList<BriefRestaurantInfo>();
        restaurantArrayList.addAll(restaurantList);
        args.putSerializable("RestaurantVisitedList",restaurantArrayList);



        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_overview_visited_list, container, false);
        restaurantList = new ArrayList<BriefRestaurantInfo>();
        restaurantList = (ArrayList<BriefRestaurantInfo>) getArguments().getSerializable("RestaurantVisitedList");


        final RestaurantOverviewItemAdapter adapter = new RestaurantOverviewItemAdapter(getContext());

        adapter.setRestaurants(restaurantList);
        RecyclerView recyclerView =   (RecyclerView)view;
//        fragmentCallBack = (RestaurantOverviewNewFragment.fragmentCallBack) getActivity();
//        adapter.setOnRestaurantClickListener(new RestaurantClickListener() {
//            @Override
//            public void onClick(String restaurantID) {
//                fragmentCallBack.onNavigateToOverviewScreen(restaurantID);
//            }
//        });
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        return view;
    }
}