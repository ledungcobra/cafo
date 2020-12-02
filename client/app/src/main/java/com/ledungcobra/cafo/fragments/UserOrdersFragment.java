package com.ledungcobra.cafo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ledungcobra.cafo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserOrdersFragment extends Fragment {



    public UserOrdersFragment() {
        // Required empty public constructor
    }


    public static UserOrdersFragment newInstance() {
        UserOrdersFragment fragment = new UserOrdersFragment();
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
        View view =  inflater.inflate(R.layout.fragment_user_orders, container, false);


        return view;
    }
}