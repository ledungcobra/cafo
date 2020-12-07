package com.ledungcobra.cafo.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
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
import com.ledungcobra.cafo.view_adapter.OrderListAdapter;

import java.util.ArrayList;
import java.util.List;


public class UserOrdersFragment extends Fragment {

    CallBacktoCreateFm callback;
    public interface CallBacktoCreateFm{
        void onCreateFm(List<Food> foods, Integer total);
    }
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_user_orders, container, false);
        final TextView tvIdOrder =  view.findViewById(R.id.tvIdOrder);
        tvIdOrder.setTextColor(getResources().getColor(android.R.color.white));

        //Load animation
        final ImageView gifProgressbar =  view.findViewById(R.id.gif_progress_bar);
        final AnimationDrawable animationDrawable = (AnimationDrawable)gifProgressbar.getDrawable();
        gifProgressbar.setVisibility(View.GONE);
        //Load animation
        final DetailOrderResponse orderResponse;
        final List<DetailOrderResponse> orderList= new ArrayList<>();
        callback = (CallBacktoCreateFm) getActivity();


        UserApiHandler.getInstance().getOrdersByCustomer(new UIThreadCallBack<ArrayList<DetailOrderResponse>, Error>() {
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
            public void onResult(ArrayList<DetailOrderResponse> result) {
                tvIdOrder.setText("Loaded");
                orderList.addAll(result);
                RecyclerView recyclerViewOrder = view.findViewById(R.id.recyclerViewOrder);
                OrderListAdapter adapter;
                adapter = new OrderListAdapter(getContext(), orderList);
                recyclerViewOrder.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerViewOrder.setAdapter(adapter);

                adapter.setOnClickListener(new OrderListAdapter.OnItemClickListener() {
                    @Override
                    public void onDeleteOrder(final int position) {
                        AlertDialog.Builder myBuilder =
                                new AlertDialog.Builder(getContext());
                        myBuilder.setTitle("Delete Order")
                                .setMessage("Are you sure?")
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichOne) {
                                        Toast.makeText(
                                                getContext(),
                                                "Delete order " + orderList.get(position).getId(),
                                                Toast.LENGTH_SHORT
                                        ).show();
                                        //TODO: Call API to set order to status delete
                                        dialog.dismiss();
                                    }})
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }) //setNegativeButton
                                .show();
                    }

                    @Override
                    public void onDetailClick(int position) {
                        callback.onCreateFm(orderList.get(position).getFoods(), orderList.get(position).getTotal());

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