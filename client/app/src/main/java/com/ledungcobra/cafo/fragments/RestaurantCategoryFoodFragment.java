package com.ledungcobra.cafo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.models.common_new.CartItem;
import com.ledungcobra.cafo.models.common_new.Food;
import com.ledungcobra.cafo.view_adapter.MenuListViewAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class RestaurantCategoryFoodFragment extends Fragment {

    //VIEW
    MenuListViewAdapter adapter;
    RecyclerView rvMenuFood;

    //DATA
    List<CartItem> cartShops = new ArrayList<CartItem>();
    List<Food> foods = new ArrayList<Food>();
    private LiveData<Boolean> isListView;

    //LISTENER
    DataUpdateListener dataListener;

    //CALLBACK
    public interface DataUpdateListener {


        void onDataUpdate(List<CartItem> mData);

        void onScrollChangeListener(RecyclerView rvMenuFood);

    }


    public RestaurantCategoryFoodFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static RestaurantCategoryFoodFragment newInstance(List<Food> foodList, LiveData<Boolean> isListView) {
        RestaurantCategoryFoodFragment fragment = new RestaurantCategoryFoodFragment();
        Bundle args = new Bundle();
        ArrayList<Food> foodArrayList = new ArrayList<Food>();
        foodArrayList.addAll(foodList);
        args.putSerializable("RestaurantID", foodArrayList);
        fragment.setArguments(args);
        fragment.isListView = isListView;

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

        InitUI(view);

        return view;
    }



    public void InitUI(View view){
        //Get data from OverViewRestaurantScreen
        foods = (List<Food>) getArguments().getSerializable("RestaurantID");
        //Create an adapter
        adapter = new MenuListViewAdapter(getContext(), new ArrayList<Food>());
        //Set value for adapter
        adapter.setFoods(foods);

        rvMenuFood = view.findViewById(R.id.foodListRecyclerView);
        //Set list layout for recycleView
        rvMenuFood.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMenuFood.setAdapter(adapter);

        //init callback
        dataListener = (DataUpdateListener) getActivity();
        isListView.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if (rvMenuFood != null) {

                    if (isListView.getValue()) {

                        rvMenuFood.setLayoutManager(new GridLayoutManager(getActivity(), 2));

                    } else {
                        rvMenuFood.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                }

            }
        });

        //set onClick add Food in Cart
        adapter.setOnClickListener(new MenuListViewAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(int position) {
                //Notification
                Toast toast = Toast.makeText(getContext(), "Added " + adapter.getFood(position).getName(), LENGTH_SHORT);
                toast.show();

                //Count amount of food
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

                //callback RestaurantDetail
                dataListener.onDataUpdate(cartShops);
                cartShops.removeAll(cartShops);
            }
        });

        //Animation Scroll Card
        rvMenuFood.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                dataListener.onScrollChangeListener(rvMenuFood);

            }
        });

    }


}

