package com.ledungcobra.cafo.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.activity.CartInformationShipping;
import com.ledungcobra.cafo.models.common_new.CartItem;
import com.ledungcobra.cafo.models.common_new.Price;
import com.ledungcobra.cafo.view_adapter.CartAdapterRecyclerView;

import java.io.Serializable;
import java.util.List;


public class ShoppingCartFragment extends Fragment {
    private List<CartItem> cartShops;
    //TODO : xử lí shopping cart khi không có gì thì disable nút đặt hàng
    public interface callBack{
        void callBackActivity(List<CartItem> cartShopList);
    }

    private String resID;
    private callBack listCart;

    public static ShoppingCartFragment newInstance(Bundle bundle){
        ShoppingCartFragment shoppingCartFragment = new ShoppingCartFragment();
        shoppingCartFragment.setArguments(bundle);

        return shoppingCartFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!=null){
            resID = getArguments().getString("resID");
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        assert this.getArguments() != null;
        cartShops= (List<CartItem>) this.getArguments().getSerializable("ListFood");

        final View rootView = inflater.inflate(R.layout.fragment_shopping_cart, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recycleViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final CartAdapterRecyclerView cartAdapterRecyclerView = new CartAdapterRecyclerView(getContext(),cartShops);

        recyclerView.setAdapter(cartAdapterRecyclerView);
        int sumOfCost = 0;
        for (CartItem cartShop: cartShops){
            sumOfCost += cartShop.getFood().getPrice().getValue() * cartShop.getNumber();
        }
        //Click event
        final TextView CostTotal = rootView.findViewById(R.id.tvResult);

        cartAdapterRecyclerView.setOnClickListener(new CartAdapterRecyclerView.OnItemClickListener() {
            @Override
            public void onAddClick(int position) {
                int sumOfCostNew=0;
                for (CartItem cartShop: cartShops){
                    sumOfCostNew += cartShop.getFood().getPrice().getValue() * cartShop.getNumber();
                }

                CostTotal.setText(
                        String.format("%,d",sumOfCostNew) + " " + getString(R.string.currency));

            }

            @Override
            public void onRemove(int position) {
                if (cartShops.get(position).getNumber() == 0) {
                    cartShops.remove(position);
                    cartAdapterRecyclerView.notifyItemRemoved(position);
                    cartAdapterRecyclerView.notifyDataSetChanged();
                }

                int sumOfCostNew=0;
                for (CartItem cartShop: cartShops){
                    sumOfCostNew += cartShop.getFood().getPrice().getValue() * cartShop.getNumber();
                }

                CostTotal.setText(
                        String.format("%,d",sumOfCostNew) + " " + getString(R.string.currency));
                listCart.callBackActivity(cartShops);
            }
        });

        TextView tvSum = rootView.findViewById(R.id.tvResult);
        Price priceTotal = new Price(sumOfCost);
        tvSum.setText(String.format("%,d",priceTotal.getValue()) + " " + getString(R.string.currency));

        Button Order = rootView.findViewById(R.id.btnOrderFood);
        Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CartInformationShipping.class);
                intent.putExtra(getActivity().getString(R.string.cart_items), (Serializable) cartShops);
                intent.putExtra(getActivity().getString(R.string.res_id),resID);
                intent.putExtra("Info", (Serializable) cartShops);
                startActivity(intent);
            }
        });


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