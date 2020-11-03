package com.ledungcobra.cafo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RestaurantOverviewItemAdapter
        extends RecyclerView.Adapter<RestaurantOverviewItemAdapter.MyViewHolder> {
    private Context context;
    private String[] nameDataset;
    private String[] addressDataset;

    RestaurantOverviewItemAdapter(Context mainActivity) {
        context = mainActivity;
        nameDataset = new String[] {"Tên quán", "Tên quán", "Tên quán", "Tên quán", "Tên quán",
            "Tên quán","Tên quán","Tên quán","Tên quán","Tên quán","Tên quán"};
        addressDataset = new String[] {"Địa chỉ", "Địa chỉ", "Địa chỉ", "Địa chỉ", "Địa chỉ",
                "Địa chỉ", "Địa chỉ","Địa chỉ","Địa chỉ","Địa chỉ","Địa chỉ"};
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_item, parent,false);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RestaurantDetailScreen.class);
                context.startActivity(intent);
            }
        });
        MyViewHolder vh = new MyViewHolder(layout);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtRestaurantName.setText(nameDataset[position]);
        holder.txtRestaurantAddress.setText(addressDataset[position]);
    }

    @Override
    public int getItemCount() {
        return nameDataset.length;
    }

    // Provide a reference to the views for each data item
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgRestaurant;
        public TextView txtRestaurantName;
        public TextView txtRestaurantAddress;
        public MyViewHolder(View v) {
            super(v);
            imgRestaurant = (ImageView) v.findViewById(R.id.imgRestaurant);
            txtRestaurantName = (TextView) v.findViewById(R.id.txtRestaurantName);
            txtRestaurantAddress = (TextView) v.findViewById(R.id.txtRestaurantAddress);
        }
    }
}
