package com.ledungcobra.cafo.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.RestaurantDetailScreen;
import com.ledungcobra.cafo.database.Repository;
import com.ledungcobra.cafo.fragments.dummy.DummyContent;
import com.ledungcobra.cafo.models.restaurants.BriefRestaurantInfo;
import com.ledungcobra.cafo.ui_calllback.RestaurantClickListener;
import com.ledungcobra.cafo.ui_calllback.UIThreadCallBack;
import com.ledungcobra.cafo.view_adapter.RestaurantOverviewItemAdapter;

import java.util.List;

import static com.ledungcobra.cafo.RestaurantsOverviewScreen.EXTRA_KEY;

/**
 * A fragment representing a list of Items.
 */
public class RestaurantOverviewNewFragment extends Fragment {


    public RestaurantOverviewNewFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RestaurantOverviewNewFragment newInstance() {
        RestaurantOverviewNewFragment fragment = new RestaurantOverviewNewFragment();
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
        View view = inflater.inflate(R.layout.fragment_restaurant_overview_new_list, container, false);

        final RestaurantOverviewItemAdapter adapter = new RestaurantOverviewItemAdapter(getContext());

        RecyclerView recyclerView =   (RecyclerView)view;
        adapter.setOnRestaurantClickListener(new RestaurantClickListener() {
            @Override
            public void onClick(String restaurantID) {
                Intent intent = new Intent(getContext(), RestaurantDetailScreen.class);
                intent.putExtra(EXTRA_KEY,restaurantID);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Repository.getInstance().fetchAllRestaurants(new UIThreadCallBack<List<BriefRestaurantInfo>, Error>() {
            @Override
            public void stopProgressIndicator() {

            }

            @Override
            public void startProgressIndicator() {

            }

            @Override
            public void onResult(List<BriefRestaurantInfo> result) {
                adapter.setRestaurants(result);
            }

            @Override
            public void onFailure(Error error) {

            }
        });




        return view;
    }
}