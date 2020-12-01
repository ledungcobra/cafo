package com.ledungcobra.cafo.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ledungcobra.cafo.R;

import com.ledungcobra.cafo.RestaurantDetailScreen;
import com.ledungcobra.cafo.models.common_new.CartItem;
import com.ledungcobra.cafo.models.common_new.Food;
import com.ledungcobra.cafo.view_adapter.MenuGridViewAdapter;
import com.ledungcobra.cafo.view_adapter.MenuListViewAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RestaurantCategoryFoodFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantCategoryFoodFragment extends Fragment {

    MenuListViewAdapter adapter;
    MenuGridViewAdapter adapterGrid;
    List<CartItem> cartShops = new ArrayList<CartItem>();
    RecyclerView rvMenuFood;
    List<Food> foods = new ArrayList<Food>();
    private boolean isShowCard = true;

    public interface DataUpdateListener {
        void onDataUpdate(List<CartItem> mData);

        void onDataInit(List<Food> foods);

        void onScrollChangeListener(RecyclerView rvMenuFood);
    }

    DataUpdateListener dataListener;


    public interface callBackCategory {
        void callBackActivity(List<CartItem> cartShopList);
    }

    ShoppingCartFragment.callBack listCart;

    public RestaurantCategoryFoodFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static RestaurantCategoryFoodFragment newInstance(List<Food> foodList) {
        RestaurantCategoryFoodFragment fragment = new RestaurantCategoryFoodFragment();
        Bundle args = new Bundle();
        ArrayList<Food> foodArrayList = new ArrayList<Food>();
        foodArrayList.addAll(foodList);
        args.putSerializable("RestaurantID", foodArrayList);
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
        View view = inflater.inflate(R.layout.fragment_restaurant_category_food, container, false);

//
        foods = (List<Food>) getArguments().getSerializable("RestaurantID");
        adapter = new MenuListViewAdapter(getContext(), new ArrayList<Food>());

//        foods = (List<Food>) this.getArguments().getSerializable("MenuFood");
//        Repository.getInstance().getRestaurant(restaurantID, new UIThreadCallBack<Restaurant, Error>() {
//            @Override
//            public void stopProgressIndicator() {
//
//            }
//
//            @Override
//            public void startProgressIndicator() {
//
//            }
//
//            @Override
//            public void onResult(Restaurant result) {
//                adapter.setFoods(result.getMenuId().getFoodsId());
//            }
//
//            @Override
//            public void onFailure(Error error) {
//
//            }
//        });
        //adapter List
        adapter.setFoods(foods);
        rvMenuFood = view.findViewById(R.id.foodListRecyclerView);
        rvMenuFood.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMenuFood.setAdapter(adapter);
        dataListener = (DataUpdateListener) getActivity();

        adapter.setOnClickListener(new MenuListViewAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(int position) {
                Toast toast = Toast.makeText(getContext(), "Added " + adapter.getFood(position).getName(), LENGTH_SHORT);
                toast.show();
                int sameFood = 0;
                for (CartItem cartShop : cartShops) {
                    if (cartShop.getFood().equals(adapter.getFood(position))) {
                        cartShop.setNumber(cartShop.getNumber() + 1);
                        sameFood++;
                    }
                }
                if (sameFood == 0) {
                    CartItem cartShop = new CartItem(adapter.getFood(position), 1);
                    cartShops.add(cartShop);
                }

                dataListener.onDataUpdate(cartShops);
                cartShops.removeAll(cartShops);
            }
        });


        rvMenuFood.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                dataListener.onScrollChangeListener(rvMenuFood);
            }
        });

        return view;
    }

}

