package com.ledungcobra.cafo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ledungcobra.cafo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverDetailOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverDetailOrderFragment extends Fragment {

;

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

        return view;

    }
}