package com.ledungcobra.cafo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.RestaurantDetailScreen;
import com.ledungcobra.cafo.models.restaurants_new.BriefRestaurantInfo;
import com.ledungcobra.cafo.ui_calllback.RestaurantClickListener;
import com.ledungcobra.cafo.view_adapter.RestaurantOverviewItemAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.ledungcobra.cafo.RestaurantsOverviewScreen.EXTRA_KEY;

/**
 * A fragment representing a list of Items.
 */
public class RestaurantOverviewTabViewFragment extends Fragment {
    private LiveData<ArrayList<BriefRestaurantInfo>> restaurantList;
    RestaurantOverviewItemAdapter adapter;


    public RestaurantOverviewTabViewFragment() {
    }

    public void setRestaurantList( LiveData<ArrayList<BriefRestaurantInfo>>  restaurantList) {
        this.restaurantList = restaurantList;
    }

    public static RestaurantOverviewTabViewFragment newInstance(LiveData<ArrayList<BriefRestaurantInfo>> restaurantList) {
        RestaurantOverviewTabViewFragment fragment = new RestaurantOverviewTabViewFragment();
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

        adapter.setOnRestaurantClickListener(new RestaurantClickListener() {
            @Override
            public void onClick(String restaurantID) {
                Intent intent = new Intent(getContext(), RestaurantDetailScreen.class);
                intent.putExtra(EXTRA_KEY, restaurantID);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        return view;
    }
}