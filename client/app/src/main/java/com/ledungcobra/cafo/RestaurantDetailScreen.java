package com.ledungcobra.cafo;


import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ledungcobra.cafo.database.Repository;
import com.ledungcobra.cafo.models.common.CartShop;
import com.ledungcobra.cafo.models.common.Food;
import com.ledungcobra.cafo.models.common.Restaurant;
import com.ledungcobra.cafo.ui_calllback.UIThreadCallBack;
import com.ledungcobra.cafo.view_adapter.CartAdapterRecyclerView;
import com.ledungcobra.cafo.view_adapter.MenuGridViewAdapter;
import com.ledungcobra.cafo.view_adapter.MenuListViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;
import static com.ledungcobra.cafo.RestaurantsOverviewScreen.DATA_KEY;
import static com.ledungcobra.cafo.RestaurantsOverviewScreen.EXTRA_KEY;

public class RestaurantDetailScreen extends AppCompatActivity  implements ShoppingCartFragment.callBack{

    RecyclerView lvMenu;
    ImageView ivLoc;
    ImageView ivDist;
    MenuListViewAdapter adapter;
    MenuGridViewAdapter adapterGrid;
    ImageView ivRestaurant;
    TextView tvRestaurantName;
    LinearLayout phoneContainer;
    TextView tvRestaurantPhone;
    FragmentManager fm;
    List<CartShop>  cartShops;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail_screen);

        Intent intent = getIntent();
        String restaurantID = intent.getStringExtra(EXTRA_KEY);
        cartShops = (List<CartShop>) intent.getSerializableExtra("CartShop");
        if (cartShops==null){
            cartShops = new ArrayList<CartShop>();
        }
        final List<Food> foods;

        //adapter List
        adapter = new MenuListViewAdapter(this, new ArrayList<Food>());
        //button Add Food
        adapter.setOnClickListener(new MenuListViewAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(int position) {
                Toast toast = Toast.makeText(getApplicationContext(), "Added " + adapter.getFood(position).getName() , LENGTH_SHORT);
                toast.show();
                int sameFood = 0;
                for (CartShop cartShop : cartShops) {
                    if (cartShop.getFood().equals(adapter.getFood(position))) {
                        cartShop.setNumber(cartShop.getNumber() + 1);
                        sameFood++;
                    }
                }
                if (sameFood == 0) {
                    CartShop cartShop = new CartShop(adapter.getFood(position), 1);
                    cartShops.add(cartShop);
                }
            }
        });

        //Adapter Grid
        adapterGrid = new MenuGridViewAdapter(this, new ArrayList<Food>());
        //button Add Food
        adapterGrid.setOnClickListener(new MenuGridViewAdapter.OnItemClickListener() {
            public void onAddClick(int position) {
                Toast toast = Toast.makeText(getApplicationContext(), "Added " + adapter.getFood(position).getName() , LENGTH_SHORT);
                toast.show();
                int sameFood = 0;
                for (CartShop cartShop : cartShops) {
                    if (cartShop.getFood().equals(adapter.getFood(position))) {
                        cartShop.setNumber(cartShop.getNumber() + 1);
                        sameFood++;
                    }
                }
                if (sameFood == 0) {
                    CartShop cartShop = new CartShop(adapter.getFood(position), 1);
                    cartShops.add(cartShop);
                }
            }
        });

        lvMenu = findViewById(R.id.lvMenu);

        ivRestaurant = findViewById(R.id.ivRestaurantPhoto);

        tvRestaurantName = findViewById(R.id.tvRestaurantName);

        lvMenu.setLayoutManager(new LinearLayoutManager(this));
        lvMenu.setAdapter(adapter);

        tvRestaurantPhone = findViewById(R.id.tvRestaurantPhone);
        phoneContainer = findViewById(R.id.phoneContainer);
        phoneContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + tvRestaurantPhone.getText().toString()));
                startActivity(callIntent);
            }
        });


        ivLoc = findViewById(R.id.ivLoc);
        ivDist = findViewById(R.id.ivDist);

        LayoutInflater layoutInflater = getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.progress_indicator, null, false);
        final ProgressBar progressBar = view.findViewById(R.id.progress_circular);

        final ViewGroup detailViewGroup = ((ViewGroup) findViewById(R.id.restaurant_detail_view));


        Repository.getInstance().getRestaurant(restaurantID, new UIThreadCallBack<Restaurant, Error>() {
            @Override
            public void stopProgressIndicator() {
                detailViewGroup.removeView(view);
                detailViewGroup.addView(lvMenu);
            }

            @Override
            public void startProgressIndicator() {
                ((ViewGroup) lvMenu.getParent()).removeView(lvMenu);
                detailViewGroup.addView(view);
            }

            @Override
            public void onResult(final Restaurant result) {
                adapter.setFoods(result.getMenuId().getFoodsId());
                Picasso.get().load(result.getPhotos().get(0).getValue()).into(ivRestaurant);
                tvRestaurantName.setText(result.getName());

                findViewById(R.id.btnMap).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RestaurantDetailScreen.this, MapScreen.class);
                        intent.putExtra("lat", result.getPosition().get(0).getLatitude());
                        intent.putExtra("long", result.getPosition().get(0).getLongitude());

                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onFailure(Error error) {

            }
        });
        //Toolbar setup menu
        Toolbar toolbar = findViewById(R.id.toolbarDetail);
        setSupportActionBar(toolbar);
        //Transition from ListView to GridView and vice versa
        final ImageButton imgbtnList = findViewById(R.id.btnGrid);
        final boolean[] isListView = {true};
        imgbtnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isListView[0]) {
                    lvMenu.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                    adapterGrid.setFoods(adapter.getListFood());
                    lvMenu.setAdapter(adapterGrid);
                    isListView[0] = !isListView[0];
                    imgbtnList.setImageResource(R.drawable.ic_baseline_grid_on_24);

                } else {
                    lvMenu.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    lvMenu.setAdapter(adapter);
                    isListView[0] = !isListView[0];
                    imgbtnList.setImageResource(R.drawable.ic_baseline_list_24);
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_restaurant, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.shopCart){
            fm = getSupportFragmentManager();
            ArrayList<CartShop> al_Food;
            al_Food = new ArrayList<>(cartShops.size());
            al_Food.addAll(cartShops);

            Bundle bundleFragment = new Bundle();
            bundleFragment.putSerializable("ListFood",al_Food);
            FragmentTransaction ft_add = fm.beginTransaction();
            ft_add.setCustomAnimations(R.anim.animation_enter,R.anim.animation_example).replace(R.id.flrestaurant_detail_view, ShoppingCartFragment.
                    newInstance(bundleFragment))
                    .addToBackStack(null).commit();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void callBackActivity(List<CartShop> cartShopList) {
        cartShops = cartShopList;
    }

}