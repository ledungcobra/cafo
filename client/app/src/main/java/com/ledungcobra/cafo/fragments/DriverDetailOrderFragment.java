package com.ledungcobra.cafo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.models.order.shipper.DetailOrderResponse;
import com.ledungcobra.cafo.models.order.shipper.Food;
import com.ledungcobra.cafo.view_adapter.CustomerOrdersAdapter;

import java.text.NumberFormat;
import java.util.List;


public class DriverDetailOrderFragment extends Fragment {


    //VIEW
    private RecyclerView listCustomerOrder;
    private TextView totalCost;
    private TextView shippingFee;

    //DATA
    private DetailOrderResponse detailOrderResponse;
    private List<Food> foods;

    public DriverDetailOrderFragment() {
        // Required empty public constructor
    }


    public static DriverDetailOrderFragment newInstance(DetailOrderResponse detailOrderResponse) {
        DriverDetailOrderFragment fragment = new DriverDetailOrderFragment();
        Bundle args = new Bundle();
        fragment.detailOrderResponse = detailOrderResponse;
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
        View view = inflater.inflate(R.layout.fragment_driver_detail_order, container, false);

        initUI(view);

        foods = detailOrderResponse.getFoods();
        totalCost.setText(NumberFormat.getCurrencyInstance().format(calcTotalCost())+"đ");
        shippingFee.setText("20.000đ");

        listCustomerOrder.setAdapter(new CustomerOrdersAdapter(foods));
        listCustomerOrder.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;

    }

    private void initUI(View view){

        listCustomerOrder = view.findViewById(R.id.listCustomerOrderItems);
        totalCost = view.findViewById(R.id.txtTotalCost);
        shippingFee = view.findViewById(R.id.txtShippingFee);

    }

    private long calcTotalCost() {
        int total = 0;

        for (Food food : detailOrderResponse.getFoods()) {
            total += food.getAmount() * food.getPrice().getValue();
        }

        total += 20000;
        return total;
    }
}