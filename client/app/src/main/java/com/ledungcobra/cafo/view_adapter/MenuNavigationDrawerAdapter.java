package com.ledungcobra.cafo.view_adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ledungcobra.cafo.R;

public class MenuNavigationDrawerAdapter extends RecyclerView.Adapter<MenuNavigationDrawerAdapter.MenuNavViewHolder> {
    LayoutInflater inflater;
    String[] name;
    Integer[] thumbnails;
    OnItemClickListener mlistener;

    public interface OnItemClickListener{
        void onClick(int position);
    }
    public void setOnClickListener(OnItemClickListener listener) {mlistener = listener;}

    public MenuNavigationDrawerAdapter(Context context, String[] name, Integer[] thumbnails) {
        this.inflater = LayoutInflater.from(context);
        this.name = name;
        this.thumbnails = thumbnails;
    }

    @NonNull
    @Override
    public MenuNavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.menu_navigation_drawer,parent, false);
        return new MenuNavViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MenuNavViewHolder holder, final int position) {
        holder.bind(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEbug","Messeger");
                v.startAnimation(new AlphaAnimation(1F,1.2F));
                mlistener.onClick(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return thumbnails.length;
    }


    public class MenuNavViewHolder extends RecyclerView.ViewHolder{
        TextView txtName;
        ImageView imgMenu;

        public MenuNavViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtMenuNav);
            imgMenu = itemView.findViewById(R.id.imgMenuNav);
        }

        public void bind(int position){


            txtName.setText(name[position]);
            imgMenu.setImageResource(thumbnails[position]);
        }
    }
}
