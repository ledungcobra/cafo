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
    Fragment newFragment,visitedFragment,favoriteFragment;
    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position==0){

            newFragment = newFragment == null?RestaurantOverviewNewFragment.newInstance():newFragment;
            return newFragment;
        }
        else if(position==1){
            visitedFragment = visitedFragment == null?RestaurantOverviewVisitedFragment.newInstance():visitedFragment;
            return visitedFragment;
        }
        else if (position==2){
            favoriteFragment = favoriteFragment == null? RestaurantOverviewFavoriteFragment.newInstance():favoriteFragment;
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
