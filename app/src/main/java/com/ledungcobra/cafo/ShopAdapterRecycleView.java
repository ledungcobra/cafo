package com.ledungcobra.cafo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ShopAdapterRecycleView extends RecyclerView.Adapter<ShopAdapterRecycleView.MyViewHolder>{
     LayoutInflater mInflater;
     Integer [] thumbnails;
     String[] items;
     String[] address;

    public ShopAdapterRecycleView(Context context, Integer[] thumbnails, String[] items, String[] address) {
        this.mInflater = LayoutInflater.from(context);
        this.thumbnails = thumbnails;
        this.items = items;
        this.address = address;
    }

    @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.shop_item,parent,false);
        return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.imageView.setImageResource(thumbnails[position]);
            holder.txtView1.setText(items[position]);
            holder.txtView2.setText(address[position]);

        }

        @Override
        public int getItemCount() {
            return items.length;
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder{
            ImageView imageView;
            TextView txtView1;
            TextView txtView2;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.ivAvatar);
                txtView1 = itemView.findViewById(R.id.twNameShop);
                txtView2 = itemView.findViewById(R.id.tvAddress);

            }
        }
    }
