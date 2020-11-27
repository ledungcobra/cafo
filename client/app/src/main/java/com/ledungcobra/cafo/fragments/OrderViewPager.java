package com.ledungcobra.cafo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ledungcobra.cafo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderViewPager#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderViewPager extends Fragment {


    public OrderViewPager() {
        
    }


    public static OrderViewPager newInstance(Bundle bundle) {
        OrderViewPager fragment = new OrderViewPager();
        fragment.setArguments(bundle);
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
        return inflater.inflate(R.layout.fragment_order_view_pager, container, false);
    }
}