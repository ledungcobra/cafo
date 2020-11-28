package com.ledungcobra.cafo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ledungcobra.cafo.ui_calllback.RestaurantClickListener;
import com.ledungcobra.cafo.view_adapter.OverviewViewPagerAdapter;

public class RestaurantsOverviewScreen extends AppCompatActivity implements RestaurantClickListener {
//    RecyclerView recyclerView;
//    RestaurantOverviewItemAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ImageButton btnInfo;

    boolean isShowActionBar = true;

    public static String DATA_KEY = "DATA";
    public static String EXTRA_KEY = "RESTAURANT";
    int height = 0;

    MenuItem searchButton;
    MenuItem infoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retaurant_overview);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarOverviewTop);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getColor(R.color.white));
        toolbar.setTitleTextAppearance(this,R.style.titleToolbar);

        TabLayout tabLayout= findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.vpRestaurant);
        OverviewViewPagerAdapter viewPagerAdapter = new OverviewViewPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }




    @Override
    public void onClick(String restaurantID) {

        Intent intent = new Intent(RestaurantsOverviewScreen.this, RestaurantDetailScreen.class);


        intent.putExtra(EXTRA_KEY, restaurantID);

        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.shop_selected_menu, menu);
        searchButton = menu.findItem(R.id.menu_search);
        infoButton = menu.findItem(R.id.action_info);

        searchButton.setVisible(false);

        final SearchView searchView = (SearchView) searchButton.getActionView();
        View searchPlate= searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchPlate.setBackgroundColor(getColor(R.color.white));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();


                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id==R.id.action_info){

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}