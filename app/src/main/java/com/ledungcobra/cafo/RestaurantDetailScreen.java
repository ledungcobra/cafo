package com.ledungcobra.cafo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

public class RestaurantDetailScreen extends Activity {
    ListView lvMenu;
    ImageView ivLoc;
    ImageView ivDist;

    int[] foodPhotos;
    String[] foodNames;
    String[] foodPrices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail_screen);
        //TODO: SHOP INFO:{ HÌNH + TÊN _ SỐ điện thoại  }
        //TODO:
        foodPhotos = new int[] {
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher
        };
        foodNames = new String[] {
                "Tên món",
                "Tên món",
                "Tên món",
                "Tên món",
                "Tên món",
                "Tên món",
                "Tên món",
                "Tên món"
        };
        foodPrices = new String[] {
                "100.000 VND",
                "100.000 VND",
                "100.000 VND",
                "100.000 VND",
                "100.000 VND",
                "100.000 VND",
                "100.000 VND",
                "100.000 VND"
        };

        lvMenu = findViewById(R.id.lvMenu);
        lvMenu.setAdapter(new MenuListViewAdapter(this, foodPhotos, foodNames, foodPrices));
        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Do something
            }
        });

        ivLoc = findViewById(R.id.ivLoc);
        ivDist = findViewById(R.id.ivDist);
        ivLoc.setImageResource(R.drawable.location);
        ivDist.setImageResource(R.drawable.cursor);
    }
}