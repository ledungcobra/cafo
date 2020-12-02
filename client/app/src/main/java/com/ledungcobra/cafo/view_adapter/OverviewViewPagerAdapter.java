package com.ledungcobra.cafo.view_adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.ledungcobra.cafo.database.Repository;
import com.ledungcobra.cafo.fragments.RestaurantOverviewFavoriteFragment;
import com.ledungcobra.cafo.fragments.RestaurantOverviewNewFragment;
import com.ledungcobra.cafo.fragments.RestaurantOverviewVisitedFragment;
import com.ledungcobra.cafo.models.restaurants_new.BriefRestaurantInfo;
import com.ledungcobra.cafo.models.user.TrackingRestaurant;

import java.util.ArrayList;
import java.util.List;

import static com.ledungcobra.cafo.models.user.TrackingRestaurant.VISITED;

public class OverviewViewPagerAdapter extends FragmentPagerAdapter {

    List<BriefRestaurantInfo> newBriefRestaurantInfoList;
    LifecycleOwner activity;

    public OverviewViewPagerAdapter(@NonNull FragmentManager fm,
                                    int behavior,
                                    List<BriefRestaurantInfo> newBriefRestaurantInfoList,
                                    LifecycleOwner activity) {
        super(fm, behavior);
        this.newBriefRestaurantInfoList = new ArrayList<BriefRestaurantInfo>();
        this.activity = activity;
        this.newBriefRestaurantInfoList.addAll(newBriefRestaurantInfoList);
    }

    Fragment newFragment,visitedFragment,favoriteFragment;
    private MutableLiveData<List<BriefRestaurantInfo>> filter(final int type, final List<BriefRestaurantInfo> data){
        final MutableLiveData<List<BriefRestaurantInfo>> result = new MutableLiveData<List<BriefRestaurantInfo>>(new ArrayList<BriefRestaurantInfo>());

        Repository.getInstance().getAllTrackingRestaurants().observe(activity, new Observer<List<TrackingRestaurant>>() {
            @Override
            public void onChanged(List<TrackingRestaurant> trackingRestaurants) {
                List<BriefRestaurantInfo> listFav = new ArrayList<>();
                for(TrackingRestaurant trackingRestaurant: trackingRestaurants){
                    if(trackingRestaurant.getType() != type ) continue;
                    boolean exist = false;
                    BriefRestaurantInfo currentRes = null;
                    for(BriefRestaurantInfo res: data){
                        if(res.getId().equals(trackingRestaurant.getId())){
                            exist = true;
                            currentRes = res;
                            break;
                        }
                    }
                    if(exist){

                        listFav.add(currentRes);

                    }

                }

                result.setValue(listFav);
            }
        });
        return result;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position==0){

            newFragment = newFragment == null? RestaurantOverviewNewFragment.newInstance(newBriefRestaurantInfoList):newFragment;
            return newFragment;
        }
        else if(position==1){
            visitedFragment = visitedFragment == null? RestaurantOverviewVisitedFragment.newInstance(filter(VISITED,newBriefRestaurantInfoList)):visitedFragment;
            return visitedFragment;
        }
        else if (position==2){

            favoriteFragment = favoriteFragment == null? RestaurantOverviewFavoriteFragment.newInstance(newBriefRestaurantInfoList):favoriteFragment;
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
        if (position==0){
            return "New";
        }
        else if(position==1){
            return "Visited";
        }
        else if (position==2){
            return "Favorite";
        }
        return null;
    }
}
