package com.ledungcobra.cafo.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.models.order.shipper.DetailOrderResponse;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

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

    private DetailOrderResponse detailOrderResponse;

    public DetailOrderResponse getDetailOrderResponse() {
        return detailOrderResponse;
    }

    public void setDetailOrderResponse(DetailOrderResponse detailOrderResponse) {
        this.detailOrderResponse = detailOrderResponse;
    }

    public interface OrderViewPagerCallback {
        void onButtonAcceptOrderClick();
    }


    public static OrderViewPager newInstance(DetailOrderResponse detailOrderResponse) {
        OrderViewPager fragment = new OrderViewPager();
        fragment.setDetailOrderResponse(detailOrderResponse);
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
        View view = inflater.inflate(R.layout.fragment_order_view_pager, container, false);
        btnAcceptOrder = view.findViewById(R.id.btnAcceptOrder);

        ImageView imgRestaurant = view.findViewById(R.id.imgRestaurant);
        Picasso.get().load(detailOrderResponse.getRestaurant().getImage().getValue()).into(imgRestaurant);

        TextView restaurantName = view.findViewById(R.id.tvRestaurantName);
        TextView restaurantAddress = view.findViewById(R.id.tvAddress);
        TextView distanceToRes = view.findViewById(R.id.tvDistanceToRestaurant);

        restaurantName.setText(detailOrderResponse.getRestaurant().getName());
        restaurantAddress.setText(detailOrderResponse.getRestaurant().getAddress());
        distanceToRes.setText(new DecimalFormat("#######.##").format(detailOrderResponse.getDistanceToRes())+" km");

        btnAcceptOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onButtonAcceptOrderClick();
                } else {
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