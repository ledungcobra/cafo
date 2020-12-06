package com.ledungcobra.cafo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.models.restaurants_new.BriefRestaurantInfo;
import com.ledungcobra.cafo.view_adapter.RestaurantOverviewItemAdapter;

import java.util.ArrayList;
import java.util.List;


public class RestaurantOverviewVisitedFragment extends Fragment {
    private LiveData<List<BriefRestaurantInfo>> restaurantList = new MutableLiveData<List<BriefRestaurantInfo>>(new ArrayList<BriefRestaurantInfo>());
    private RestaurantOverviewItemAdapter adapter;

    public RestaurantOverviewVisitedFragment() {
    }



    public void setRestaurantList( LiveData<List<BriefRestaurantInfo>>  restaurantList) {
        this.restaurantList = restaurantList;
    }

    public static RestaurantOverviewVisitedFragment newInstance(MutableLiveData<List<BriefRestaurantInfo>> restaurantList) {
        RestaurantOverviewVisitedFragment fragment = new RestaurantOverviewVisitedFragment();
        Bundle args = new Bundle();
        fragment.setRestaurantList(restaurantList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        restaurantList.observe(this, new Observer<List<BriefRestaurantInfo>>() {
            @Override
            public void onChanged(List<BriefRestaurantInfo> briefRestaurantInfos) {
                if(briefRestaurantInfos!=null){
                    adapter.setRestaurants((ArrayList<BriefRestaurantInfo>) briefRestaurantInfos);
                }
            }
        });



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_overview_visited_list, container, false);
//        List<BriefRestaurantInfo> tRestaurantList = new ArrayList<BriefRestaurantInfo>();
//        tRestaurantList = (ArrayList<BriefRestaurantInfo>) getArguments().getSerializable("RestaurantVisitedList");
//

         adapter = new RestaurantOverviewItemAdapter(getContext());

        adapter.setRestaurants((ArrayList<BriefRestaurantInfo>) restaurantList.getValue());
        RecyclerView recyclerView =   (RecyclerView)view;
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