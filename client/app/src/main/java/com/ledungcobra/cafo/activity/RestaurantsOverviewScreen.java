package com.ledungcobra.cafo.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.fragments.ProfileUserFragment;
import com.ledungcobra.cafo.fragments.RestaurantOverViewFragment;
import com.ledungcobra.cafo.fragments.UserOrdersFragment;
import com.ledungcobra.cafo.fragments.fragmentDetailFoodInOrder;
import com.ledungcobra.cafo.models.order.shipper.Food;
import com.ledungcobra.cafo.models.restaurant_detail_new.RestaurantDetail;
import com.ledungcobra.cafo.models.restaurants_new.BriefRestaurantInfo;
import com.ledungcobra.cafo.service.Repository;
import com.ledungcobra.cafo.view_adapter.MenuNavigationDrawerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RestaurantsOverviewScreen extends AppCompatActivity implements  UserOrdersFragment.CallBacktoCreateFm {

    //VIEW
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;

    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction ft = fm.beginTransaction();

    //DATA
    public static String EXTRA_KEY = "RESTAURANT";
    public static String[] MENU_NAV_NAME = {"Home", "Profile", "Your orders","Logout"};
    public static Integer[] MENU_NAV_THUMB = {R.drawable.ic_baseline_home_24, R.drawable.ic_baseline_person_24, R.drawable.ic_baseline_list_alt_24,R.drawable.logout};




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

        //Initial View Element
        initUI();

        //CREATE A FRAGMENT TO HOLD VIEWPAGER
        //THEN PASS DATA WAS PASSED FROM MAIN ACTIVITY BY FETCHING DATA THROUGH API
        Fragment restaurantOverViewFragment = RestaurantOverViewFragment.newInstance();
        ArrayList<BriefRestaurantInfo> res = (ArrayList<BriefRestaurantInfo>) getIntent()
                .getSerializableExtra(getString(R.string.list_restaurants));
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.list_restaurants), res);
        restaurantOverViewFragment.setArguments(bundle);

        //Add the created fragment to backstack that behalf for the ui of this activity
        ft
                .add(R.id.OverViewLayout, restaurantOverViewFragment, "Overview")
                .addToBackStack("Overview")
                .commit();

    }


    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarOverviewTop);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getColor(R.color.white));
        toolbar.setTitleTextAppearance(this, R.style.titleToolbar);
        toolbar.setNavigationIcon(getDrawable(R.drawable.ic_baseline_dehaze_24));

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
                // close drawer when item is tappd
                drawerLayout.closeDrawers();


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


                } else if (position == 0) {
                    if (fm.findFragmentByTag("Overview") == null) {

                        Fragment restaurantOverViewFragment = RestaurantOverViewFragment.newInstance();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(getString(R.string.list_restaurants), getIntent().getSerializableExtra(getString(R.string.list_restaurants)));

                        restaurantOverViewFragment.setArguments(bundle);

                        ft = fm.beginTransaction();
                        ft.setCustomAnimations(R.anim.animation_enter, R.anim.animation_example, R.anim.animation_enter, R.anim.animation_example)
                                .replace(R.id.OverViewLayout, restaurantOverViewFragment, "Overview").addToBackStack("Overview").commit();
                    } else {
                        getSupportFragmentManager().popBackStack("Overview", 0);

                    }

                } else  if (position == 2) {


                    if (fm.findFragmentByTag("Your order") == null) {

                        ft = fm.beginTransaction();
                        ft.setCustomAnimations(R.anim.animation_enter, R.anim.animation_example, R.anim.animation_enter, R.anim.animation_example)
                                .replace(R.id.OverViewLayout, UserOrdersFragment.newInstance(), "Your Order").addToBackStack("Your Order").commit();
                    } else getSupportFragmentManager().popBackStack("Your Order", 0);

                }else if(position == 3){
                    finish();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate xml to android UI
        getMenuInflater().inflate(R.menu.shop_selected_menu, menu);

        //Get two components from action bar menu
        MenuItem searchButton = menu.findItem(R.id.menu_search);
        MenuItem infoButton  = menu.findItem(R.id.action_info);

        //Implement searching
        final SearchView searchView = (SearchView) searchButton.getActionView();
        View searchPlate = searchView.findViewById(R.id.search_src_text);

        //Change the background color for the palate search view
        searchPlate.setBackgroundColor(getColor(R.color.white));

        //Text change listener for search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();


                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(newText.length()>0){

                    Repository.getInstance().searchRestaurant(newText,1,10,RestaurantsOverviewScreen.this).observe(RestaurantsOverviewScreen.this, new Observer<ArrayList<RestaurantDetail>>() {
                        @Override
                        public void onChanged(ArrayList<RestaurantDetail> restaurantDetails) {
                            Log.d("SEARCHING", "onChanged: "+ restaurantDetails);
                        }
                    });
                }else{

                }

                return false;
            }
        });

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        //set onClick button About
        if (id == R.id.action_info) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fm.findFragmentByTag("Overview") == null) {

            Fragment restaurantOverViewFragment = RestaurantOverViewFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putSerializable(getString(R.string.list_restaurants), getIntent().getSerializableExtra(getString(R.string.list_restaurants)));

            restaurantOverViewFragment.setArguments(bundle);

            ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.animation_enter, R.anim.animation_example, R.anim.animation_enter, R.anim.animation_example)
                    .replace(R.id.OverViewLayout, restaurantOverViewFragment, "Overview").addToBackStack("Overview").commit();
        }
    }

    //CALLBACK to init new fragment from YourOrder
    @Override
    public void onCreateFm(List<Food> foods,Integer total) {

        ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.animation_enter, R.anim.animation_example, R.anim.animation_enter, R.anim.animation_example)
                .add(R.id.OverViewLayout, fragmentDetailFoodInOrder.newInstance(foods,total)).addToBackStack("Your Order").commit();    }
}