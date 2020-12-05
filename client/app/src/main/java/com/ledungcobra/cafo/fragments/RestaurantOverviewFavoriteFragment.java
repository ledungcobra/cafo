package com.ledungcobra.cafo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.models.restaurants_new.BriefRestaurantInfo;
import com.ledungcobra.cafo.view_adapter.RestaurantOverviewItemAdapter;

import java.util.ArrayList;
import java.util.List;


public class RestaurantOverviewFavoriteFragment extends Fragment {

    ArrayList<BriefRestaurantInfo> restaurantList;


    public RestaurantOverviewFavoriteFragment() {
    }


    public static RestaurantOverviewFavoriteFragment newInstance(List<BriefRestaurantInfo> restaurantList) {
        RestaurantOverviewFavoriteFragment fragment = new RestaurantOverviewFavoriteFragment();
        Bundle args = new Bundle();
        ArrayList<BriefRestaurantInfo> restaurantArrayList = new ArrayList<BriefRestaurantInfo>();
        restaurantArrayList.addAll(restaurantList);
        args.putSerializable("RestaurantFavoriteList", restaurantArrayList);


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
        View view = inflater.inflate(R.layout.fragment_restaurant_overview_favorite_list, container, false);


        final RestaurantOverviewItemAdapter adapter = new RestaurantOverviewItemAdapter(getContext());
        restaurantList = new ArrayList<BriefRestaurantInfo>();
        restaurantList = (ArrayList<BriefRestaurantInfo>) getArguments().getSerializable("RestaurantFavoriteList");

        adapter.setRestaurants(restaurantList);
        RecyclerView recyclerView = (RecyclerView) view;
//        fragmentCallBack = (RestaurantOverviewNewFragment.fragmentCallBack) getActivity();
//        adapter.setOnRestaurantClickListener(new RestaurantClickListener() {
//            @Override
//            public void onClick(String restaurantID) {
//                fragmentCallBack.onNavigateToOverviewScreen(restaurantID);
//            }
//        });
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false));


        return view;
    }
}