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
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverDetailOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverDetailOrderFragment extends Fragment {



    RecyclerView listCustomerOrder;
    public DriverDetailOrderFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DriverDetailOrderFragment newInstance() {
        DriverDetailOrderFragment fragment = new DriverDetailOrderFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_driver_detail_order, container, false);
        listCustomerOrder = view.findViewById(R.id.listCustomerOrderItems);
        List<CustomerOrder> customerOrders = new ArrayList<>();
        Collections.addAll(customerOrders,new CustomerOrder("1000d","Name 1",
                        "10","1000.0d","https://picsum.photos/id/237/200/300"),
                new CustomerOrder("1000d","Name 2",
                        "10","1000.0d","https://picsum.photos/id/237/200/300"),

                new CustomerOrder("1000d","Name 3",
                        "10","1000.0d","https://picsum.photos/id/237/200/300"),
                new CustomerOrder("1000d","Name 4",
                        "10","1000.0d","https://picsum.photos/id/237/200/300"),
                new CustomerOrder("1000d","Name 5",
                        "10","1000.0d","https://picsum.photos/id/237/200/300"));
        listCustomerOrder.setAdapter(new CustomerOrdersAdapter(customerOrders));
        listCustomerOrder.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;

    }
}