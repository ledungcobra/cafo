package com.ledungcobra.cafo.view_adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.models.common.Food;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MenuListViewAdapter extends RecyclerView.Adapter<MenuListViewAdapter.MenuViewHolder> {
    Context context;
    List<Food> foods;
    OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onAddClick(int position);
    }

    public void setOnClickListener(OnItemClickListener listener) {mListener = listener;}

    public MenuListViewAdapter(Context context, List<Food> foods) {
        this.context = context;
        this.foods = foods;

    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View itemView = inflater.inflate(R.layout.menu_item, parent, false);

        return new MenuViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
            Food food =foods.get(position) ;
            String loadedImageURL = food.getPhotos().get(0).getValue();

            String useImageUrl = loadedImageURL.equals("https://images.foody.vn/default/s120x120/deli-dish-no-image.png")?"https://www.bmihealthcare.co.uk/~/media/images/health-matters/editions/april-2020/greek-salad-recipe-blog-1.ashx?la=en":loadedImageURL;

            Picasso.get().load(useImageUrl).transform(new CropCircleTransformation()).into(holder.ivFoodPhoto);
            holder.tvFoodName.setText(food.getName());
            holder.tvFoodPrice.setText(food.getPrice().getValue()+"");
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }


    public class MenuViewHolder extends RecyclerView.ViewHolder {
        public  ImageView ivFoodPhoto;
        public TextView tvFoodName ;
        public TextView tvFoodPrice;
        public ImageView ivAddCart;

        public MenuViewHolder(@NonNull View row) {
            super(row);
            ivFoodPhoto = row.findViewById(R.id.ivFoodPhoto);
            tvFoodName = row.findViewById(R.id.tvFoodName);
            tvFoodPrice = row.findViewById(R.id.tvFoodPrice);
            ivAddCart = row.findViewById(R.id.ivAddCart);

            ivAddCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    mListener.onAddClick(position);
                }
            });
        }
    }
}
