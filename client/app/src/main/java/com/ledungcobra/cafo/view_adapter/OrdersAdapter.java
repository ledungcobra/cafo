package com.ledungcobra.cafo.view_adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.models.common_new.CartItem;
import com.squareup.picasso.Picasso;

import java.util.List;

class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.CartViewHolder> {
    List<CartItem> cartShops;
    CartAdapterRecyclerView.OnItemClickListener mListener;
    int count = 1;


    public interface OnItemClickListener{
        void onAddClick(int position);
        void onRemove(int position);
    }
    public void setOnClickListener(CartAdapterRecyclerView.OnItemClickListener listener){mListener = listener;};


    public OrdersAdapter(List<CartItem> cartShops) {
        this.cartShops = cartShops;
    }

    @NonNull
    @Override
    public OrdersAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_cart_item,parent,false);

        return new OrdersAdapter.CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.bindData(cartShops.get(position), count);
        count++;

    }

    @Override
    public int getItemCount() {
        return cartShops.size();
    }



    public class CartViewHolder extends RecyclerView.ViewHolder{
        TextView tvSTT;
        TextView tvName;
        TextView tvNumber;
        TextView tvCost;
        ImageView ivFoodPhoto;
        ImageView ivPlus;
        ImageView ivSub;
        CartItem cartShop;

        public CartViewHolder(@NonNull final View itemView) {
            super(itemView);

            ivPlus = itemView.findViewById(R.id.ivPlusFood);
            ivSub = itemView.findViewById(R.id.ivMinusFood);
            //button add
            ivPlus.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    int numberNew=cartShops.get(position).getNumber()+1;
                    cartShops.get(position).setNumber(numberNew);

                    tvCost.setText(Integer.toString(numberNew*cartShop.getFood().getPrice().getValue()));
                    tvNumber.setText(Integer.toString(numberNew));

                    mListener.onAddClick(position);

                }
            });
            //button sub
            ivSub.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (Integer.parseInt(tvNumber.getText().toString()) > 0) {
                        int numberNew=cartShops.get(position).getNumber()-1;
                        tvCost.setText(Integer.toString(numberNew*cartShop.getFood().getPrice().getValue()));
                        tvNumber.setText(Integer.toString(numberNew));
                        cartShops.get(position).setNumber(numberNew);
                    }

                    mListener.onRemove(position);

                }
            });

        }

        @SuppressLint("SetTextI18n")
        public void bindData(CartItem cartShop, int serial){
            this.cartShop = cartShop;

            tvSTT = itemView.findViewById(R.id.tvSTT);
            tvName = itemView.findViewById(R.id.tvNameFoodCart);
            tvNumber = itemView.findViewById(R.id.tvNumberFood);
            tvCost = itemView.findViewById(R.id.tvCostFood);
            ivFoodPhoto = itemView.findViewById(R.id.ivFoodPhotoCart);

            tvName.setText(cartShop.getFood().getName());
            String loadedImageURL = cartShop.getFood().getImage().getValue();
            String useImageUrl = loadedImageURL.equals("https://images.foody.vn/default/s120x120/deli-dish-no-image.png")?"https://www.bmihealthcare.co.uk/~/media/images/health-matters/editions/april-2020/greek-salad-recipe-blog-1.ashx?la=en":loadedImageURL;
            Picasso.get().load(useImageUrl).into(ivFoodPhoto);

            tvNumber.setText(Integer.toString(cartShop.getNumber()));
            tvCost.setText(Integer.toString(cartShop.getFood().getPrice().getValue()*cartShop.getNumber()));


            tvSTT.setText(Integer.toString(serial));



        }
    }
}
