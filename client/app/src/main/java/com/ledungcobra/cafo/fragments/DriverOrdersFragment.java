package com.ledungcobra.cafo.fragments;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.service.UserApiHandler;
import com.ledungcobra.cafo.models.order.shipper.DetailOrderResponse;
import com.ledungcobra.cafo.models.order.shipper.Food;
import com.ledungcobra.cafo.ui_calllback.UIThreadCallBack;
import com.ledungcobra.cafo.view_adapter.DriverOrderListAdapter;
import com.ledungcobra.cafo.view_adapter.OrderListAdapter;

import java.util.ArrayList;
import java.util.List;


public class DriverOrdersFragment extends Fragment {

    CallBackToCreateFm callback;
    public interface CallBackToCreateFm{
        void onCreateFm(DetailOrderResponse detail);
    }
    public DriverOrdersFragment() {
        // Required empty public constructor
    }


    public static DriverOrdersFragment newInstance() {
        DriverOrdersFragment fragment = new DriverOrdersFragment();
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_user_orders, container, false);
        final TextView tvIdOrder = view.findViewById(R.id.tvIdOrder);

        //Load animation
        final ImageView gifProgressbar =  view.findViewById(R.id.gif_progress_bar);
        final AnimationDrawable animationDrawable = (AnimationDrawable)gifProgressbar.getDrawable();
        gifProgressbar.setVisibility(View.GONE);
        //Load animation

        final DetailOrderResponse orderResponse;
        final List<DetailOrderResponse> orderList= new ArrayList<>();
        callback = (CallBackToCreateFm) getActivity();


        UserApiHandler.getInstance().getAcceptedOrdersByShipper(new UIThreadCallBack<List<DetailOrderResponse>, Error>() {
            @Override
            public void stopProgressIndicator() {
                tvIdOrder.setText("Loaded");
                tvIdOrder.setVisibility(View.GONE);
                gifProgressbar.setVisibility(View.GONE);
                animationDrawable.stop();
            }

            @Override
            public void startProgressIndicator() {
                tvIdOrder.setText("Loading");
                gifProgressbar.setVisibility(View.VISIBLE);
                animationDrawable.start();
            }

            @Override
            public void onResult(List<DetailOrderResponse> result) {
                orderList.addAll((ArrayList<DetailOrderResponse>) result);
                RecyclerView recyclerViewOrder = view.findViewById(R.id.recyclerViewOrder);
                DriverOrderListAdapter adapter;
                adapter = new DriverOrderListAdapter(getContext(), orderList);
                recyclerViewOrder.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerViewOrder.setAdapter(adapter);

                adapter.setOnClickListener(new DriverOrderListAdapter.OnItemClickListener() {
                    /*@Override
                    public void onDeleteOrder(int position) {
                        Toast.makeText(getContext(),"Delete Click",Toast.LENGTH_SHORT).show();
                    }*/

                    @Override
                    public void onDetailClick(int position) {
                        callback.onCreateFm(orderList.get(position));
                    }
                });

            }

            @Override
            public void onFailure(Error error) {
                Log.d("ORDER", String.valueOf(error));
                tvIdOrder.setText("Error");
            }
        });
        return view;
    }
}