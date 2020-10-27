package com.ledungcobra.cafo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuListViewAdapter extends BaseAdapter {
    Context context;
    int[] foodPhotos;
    String[] foodNames;
    String[] foodPrices;

    public MenuListViewAdapter(Context context, int[] foodPhotos, String[] foodNames, String[] foodPrices)
    {
        this.context = context;
        this.foodPhotos = foodPhotos;
        this.foodNames = foodNames;
        this.foodPrices = foodPrices;
    }

    @Override
    public int getCount() {
        return foodPhotos.length;
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
            ivFoodPhoto.setImageResource(foodPhotos[position]);
            tvFoodName.setText(foodNames[position]);
            tvFoodPrice.setText(foodPrices[position]);
        } else {
            row = convertView;
        }

        return row;
    }
}
