package com.ledungcobra.cafo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ledungcobra.cafo.models.common.CartShop;
import com.ledungcobra.cafo.models.common.Food;
import com.ledungcobra.cafo.models.common.Price;
import com.ledungcobra.cafo.view_adapter.CartAdapterRecyclerView;

import java.io.Serializable;
import java.util.List;


public class ShoppingCartFragment extends Fragment {
    List<CartShop> cartShops;

    interface callBack{
        void callBackActivity(List<CartShop> cartShopList);
    }
    callBack listCart;

    public static ShoppingCartFragment newInstance(Bundle bundle){
        ShoppingCartFragment shoppingCartFragment = new ShoppingCartFragment();
        shoppingCartFragment.setArguments(bundle);

        return shoppingCartFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        assert this.getArguments() != null;
        cartShops= (List<CartShop>) this.getArguments().getSerializable("ListFood");

        final View rootView = inflater.inflate(R.layout.fragment_shopping_cart, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recycleViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final CartAdapterRecyclerView cartAdapterRecyclerView = new CartAdapterRecyclerView(getContext(),cartShops);

        recyclerView.setAdapter(cartAdapterRecyclerView);
        int sumOfCost = 0;
        for (CartShop cartShop: cartShops){
            sumOfCost += cartShop.getFood().getPrice().getValue() * cartShop.getNumber();
        }
        //Click event
        cartAdapterRecyclerView.setOnClickListener(new CartAdapterRecyclerView.OnItemClickListener() {
            @Override
            public void onAddClick(int position) {
                int sumOfCostNew=0;
                for (CartShop cartShop: cartShops){
                    sumOfCostNew += cartShop.getFood().getPrice().getValue() * cartShop.getNumber();
                }

                TextView CostTotal = rootView.findViewById(R.id.tvResult);
                CostTotal.setText(Integer.toString(sumOfCostNew));


            }

            @Override
            public void onRemove(int position) {
                if (cartShops.get(position).getNumber() == 0) {
                    cartShops.remove(position);
                    cartAdapterRecyclerView.notifyItemRemoved(position);
                    cartAdapterRecyclerView.notifyDataSetChanged();
                }

                int sumOfCostNew=0;
                for (CartShop cartShop: cartShops){
                    sumOfCostNew += cartShop.getFood().getPrice().getValue() * cartShop.getNumber();
                }

                TextView tvResult = rootView.findViewById(R.id.tvResult);
                tvResult.setText(Integer.toString(sumOfCostNew));
                listCart.callBackActivity(cartShops);
            }
        });

        TextView tvSum = rootView.findViewById(R.id.tvResult);
        Price priceTotal = new Price(sumOfCost);
        tvSum.setText(Integer.toString(priceTotal.getValue()));


        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof callBack ){
            listCart = (callBack) context;
        } else {
            throw new ClassCastException(context.toString());
        }
    }
}