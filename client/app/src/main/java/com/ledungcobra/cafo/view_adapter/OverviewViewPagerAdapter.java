package com.ledungcobra.cafo.view_adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.ledungcobra.cafo.database.Repository;
import com.ledungcobra.cafo.fragments.RestaurantOverviewNewFragment;
import com.ledungcobra.cafo.fragments.RestaurantOverviewTabViewFragment;
import com.ledungcobra.cafo.models.restaurants_new.BriefRestaurantInfo;
import com.ledungcobra.cafo.models.user.TrackingRestaurant;

import java.util.ArrayList;
import java.util.List;

import static com.ledungcobra.cafo.models.user.TrackingRestaurant.FAVORITE;
import static com.ledungcobra.cafo.models.user.TrackingRestaurant.VISITED;

public class OverviewViewPagerAdapter extends FragmentPagerAdapter {

    //TODO: Dũng Filter
    List<BriefRestaurantInfo> newBriefRestaurantInfoList;
    LifecycleOwner lifecycleOwner;

    public OverviewViewPagerAdapter(@NonNull FragmentManager fm,
                                    int behavior,
                                    List<BriefRestaurantInfo> newBriefRestaurantInfoList,
                                    LifecycleOwner activity) {
        super(fm, behavior);
        lifecycleOwner = activity;
        this.newBriefRestaurantInfoList = newBriefRestaurantInfoList;
        this.newBriefRestaurantInfoList.addAll(newBriefRestaurantInfoList);
    }

    Fragment newFragment, visitedFragment, favoriteFragment;

    public LiveData<ArrayList<BriefRestaurantInfo>> filter(final int type) {
        final MutableLiveData<ArrayList<BriefRestaurantInfo>> filterResult = new MutableLiveData<>(new ArrayList<BriefRestaurantInfo>());
        Repository.getInstance().getAllTrackingRestaurants().observe(lifecycleOwner, new Observer<List<TrackingRestaurant>>() {
            @Override
            public void onChanged(final List<TrackingRestaurant> trackingRestaurants) {
                Log.d("CALL_API", "Number of tracking ress " + trackingRestaurants.size());
                if (trackingRestaurants != null && trackingRestaurants.size() > 0) {
                    List<BriefRestaurantInfo> filterData = new ArrayList<>();

                    for (BriefRestaurantInfo briefRes : newBriefRestaurantInfoList) {
                        boolean exist = false;
                        for (TrackingRestaurant res : trackingRestaurants) {
                            if (res.getType() == type && res.getId().equals( briefRes.getId())) {
                                exist = true;
                                break;
                            }
                        }

                        if (exist) {
                            filterData.add(briefRes);

                            Log.d("CALL_APU", "onChanged: " + briefRes);
                        }

                    }
                    filterResult.setValue((ArrayList<BriefRestaurantInfo>) filterData);
                }




            }
        });
        return filterResult;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {

            newFragment = newFragment == null ? RestaurantOverviewNewFragment.newInstance(newBriefRestaurantInfoList) : newFragment;

            return newFragment;
        } else if (position == 1) {
            visitedFragment = visitedFragment == null ? RestaurantOverviewTabViewFragment.newInstance(filter(VISITED)) : visitedFragment;
            return visitedFragment;
        } else if (position == 2) {
            favoriteFragment = favoriteFragment == null ? RestaurantOverviewTabViewFragment.newInstance(filter(FAVORITE)) : favoriteFragment;
            return favoriteFragment;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
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
