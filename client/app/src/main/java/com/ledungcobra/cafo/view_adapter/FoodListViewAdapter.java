package com.ledungcobra.cafo.view_adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ledungcobra.cafo.R;

import com.ledungcobra.cafo.models.common_new.CartItem;
import com.ledungcobra.cafo.models.order.shipper.Food;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodListViewAdapter extends RecyclerView.Adapter<FoodListViewAdapter.FoodViewHolder> {
    LayoutInflater mInflater;
    List<Food> foods;

    public FoodListViewAdapter(Context context, List<Food> foods) {
        this.mInflater = LayoutInflater.from(context);
        this.foods = foods;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_food_in_detail_order,parent,false);
        return new FoodViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        holder.bindData(foods.get(position),position);
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView tvSTT;
        TextView tvName;
        TextView tvNumber;
        TextView tvCost;
        ImageView ivFoodPhoto;


        public FoodViewHolder(@NonNull final View itemView) {
            super(itemView);

        }


        public void bindData(Food food, int serial){

            tvSTT = itemView.findViewById(R.id.tvSTT);
            tvName = itemView.findViewById(R.id.tvNameFoodCart);
            tvNumber = itemView.findViewById(R.id.tvNumberFood);
            tvCost = itemView.findViewById(R.id.tvCostFood);
            ivFoodPhoto = itemView.findViewById(R.id.ivFoodPhotoCart);

            tvName.setText(food.getName());
            String loadedImageURL = food.getImage().getValue();
            String useImageUrl = loadedImageURL.equals("https://images.foody.vn/default/s120x120/deli-dish-no-image.png")?"https://www.bmihealthcare.co.uk/~/media/images/health-matters/editions/april-2020/greek-salad-recipe-blog-1.ashx?la=en":loadedImageURL;
            Picasso.get().load(useImageUrl).into(ivFoodPhoto);

            tvNumber.setText(Integer.toString(food.getCount()));
            tvCost.setText(Integer.toString(food.getPrice().getValue()*food.getCount()));


            tvSTT.setText(Integer.toString(serial));



        }
    }
}
