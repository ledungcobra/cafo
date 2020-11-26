package com.ledungcobra.cafo.view_adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ledungcobra.cafo.fragments.RestaurantOverviewFavoriteFragment;
import com.ledungcobra.cafo.fragments.RestaurantOverviewNewFragment;
import com.ledungcobra.cafo.fragments.RestaurantOverviewVisitedFragment;

public class OverviewViewPagerAdapter extends FragmentPagerAdapter {

    public OverviewViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position==0){
            return RestaurantOverviewNewFragment.newInstance();
        }
        else if(position==1){
            return RestaurantOverviewVisitedFragment.newInstance();
        }
        else if (position==2){
            return RestaurantOverviewFavoriteFragment.newInstance();
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
