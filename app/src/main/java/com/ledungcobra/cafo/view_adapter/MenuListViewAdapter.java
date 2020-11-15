package com.ledungcobra.cafo.view_adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.models.common.Food;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MenuListViewAdapter extends BaseAdapter {
    Context context;
    List<Food> foods;

    public MenuListViewAdapter(Context context,List<Food> foods)
    {
        this.context = context;
        this.foods = foods;

    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return foods.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.menu_item, null);

        ImageView ivFoodPhoto = row.findViewById(R.id.ivFoodPhoto);
        TextView tvFoodName = row.findViewById(R.id.tvFoodName);
        TextView tvFoodPrice = row.findViewById(R.id.tvFoodPrice);

        if (convertView == null) {
            Food food =foods.get(position) ;
            Picasso.get().load(food.getPhotos().get(0).getValue()).into(ivFoodPhoto);
            tvFoodName.setText(food.getName());
            tvFoodPrice.setText(food.getPrice().getValue()+"");
        } else {
            row = convertView;
        }

        return row;
    }
}
