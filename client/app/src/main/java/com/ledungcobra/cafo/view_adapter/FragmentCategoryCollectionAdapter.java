package com.ledungcobra.cafo.view_adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.LiveData;

import com.ledungcobra.cafo.fragments.RestaurantCategoryFoodFragment;
import com.ledungcobra.cafo.models.restaurant_detail_new.Menu;

import java.util.ArrayList;
import java.util.List;

public class FragmentCategoryCollectionAdapter extends FragmentStatePagerAdapter {

    List<Menu> menuList;
    private LiveData<Boolean> isListView;


    public FragmentCategoryCollectionAdapter(@NonNull FragmentManager fm, int behavior, List<Menu> menuList,LiveData<Boolean> isListView) {
        super(fm, behavior);
        this.menuList = new ArrayList<>();
        this.menuList.addAll(menuList);
        this.isListView = isListView;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        return RestaurantCategoryFoodFragment.newInstance(menuList.get(position).getFoods(),isListView);

    }

    @Override
    public int getCount() {
        return menuList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return menuList.get(position).getName();
    }

}
