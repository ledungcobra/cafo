package com.ledungcobra.cafo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ledungcobra.cafo.sampledata.Photo;
import com.ledungcobra.cafo.sampledata.Restaurant;
import com.squareup.picasso.Picasso;

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

            holder.bindData(new Restaurant("10","20","google.com"
            ,new Photo[] {
                    new Photo("https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png",
                    100,100),
            },null,null,"Yes coffee",null,"Yes coffee"));

        }

        @Override
        public int getItemCount() {
            return items.length;
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView txtNameShop;
            TextView txtAddress;
            Restaurant restaurant;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
            }

            public void bindData(Restaurant restaurant){
                this.restaurant = restaurant;

                imageView = itemView.findViewById(R.id.ivAvatar);
                txtNameShop = itemView.findViewById(R.id.twNameShop);
                txtAddress = itemView.findViewById(R.id.tvAddress);

                txtNameShop.setText(restaurant.getName());
                txtAddress.setText(restaurant.getShortAddress());

                Picasso.get().load(restaurant.getPhotos()[0].getUrl()).into(imageView);
            }


        }


    }
