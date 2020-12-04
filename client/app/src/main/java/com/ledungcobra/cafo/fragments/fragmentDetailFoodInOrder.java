package com.ledungcobra.cafo.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.models.order.shipper.Food;
import com.ledungcobra.cafo.view_adapter.CartAdapterRecyclerView;
import com.ledungcobra.cafo.view_adapter.FoodListViewAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragmentDetailFoodInOrder#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragmentDetailFoodInOrder extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "List Food in DetailOrder";
    private static final String ARG_PARAM2 = "Total in DetailOrder";


    public fragmentDetailFoodInOrder() {
        // Required empty public constructor
    }


    public static fragmentDetailFoodInOrder newInstance(List<Food> foodList,Integer total) {
        fragmentDetailFoodInOrder fragment = new fragmentDetailFoodInOrder();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, (Serializable) foodList);
        args.putInt(ARG_PARAM2,total);
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
        // Inflate the layout for this fragment
        List<com.ledungcobra.cafo.models.order.shipper.Food> foodList;
        foodList = new ArrayList<com.ledungcobra.cafo.models.order.shipper.Food>();
        foodList = (List<com.ledungcobra.cafo.models.order.shipper.Food>) getArguments().getSerializable(ARG_PARAM1);
        Integer total = getArguments().getInt(ARG_PARAM2);

        View view = inflater.inflate(R.layout.fragment_detail_food_in_order, container, false);
        TextView tvTotal = view.findViewById(R.id.tvResultInOrder);
        tvTotal.setText(Integer.toString(total)+" đ");
        RecyclerView recyclerView = view.findViewById(R.id.rvDetailFoodOrder);
        FoodListViewAdapter adapter = new FoodListViewAdapter(getContext(),foodList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }
}