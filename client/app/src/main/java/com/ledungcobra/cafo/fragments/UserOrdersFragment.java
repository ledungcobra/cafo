package com.ledungcobra.cafo.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        final TextView tvIdOrder =  view.findViewById(R.id.idOrder);
        final DetailOrderResponse orderResponse;
        final List<DetailOrderResponse> orderList= new ArrayList<>();
        callback = (CallBacktoCreateFm) getActivity();


        UserApiHandler.getInstance().getOrdersByCustomer(new UIThreadCallBack<ArrayList<DetailOrderResponse>, Error>() {
            @Override
            public void stopProgressIndicator() {

            }

            @Override
            public void startProgressIndicator() {

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
                    public void onDeleteOrder(int position) {
                        Toast.makeText(getContext(),"Delete Click",Toast.LENGTH_SHORT).show();
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