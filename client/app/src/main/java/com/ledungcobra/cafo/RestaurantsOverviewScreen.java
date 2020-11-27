package com.ledungcobra.cafo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;
import androidx.viewpager.widget.ViewPager;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.ledungcobra.cafo.database.Repository;
import com.ledungcobra.cafo.models.restaurants.RestaurantArray;
import com.ledungcobra.cafo.ui_calllback.OnAnimationEnd;
import com.ledungcobra.cafo.ui_calllback.RestaurantClickListener;
import com.ledungcobra.cafo.view_adapter.OverviewViewPagerAdapter;
import com.ledungcobra.cafo.view_adapter.RestaurantOverviewItemAdapter;

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
        setContentView(R.layout.retaurant_overview);
//        btnInfo = findViewById(R.id.btnInfo);
//        recyclerView = findViewById(R.id.restaurantOverviewRecyclerView);

//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new RestaurantOverviewItemAdapter(this);
//        recyclerView.setAdapter(adapter);




//        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                if (scrollY - oldScrollY > 0 &&isShowActionBar == true) {
//                    hideComponents();
//                } else if (scrollY - oldScrollY < 0 && isShowActionBar == false) {
//                    showComponents();
//                }
//            }
//        });

//        Repository.getInstance().getAllRestaurants().observe(this, new Observer<RestaurantArray>() {
//            @Override
//            public void onChanged(RestaurantArray restaurantArray) {
//                adapter.setRestaurants(restaurantArray.getRestaurants());
//            }
//        });
//        adapter.setOnRestaurantClickListener(this);


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


//    private void showComponents() {
//
//        View view = findViewById(R.id.searchView);
//        searchButton.setVisible(false);
//        slideView(view, 0, height,new OnAnimationEnd(){
//            @Override
//            public void onEnd()
//
//            {
//                isShowActionBar = true;
//            }
//        });
//
//
//    }
//
//    private void hideComponents() {
//
//
//        View view = findViewById(R.id.searchView);
//        height = view.getMeasuredHeight();
//        slideView(view, height, 0, new OnAnimationEnd() {
//            @Override
//            public void onEnd() {
//                searchButton.setVisible(true);
//                isShowActionBar = false;
//            }
//        });
//
//    }
//
//    public void slideView(final View view,
//                          int currentHeight,
//                          final int newHeight,
//                          final OnAnimationEnd callback
//                          ) {
//        ValueAnimator slideAnimator = ValueAnimator
//                .ofInt(currentHeight, newHeight)
//                .setDuration(1000);
//
//        /* We use an update listener which listens to each tick
//         * and manually updates the height of the view  */
//
//        slideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                int value = (Integer) animation.getAnimatedValue();
//
//                view.getLayoutParams().height = value;
//                view.requestLayout();
//
//                if(newHeight == value){
//                    callback.onEnd();
//                }
//
//            }
//        });
//
//        AnimatorSet animationSet = new AnimatorSet();
//        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
//        animationSet.play(slideAnimator);
//        animationSet.start();
//    }

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