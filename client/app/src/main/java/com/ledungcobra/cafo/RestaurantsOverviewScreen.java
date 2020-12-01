package com.ledungcobra.cafo;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.ledungcobra.cafo.database.Repository;
import com.ledungcobra.cafo.fragments.ProfileUserFragment;
import com.ledungcobra.cafo.fragments.RestaurantOverViewFragment;
import com.ledungcobra.cafo.fragments.RestaurantOverviewNewFragment;
import com.ledungcobra.cafo.models.restaurants_new.BriefRestaurantInfo;
import com.ledungcobra.cafo.ui_calllback.RestaurantClickListener;
import com.ledungcobra.cafo.ui_calllback.UIThreadCallBack;
import com.ledungcobra.cafo.view_adapter.MenuNavigationDrawerAdapter;
import com.ledungcobra.cafo.view_adapter.OverviewViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RestaurantsOverviewScreen extends AppCompatActivity implements RestaurantClickListener, RestaurantOverviewNewFragment.fragmentCallBack {
    //    RecyclerView recyclerView;
//    RestaurantOverviewItemAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ImageButton btnInfo;

    boolean isShowActionBar = true;

    public static String DATA_KEY = "DATA";
    public static String EXTRA_KEY = "RESTAURANT";
    int height = 0;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    public static String[] MENU_NAV_NAME = {"Home", "Profile", "Contact"};
    public static Integer[] MENU_NAV_THUMB = {R.drawable.ic_baseline_home_24, R.drawable.ic_baseline_home_24, R.drawable.ic_baseline_home_24};

    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction ft = fm.beginTransaction();


    MenuItem searchButton;
    MenuItem infoButton;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retaurant_overview);

        final ArrayList<BriefRestaurantInfo> restaurantList = new ArrayList<BriefRestaurantInfo>();
        //get data
        Repository.getInstance().fetchAllRestaurants(1, 9, new UIThreadCallBack<ArrayList<BriefRestaurantInfo>, Error>() {
            @Override
            public void stopProgressIndicator() {

            }

            @Override
            public void startProgressIndicator() {

            }

            @Override
            public void onResult(ArrayList<BriefRestaurantInfo> result) {
                restaurantList.addAll(result);
                ft.add(R.id.OverViewLayout, RestaurantOverViewFragment.newInstance(restaurantList), "Overview").addToBackStack("Overview").commit();
            }

            @Override
            public void onFailure(Error error) {

            }
        });



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarOverviewTop);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getColor(R.color.white));
        toolbar.setTitleTextAppearance(this, R.style.titleToolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_dehaze_24));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        drawerLayout = findViewById(R.id.overviewDrawerLayout);
        NavigationView navigationView = findViewById(R.id.overviewNav);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // set item as selected to persist highlight
                item.setChecked(true);
                // close drawer when item is tapped
                drawerLayout.closeDrawers();

                // Add code here to update the UI based on the item selected
                // For example, swap UI fragments here

                return true;
            }
        });

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_app_bar_open_drawer_description, R.string.nav_app_bar_navigate_up_description);
        drawerLayout.addDrawerListener(drawerToggle);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        RecyclerView menuNavRecyclerView = findViewById(R.id.listMenuRecyclerView);
        MenuNavigationDrawerAdapter adapter = new MenuNavigationDrawerAdapter(getBaseContext(), MENU_NAV_NAME, MENU_NAV_THUMB);
        menuNavRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        menuNavRecyclerView.setAdapter(adapter);
        //MenuNavigationClick
        adapter.setOnClickListener(new MenuNavigationDrawerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                if (position == 1) {
                    if (fm.findFragmentByTag("Profile") == null) {
                        ft = fm.beginTransaction();
                        ft.setCustomAnimations(R.anim.animation_enter, R.anim.animation_example, R.anim.animation_enter, R.anim.animation_example)
                                .replace(R.id.OverViewLayout, ProfileUserFragment.newInstance(), "Profile").addToBackStack("Profile").commit();
                    } else getSupportFragmentManager().popBackStack("Profile", 0);
                }
                if (position == 0) {
                    if (fm.findFragmentByTag("Overview") == null) {
                        ft = fm.beginTransaction();
                        ft.setCustomAnimations(R.anim.animation_enter, R.anim.animation_example, R.anim.animation_enter, R.anim.animation_example)
                                .replace(R.id.OverViewLayout, RestaurantOverViewFragment.newInstance(restaurantList), "Overview").addToBackStack("Overview").commit();
                    } else {
                        getSupportFragmentManager().popBackStack("Overview", 0);

                    }

                }
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
        View searchPlate = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
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

        if (id == R.id.action_info) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNavigateToOverviewScreen(String restaurantID) {

    }
}