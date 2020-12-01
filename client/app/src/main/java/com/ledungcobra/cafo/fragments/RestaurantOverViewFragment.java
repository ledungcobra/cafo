package com.ledungcobra.cafo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.RestaurantDetailScreen;
import com.ledungcobra.cafo.database.Repository;
import com.ledungcobra.cafo.models.restaurants_new.BriefRestaurantInfo;
import com.ledungcobra.cafo.models.user.TrackingRestaurant;
import com.ledungcobra.cafo.ui_calllback.RestaurantClickListener;
import com.ledungcobra.cafo.view_adapter.OverviewViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.ledungcobra.cafo.RestaurantsOverviewScreen.EXTRA_KEY;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RestaurantOverViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantOverViewFragment extends Fragment implements RestaurantClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    List<BriefRestaurantInfo> restaurantList;


    // TODO: Rename and change types of parameters
    private String mParam1;

    public RestaurantOverViewFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RestaurantOverViewFragment newInstance(List<BriefRestaurantInfo> restaurantList) {
        RestaurantOverViewFragment fragment = new RestaurantOverViewFragment();
        Bundle args = new Bundle();

        ArrayList<BriefRestaurantInfo> restaurantArrayList = new ArrayList<BriefRestaurantInfo>();
        restaurantArrayList.addAll(restaurantList);
        args.putSerializable("RestaurantList",restaurantArrayList);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_over_view, container, false);

        restaurantList = new ArrayList<BriefRestaurantInfo>();
        restaurantList = (List<BriefRestaurantInfo>) getArguments().getSerializable("RestaurantList");


        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager viewPager = view.findViewById(R.id.vpRestaurant);
        OverviewViewPagerAdapter viewPagerAdapter = new OverviewViewPagerAdapter(getChildFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,restaurantList,getActivity());

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    @Override
    public void onClick(String restaurantID) {

        Intent intent = new Intent(getContext(), RestaurantDetailScreen.class);
        intent.putExtra(EXTRA_KEY, restaurantID);
        startActivity(intent);

    }
}
