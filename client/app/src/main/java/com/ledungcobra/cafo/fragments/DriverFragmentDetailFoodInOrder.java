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
import com.ledungcobra.cafo.view_adapter.FoodListViewAdapter;


import java.io.Serializable;
import java.util.List;


public class DriverFragmentDetailFoodInOrder extends Fragment {


    //DATA
    private static final String ARG_PARAM1 = "Order Detail";

    public DriverFragmentDetailFoodInOrder() {
        // Required empty public constructor
    }


    public static DriverFragmentDetailFoodInOrder newInstance(DetailOrderResponse detail) {
        DriverFragmentDetailFoodInOrder fragment = new DriverFragmentDetailFoodInOrder();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, (Serializable) detail);
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
        DetailOrderResponse details;
        details = (DetailOrderResponse) getArguments().getSerializable(ARG_PARAM1);

        View view = inflater.inflate(R.layout.fragment_driver_detail_order, container, false);
        TextView tvTotal = view.findViewById(R.id.txtTotalCost);

        if (details == null) {
            //Error
            tvTotal.setText("Error");
            return view;
        }

        //Data
        String orderID = details.getId();
        String resName = details.getRestaurant().getName();
        String resAddress = details.getRestaurant().getAddress();
        String customerPosition = details.getOrderPosition().getLatitude()
                +", "+ details.getOrderPosition().getLongitude();
        String customerContact = "(NONE)";  //API can't handle
        String status = details.getStatus();
        List<com.ledungcobra.cafo.models.order.shipper.Food> foodList = details.getFoods();
        int shippingFee = 20000;
        int total = details.getTotal() + shippingFee;
        //Data

        //Views
        TextView tvOrderID = view.findViewById(R.id.tvOrderID);
        TextView tvResName = view.findViewById(R.id.tvResOrder);
        TextView tvResAddress = view.findViewById(R.id.tvResAddressOrder);
        TextView tvCustomerPosition = view.findViewById(R.id.tvCustomerAddressOrder);
        TextView tvCustomerContact = view.findViewById(R.id.tvCustomerPhoneOrder);
        TextView tvStatus = view.findViewById(R.id.tvStatusOrder);
        TextView tvShippingFee = view.findViewById(R.id.txtShippingFee);
        //Views

        //Set data
        tvOrderID.setText(orderID);
        tvResName.setText(resName);
        tvResAddress.setText(resAddress);
        tvCustomerPosition.setText(customerPosition);
        tvCustomerContact.setText(customerContact);
        tvStatus.setText(status);
        tvShippingFee.setText(String.format("%,d", shippingFee) + getString(R.string.currency));
        tvTotal.setText(String.format("%,d", total) + getString(R.string.currency));

        RecyclerView recyclerView = view.findViewById(R.id.listCustomerOrderItems);
        FoodListViewAdapter adapter = new FoodListViewAdapter(getContext(),foodList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        recyclerView.setAdapter(adapter);
        //Set data
        return view;
    }
}