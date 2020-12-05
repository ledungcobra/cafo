package com.ledungcobra.cafo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.models.order.customer.CustomerOrder;
import com.ledungcobra.cafo.view_adapter.CustomerOrdersAdapter;

import java.util.ArrayList;
import java.util.List;


public class DriverDetailOrderFragment extends Fragment {


    //VIEW
    RecyclerView listCustomerOrder;
    CustomerOrdersAdapter adapter ;

    public DriverDetailOrderFragment() {
        // Required empty public constructor
    }


    public static DriverDetailOrderFragment newInstance() {
        DriverDetailOrderFragment fragment = new DriverDetailOrderFragment();
        Bundle args = new Bundle();
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
        listCustomerOrder = view.findViewById(R.id.listCustomerOrderItems);
        List<CustomerOrder> customerOrders = new ArrayList<>();

        adapter = new CustomerOrdersAdapter(customerOrders);
        listCustomerOrder.setAdapter(adapter);


        listCustomerOrder.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;

    }
}