package com.ledungcobra.cafo.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.service.Repository;
import com.ledungcobra.cafo.models.restaurants_new.BriefRestaurantInfo;
import com.ledungcobra.cafo.models.user.TrackingRestaurant;

import java.util.ArrayList;
import java.util.List;

import static com.ledungcobra.cafo.models.user.TrackingRestaurant.FAVORITE;
import static com.ledungcobra.cafo.models.user.TrackingRestaurant.VISITED;


public class RestaurantOverViewFragment extends Fragment{
    //DATA
    MutableLiveData<ArrayList<BriefRestaurantInfo>> restaurantList = new MutableLiveData(new ArrayList<>());
    //VIEW
    RestaurantOverViewFragment.OverviewViewPagerAdapter viewPagerAdapter;


    public RestaurantOverViewFragment() {
        // Required empty public constructor
    }

    public static RestaurantOverViewFragment newInstance() {
        RestaurantOverViewFragment fragment = new RestaurantOverViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("BACK" ,"onCreate fragment ");
        //Get list restaurant
        ArrayList<BriefRestaurantInfo> restaurantInfos = (ArrayList<BriefRestaurantInfo>) getArguments().getSerializable(getString(R.string.list_restaurants));
        restaurantList.setValue(restaurantInfos);

        restaurantList.observe(getActivity(), new Observer<ArrayList<BriefRestaurantInfo>>() {
            @Override
            public void onChanged(ArrayList<BriefRestaurantInfo> briefRestaurantInfos) {
                if (viewPagerAdapter != null) {
                    viewPagerAdapter.notifyDataSetChanged();
                }
            }
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_over_view, container, false);

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager viewPager = view.findViewById(R.id.vpRestaurant);

        viewPagerAdapter = new RestaurantOverViewFragment.OverviewViewPagerAdapter(getChildFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        return view;
    }


    public class OverviewViewPagerAdapter extends FragmentPagerAdapter {


        public OverviewViewPagerAdapter(@NonNull FragmentManager fm,
                                        int behavior
        ) {
            super(fm, behavior);
        }

        public LiveData<ArrayList<BriefRestaurantInfo>> filter(final int type) {
            final MutableLiveData<ArrayList<BriefRestaurantInfo>> filterResult = new MutableLiveData<>(new ArrayList<BriefRestaurantInfo>());
            Repository.getInstance().getAllTrackingRestaurants().observe(getViewLifecycleOwner(), new Observer<List<TrackingRestaurant>>() {
                @Override
                public void onChanged(final List<TrackingRestaurant> trackingRestaurants) {
                    if (trackingRestaurants != null && trackingRestaurants.size() > 0) {
                        final List<BriefRestaurantInfo> filterData = new ArrayList<>();

                        restaurantList.observe(getViewLifecycleOwner(), new Observer<ArrayList<BriefRestaurantInfo>>() {
                            @Override
                            public void onChanged(ArrayList<BriefRestaurantInfo> briefRestaurantInfos) {
                                if (briefRestaurantInfos != null) {
                                    for (BriefRestaurantInfo briefRes : briefRestaurantInfos) {

                                        boolean exist = false;
                                        for (TrackingRestaurant res : trackingRestaurants) {

                                            if (res.getType() == type && res.getId().equals(briefRes.getId())) {
                                                exist = true;
                                                break;
                                            }

                                        }

                                        if (exist) {
                                            filterData.add(briefRes);

                                            Log.d("CALL_API", "onChanged: " + briefRes);
                                        }

                                    }
                                }
                                filterResult.setValue((ArrayList<BriefRestaurantInfo>) filterData);
                            }
                        });

                    }

                }
            });
            return filterResult;
        }

        Fragment newFragment, visitedFragment, favoriteFragment;

        @NonNull
        @Override
        public Fragment getItem(int position) {

            if (position == 0) {//Create New Restaurant
                newFragment = newFragment == null ? RestaurantOverviewTabViewFragment.newInstance(restaurantList,RestaurantOverviewTabViewFragment.NEW_PAGER) : newFragment;
                return newFragment;
            } else if (position == 1) { //Create Visited Restaurant
                visitedFragment = visitedFragment == null ? RestaurantOverviewTabViewFragment.newInstance(filter(VISITED),RestaurantOverviewTabViewFragment.VISITED_PAGER) : visitedFragment;
                return visitedFragment;
            } else if (position == 2) { //Create Favorite Restaurant
                favoriteFragment = favoriteFragment == null ? RestaurantOverviewTabViewFragment.newInstance(filter(FAVORITE),RestaurantOverviewTabViewFragment.FAV_PAGER) : favoriteFragment;
                return favoriteFragment;
            }

            return null;
        }

        @Override
        public int getCount() {//3 tab view
            return 3;
        }


        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {//Title of page
            if (position == 0) {
                return "New";
            } else if (position == 1) {
                return "Visited";
            } else if (position == 2) {
                return "Favorite";
            }
            return null;
        }
    }

}

