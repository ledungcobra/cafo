package com.ledungcobra.cafo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ledungcobra.cafo.R;

public class DashboardFragment extends Fragment {

    public DashboardFragment() {
        super();
    }
    public static DashboardFragment getInstance(String title){

        Bundle data = new Bundle();
        data.putString("title",title);
        DashboardFragment fragment = new DashboardFragment();
        fragment.setArguments(data);
        return fragment;

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drawer,container,false);

        //Button for customer find fetch five order
        CardView cardFindOrders = view.findViewById(R.id.cardFindOrders);

        cardFindOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction ft= fragmentManager.beginTransaction();
                DriverFindOrdersFragment findOrdersFragment = DriverFindOrdersFragment.newInstance();
                ft.add(R.id.container,findOrdersFragment);
                ft.addToBackStack(null);
                ft.commit();

            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
