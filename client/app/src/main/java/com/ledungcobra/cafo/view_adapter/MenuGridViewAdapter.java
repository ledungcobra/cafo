package com.ledungcobra.cafo.view_adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.models.common_new.Food;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MenuGridViewAdapter extends RecyclerView.Adapter<MenuGridViewAdapter.MenuGridViewHolder> {

    public interface OnItemClickListener {
        void onAddClick(int position);
    }
    Context context;
    List<Food> foods;

    OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public MenuGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View itemView = inflater.inflate(R.layout.menu_grid_item, parent, false);

        return new MenuGridViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MenuGridViewHolder holder, int position) {
        Food food = foods.get(position);
        String loadedImageURL = food.getImage().getValue();

        String useImageUrl = loadedImageURL.equals("https://images.foody.vn/default/s120x120/deli-dish-no-image.png") ? "https://www.bmihealthcare.co.uk/~/media/images/health-matters/editions/april-2020/greek-salad-recipe-blog-1.ashx?la=en" : loadedImageURL;

        Picasso.get().load(useImageUrl).transform(new CropCircleTransformation()).into(holder.ivFoodPhoto);
        holder.tvFoodName.setText(food.getName());
        holder.tvFoodPrice.setText(Integer.toString(food.getPrice().getValue()));


    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public void setOnClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    ;

    public MenuGridViewAdapter(@NonNull Context context, List<Food> foods) {
        this.context = context;
        this.foods = foods;
    }


    public class MenuGridViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivFoodPhoto;
        public TextView tvFoodName;
        public TextView tvFoodPrice;
        public ImageView ivAddCart;


        public MenuGridViewHolder(@NonNull View itemView) {
            super(itemView);

            ivFoodPhoto = itemView.findViewById(R.id.ivFoodPhotoGrid);
            tvFoodName = itemView.findViewById(R.id.tvNameFoodGrid);
            tvFoodPrice = itemView.findViewById(R.id.tvFoodPriceGrid);
            ivAddCart = itemView.findViewById(R.id.ivAddCartGrid);

            ivAddCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    onItemClickListener.onAddClick(position);

                    ObjectAnimator animatorX = ObjectAnimator.ofFloat(v, "scaleX",1,1.2f,1.4f,1.2f,1);
                    ObjectAnimator animatorY = ObjectAnimator.ofFloat(v, "scaleY",1,1.2f,1.4f,1.2f,1);

                    animatorX.setDuration(1000).setInterpolator(new AccelerateDecelerateInterpolator());
                    animatorY.setDuration(1000).setInterpolator(new AccelerateDecelerateInterpolator());

                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(animatorX,animatorY);
                    animatorSet.start();

                    onItemClickListener.onAddClick(position);
                }
            });
        }
    }

    public Food getFood(int position) {
        return foods.get(position);
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
        notifyDataSetChanged();
    }


}
