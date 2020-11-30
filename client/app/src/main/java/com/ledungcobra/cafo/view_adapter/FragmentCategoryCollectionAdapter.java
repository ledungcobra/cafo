package com.ledungcobra.cafo.view_adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ledungcobra.cafo.fragments.RestaurantCategoryFoodFragment;
import com.ledungcobra.cafo.models.common_new.Food;
import com.ledungcobra.cafo.models.restaurant_detail_new.Menu;

import java.util.ArrayList;
import java.util.List;

public class FragmentCategoryCollectionAdapter extends FragmentStatePagerAdapter {
    int numOfMenu;
    Fragment fragment = null;
    List<Menu> menuList;
    public FragmentCategoryCollectionAdapter(@NonNull FragmentManager fm, int behavior, List<Menu> menuList) {
        super(fm, behavior);
        this.numOfMenu = menuList.size();
        this.menuList = new ArrayList<>();
        this.menuList.addAll(menuList);

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        for (int i=0; i<numOfMenu; i++){
            if (i==position){
                fragment = RestaurantCategoryFoodFragment.newInstance(menuList.get(position).getFoods());
                break;
            }
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return numOfMenu;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return menuList.get(position).getName();
    }

    public void getMenu(List<Menu> menuList){

    }

}
