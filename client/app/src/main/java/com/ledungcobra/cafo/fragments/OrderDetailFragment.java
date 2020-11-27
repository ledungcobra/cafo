package com.ledungcobra.cafo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.models.CustomerOrder;
import com.ledungcobra.cafo.view_adapter.CustomerOrdersAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderDetailFragment extends Fragment {




    RecyclerView recyclerView;
    List<CustomerOrder> customerOrders = new ArrayList<>();
    public OrderDetailFragment() {
        // Required empty public constructor
    }


    public static OrderDetailFragment newInstance(String param1, String param2) {
        OrderDetailFragment fragment = new OrderDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    CustomerOrdersAdapter customerOrdersAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);

        recyclerView = view.findViewById(R.id.rvCustomerOrders);
        customerOrdersAdapter = new CustomerOrdersAdapter();
        recyclerView.setAdapter(customerOrdersAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Collections.addAll(customerOrders,new CustomerOrder("1000d","Name 1",
                "10","1000.0d"),
                new CustomerOrder("1000d","Name 2",
                "10","1000.0d"),
                new CustomerOrder("1000d","Name 3",
                        "10","1000.0d"),
                new CustomerOrder("1000d","Name 4",
                        "10","1000.0d"),
                new CustomerOrder("1000d","Name 5",
                        "10","1000.0d"));


        return view;
    }
}

