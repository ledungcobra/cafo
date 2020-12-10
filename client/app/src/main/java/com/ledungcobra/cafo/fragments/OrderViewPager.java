package com.ledungcobra.cafo.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.models.order.shipper.DetailOrderResponse;
import com.ledungcobra.cafo.service.UserApiHandler;
import com.ledungcobra.cafo.ui_calllback.UIThreadCallBack;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;


public class OrderViewPager extends Fragment {

    //VIEW
    private MaterialButton btnAcceptOrder;

    //CALLBACK
    private OrderViewPagerCallback callback;
    private OnAcceptAnOrderCallBack acceptAnOrderCallBack;

    public void setAcceptOrderCallBack(DriverFindOrdersFragment driverFindOrdersFragment) {
        this.acceptAnOrderCallBack = driverFindOrdersFragment;
    }

    //INTERFACE
    public interface OnAcceptAnOrderCallBack{
        void removeOrderFromArray(String orderID);
    }

    public OrderViewPager() {

    }

    private DetailOrderResponse detailOrderResponse;


    public void setDetailOrderResponse(DetailOrderResponse detailOrderResponse) {
        this.detailOrderResponse = detailOrderResponse;
    }

    public interface OrderViewPagerCallback {
        void onSelectedOrder(DetailOrderResponse detailOrderResponse);
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

                UserApiHandler.getInstance().acceptAnOrder(detailOrderResponse.getId(), new UIThreadCallBack<String, Error>() {
                    @Override
                    public void stopProgressIndicator() {

                    }

                    @Override
                    public void startProgressIndicator() {

                    }

                    @Override
                    public void onResult(String result) {

                        try{

                            Toast.makeText(getContext(), getString(R.string.accept_an_order_successfully),Toast.LENGTH_SHORT).show();

                        }catch (Exception e){

                            Log.d("EXCEPTION", "onResult: ");

                        }
                        acceptAnOrderCallBack.removeOrderFromArray(detailOrderResponse.getId());
                    }

                    @Override
                    public void onFailure(Error error) {
                        Toast.makeText(getContext(), getString(R.string.cannot_accept_this_order),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onSelectedOrder(detailOrderResponse);

            }
        });
        return view;

    }

    public void setCallback(OrderViewPagerCallback callback) {
        this.callback = callback;
    }
}