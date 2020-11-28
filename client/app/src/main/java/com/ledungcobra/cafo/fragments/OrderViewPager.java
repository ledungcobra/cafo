package com.ledungcobra.cafo.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.ledungcobra.cafo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderViewPager#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderViewPager extends Fragment {

    MaterialButton btnAcceptOrder;
    private OrderViewPagerCallback callback;
    public OrderViewPager() {
        
    }


    public interface OrderViewPagerCallback{
        void onButtonAcceptOrderClick();
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
        View view = inflater.inflate(R.layout.fragment_order_view_pager, container, false);
        btnAcceptOrder = view.findViewById(R.id.btnAcceptOrder);
        btnAcceptOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback!=null){
                    callback.onButtonAcceptOrderClick();
                }else{
                    Log.d("Must implement this", "onClick: ");

                }
            }
        });
        return view;

    }

    public void setCallback(OrderViewPagerCallback callback) {
        this.callback = callback;
    }
}