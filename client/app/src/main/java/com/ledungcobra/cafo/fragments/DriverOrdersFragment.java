package com.ledungcobra.cafo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ledungcobra.cafo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverOrdersFragment extends Fragment {

    public DriverOrdersFragment() {
        // Required empty public constructor
    }

    public static DriverOrdersFragment newInstance(String param1, String param2) {
        DriverOrdersFragment fragment = new DriverOrdersFragment();
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
        return inflater.inflate(R.layout.fragment_driver_orders, container, false);
    }
}