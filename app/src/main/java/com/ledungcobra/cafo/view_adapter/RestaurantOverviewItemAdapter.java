package com.ledungcobra.cafo.view_adapter;

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

import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.RestaurantDetailScreen;
import com.ledungcobra.cafo.models.restaurants.BriefRestaurantInfo;
import com.ledungcobra.cafo.ui_calllback.RestaurantClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RestaurantOverviewItemAdapter
        extends RecyclerView.Adapter<RestaurantOverviewItemAdapter.MyViewHolder> {
    private Context context;
    private List<BriefRestaurantInfo> restaurants = new ArrayList<>();
    private RestaurantClickListener clickListener;
    public RestaurantOverviewItemAdapter(Context mainActivity) {
        context = mainActivity;
    }
    public void setRestaurants(List<BriefRestaurantInfo> restaurants){
        this.restaurants = restaurants;
        notifyDataSetChanged();
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        BriefRestaurantInfo currentRestaurant = restaurants.get(position);

        holder.txtRestaurantName.setText(currentRestaurant.getName());
        holder.txtRestaurantAddress.setText(currentRestaurant.getSingleAddress());

        String imageURL =currentRestaurant.getPhotos().size()>0?currentRestaurant.getPhotos().get(0).getValue():"https://www.tibs.org.tw/images/default.jpg";
        Picasso.get().load(imageURL).into(holder.imgRestaurant);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(restaurants.get(position).getId());
            }
        });

    }


    // Provide a reference to the views for each data item
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgRestaurant;

        public TextView txtRestaurantName;
        public TextView txtRestaurantAddress;
        public ViewGroup item;
        public MyViewHolder(View v) {
            super(v);
            imgRestaurant = (ImageView) v.findViewById(R.id.imgRestaurant);
            txtRestaurantName = (TextView) v.findViewById(R.id.txtRestaurantName);
            txtRestaurantAddress = (TextView) v.findViewById(R.id.txtRestaurantAddress);
            item = v.findViewById(R.id.restaurant_item);

        }

    }
    public void setOnRestaurantClickListener(RestaurantClickListener listener){
        this.clickListener = listener;
    }
    @Override
    public int getItemCount() {
        return restaurants.size();
    }
}
