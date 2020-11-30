package com.ledungcobra.cafo.view_adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ledungcobra.cafo.fragments.RestaurantCategoryFoodFragment;
import com.ledungcobra.cafo.models.common_new.Food;

import java.util.List;

public class FragmentCategoryCollectionAdapter extends FragmentStatePagerAdapter {
    int numOfTabs;
    Fragment fragment = null;
    List<Food> foodList;
    public FragmentCategoryCollectionAdapter(@NonNull FragmentManager fm, int behavior, int numOfCategory) {
        super(fm, behavior);
        this.numOfTabs =numOfCategory;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        for (int i=0; i<numOfTabs; i++){
            if (i==position){
                fragment = RestaurantCategoryFoodFragment.newInstance(foodList);
                break;
            }
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Tab "+ String.valueOf(position);
    }

    public void getFoods(List<Food> foods){
        this.foodList = foods;
    }
}
