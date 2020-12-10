package com.ledungcobra.cafo.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.activity.MapScreen;
import com.ledungcobra.cafo.models.order.shipper.DetailOrderResponse;
import com.ledungcobra.cafo.service.UserApiHandler;
import com.ledungcobra.cafo.ui_calllback.UIThreadCallBack;
import com.ledungcobra.cafo.view_adapter.DriverOrderListAdapter;

import java.util.ArrayList;
import java.util.List;


public class DriverOrdersFragment extends Fragment {
    //VIEW
    private ImageView gifProgressbar;
    private TextView tvIdOrder;
    private AnimationDrawable animationDrawable;
    private View viewFragment;


    //DATA
    MutableLiveData<List<DetailOrderResponse>> orders = new MutableLiveData<>(null);

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
        this.viewFragment = view;
        //Load animation
        gifProgressbar =  view.findViewById(R.id.gif_progress_bar);
        animationDrawable = (AnimationDrawable)gifProgressbar.getDrawable();
        gifProgressbar.setVisibility(View.GONE);
        tvIdOrder = view.findViewById(R.id.tvIdOrder);
        //Load animation

        callback = (CallBackToCreateFm) getActivity();

        getAllOrders();

        orders.observe(getViewLifecycleOwner(), new Observer<List<DetailOrderResponse>>() {
            @Override
            public void onChanged(List<DetailOrderResponse> detailOrderResponses) {
                if(detailOrderResponses!=null){
                    bindView(view, (ArrayList<DetailOrderResponse>) detailOrderResponses);
                }
            }
        });

        return view;
    }

    private void getAllOrders(){
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
            public void onResult(final List<DetailOrderResponse> result) {
                orders.setValue(result);
            }

            @Override
            public void onFailure(Error error) {
                Log.d("ORDER", String.valueOf(error));
                tvIdOrder.setText("Error");
            }
        });
    }

    private void bindView(final View view, final ArrayList<DetailOrderResponse> result){
        RecyclerView recyclerViewOrder = view.findViewById(R.id.recyclerViewOrder);
        DriverOrderListAdapter adapter;
        adapter = new DriverOrderListAdapter(getContext(), orders.getValue());
        recyclerViewOrder.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewOrder.setAdapter(adapter);

        adapter.setOnClickListener(new DriverOrderListAdapter.OnItemClickListener() {
            @Override
            public void onMapButtonClicked(final int pos) {
                new AlertDialog.Builder(getActivity()).
                        setTitle("Choose type of map do you want to use")
                        .setPositiveButton("Google Map (Recommended)", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q="+ MapScreen.getAddress(getActivity(),
                                        Double.parseDouble(result.get(pos).getOrderPosition().getLatitude()),
                                        Double.parseDouble(result.get(pos).getOrderPosition().getLongitude()))+
                                        "&mode=d"));

                                intent.setPackage("com.google.android.apps.maps");
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton("Built-in map", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getActivity(), MapScreen.class);
                                intent.putExtra("lat",Double.parseDouble(result.get(pos).getOrderPosition().getLatitude()));
                                intent.putExtra("long", Double.parseDouble(result.get(pos).getOrderPosition().getLongitude()));

                                intent.putExtra(getString(R.string.order_id),result.get(pos).getId());

                                startActivityForResult(intent,MapScreen.CODE);
                            }
                        })
                        .create()
                        .show();

            }
                    /*@Override
                    public void onDeleteOrder(int position) {
                        Toast.makeText(getContext(),"Delete Click",Toast.LENGTH_SHORT).show();
                    }*/

            @Override
            public void onDetailClick(int position) {
                callback.onCreateFm(orders.getValue().get(position));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MapScreen.CODE && data !=null && resultCode == MapScreen.CODE ){
            getAllOrders();
        }
    }
}