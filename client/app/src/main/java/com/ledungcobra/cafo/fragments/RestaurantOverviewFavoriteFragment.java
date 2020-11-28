package com.ledungcobra.cafo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import static com.ledungcobra.cafo.RestaurantsOverviewScreen.EXTRA_KEY;

/**
 * A fragment representing a list of Items.
 */
public class RestaurantOverviewFavoriteFragment extends Fragment {


    public RestaurantOverviewFavoriteFragment() {
    }


    public static RestaurantOverviewFavoriteFragment newInstance() {
        RestaurantOverviewFavoriteFragment fragment = new RestaurantOverviewFavoriteFragment();
        Bundle args = new Bundle();

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

        RecyclerView recyclerView =   (RecyclerView)view;
        recyclerView.setAdapter(adapter);

        adapter.setOnRestaurantClickListener(new RestaurantClickListener() {
            @Override
            public void onClick(String restaurantID) {
                Intent intent = new Intent(getContext(), RestaurantDetailScreen.class);
                intent.putExtra(EXTRA_KEY,restaurantID);
                startActivity(intent);

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Repository.getInstance().fetchAllRestaurants(2, 20, new UIThreadCallBack<ArrayList<BriefRestaurantInfo>, Error>() {
            @Override
            public void stopProgressIndicator() {

            }

            @Override
            public void startProgressIndicator() {

            }

            @Override
            public void onResult(ArrayList<BriefRestaurantInfo> result) {

                adapter.setRestaurants(result);
            }

            @Override
            public void onFailure(Error error) {
                Log.d("CALL_API", "onFailure: "+error);
            }
        });
        return view;
    }
}