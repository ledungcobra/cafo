package com.ledungcobra.cafo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
@Deprecated
public class RestaurantOverviewNewFragment extends Fragment {

    LiveData<ArrayList<BriefRestaurantInfo>> restaurantList;
    fragmentCallBack fragmentCallBack;
     RestaurantOverviewItemAdapter adapter ;
    public interface fragmentCallBack{
        void onNavigateToOverviewScreen(String restaurantID);
    }


    public RestaurantOverviewNewFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RestaurantOverviewNewFragment newInstance(LiveData<ArrayList<BriefRestaurantInfo>> restaurantList) {
        RestaurantOverviewNewFragment fragment = new RestaurantOverviewNewFragment();
        Bundle args = new Bundle();
        Log.d("CALL_API", "newInstance: RestaurantOverviewNewFragment");
        fragment.setRestaurantList(restaurantList);
        fragment.setArguments(args);
        return fragment;
    }

    private void setRestaurantList(LiveData<ArrayList<BriefRestaurantInfo>> restaurantList) {

        this.restaurantList = restaurantList;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restaurantList.observe(this, new Observer<List<BriefRestaurantInfo>>() {
            @Override
            public void onChanged(List<BriefRestaurantInfo> briefRestaurantInfos) {
                Log.d(TAG, "Change state ");
                if(adapter!=null){
                    adapter.setRestaurants((ArrayList<BriefRestaurantInfo>) briefRestaurantInfos);
                }
            }
        });

        Log.d(TAG, "onCreate: RestaurantOverviewNewFragment");


    }
    String TAG = "CALL_API";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_overview_new_list, container, false);

        adapter = new RestaurantOverviewItemAdapter(getContext());

        RecyclerView recyclerView =   (RecyclerView)view;
//        fragmentCallBack = (RestaurantOverviewNewFragment.fragmentCallBack)getActivity();
        adapter.setOnRestaurantClickListener(new RestaurantClickListener() {
            @Override
            public void onClick(String restaurantID) {
                Intent intent = new Intent(getActivity(), RestaurantDetailScreen.class);
                intent.putExtra(EXTRA_KEY,restaurantID);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }
}