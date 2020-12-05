package com.ledungcobra.cafo.activity;


import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.service.Repository;
import com.ledungcobra.cafo.fragments.RestaurantCategoryFoodFragment;
import com.ledungcobra.cafo.fragments.ShoppingCartFragment;
import com.ledungcobra.cafo.models.common_new.CartItem;
import com.ledungcobra.cafo.models.common_new.Food;
import com.ledungcobra.cafo.models.restaurant_detail_new.RestaurantDetail;
import com.ledungcobra.cafo.models.user.TrackingRestaurant;
import com.ledungcobra.cafo.ui_calllback.OnAnimationEnd;
import com.ledungcobra.cafo.ui_calllback.UIThreadCallBack;
import com.ledungcobra.cafo.view_adapter.FragmentCategoryCollectionAdapter;
import com.ledungcobra.cafo.view_adapter.MenuGridViewAdapter;
import com.ledungcobra.cafo.view_adapter.MenuListViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;
import static com.ledungcobra.cafo.activity.RestaurantsOverviewScreen.EXTRA_KEY;

;

public class RestaurantDetailScreen extends AppCompatActivity implements
        ShoppingCartFragment.callBack,
        RestaurantCategoryFoodFragment.DataUpdateListener {

    private static final String TAGKEO = "SCROLL";
    ImageView ivLoc;
    ImageView ivDist;
    MenuListViewAdapter adapter;
    MenuGridViewAdapter adapterGrid;
    ImageView ivRestaurant;
    TextView tvRestaurantName;
    LinearLayout phoneContainer;
    TextView tvRestaurantPhone;
    //TODO: fix bug Card view khi recycler view không thể kéo được
    //TODO: xử lí
    LinearLayout restaurantCard;
    ImageButton imgbtnList;

    FragmentManager fm;
    List<CartItem> cartShops;
    private boolean isShowCard = true;
    private MutableLiveData<Boolean> isListView = new MutableLiveData<>(false);

    private ArrayList<RecyclerView> listViewPagerRecyclerView = new ArrayList<>();


    int cardHeight = -100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail_screen);

        restaurantCard = findViewById(R.id.restaurantCard);


        Intent intent = getIntent();
        final String restaurantID = intent.getStringExtra(EXTRA_KEY);
        cartShops = (List<CartItem>) intent.getSerializableExtra("CartShop");
        ImageView imvLove = findViewById(R.id.imAddToFavorite);

        imvLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CALL_API", "add love");
                Repository.getInstance().insert(new TrackingRestaurant(restaurantID, TrackingRestaurant.FAVORITE));

            }
        });

        if (cartShops == null) {
            cartShops = new ArrayList<CartItem>();
        }
        //adapter List
        adapter = new MenuListViewAdapter(this, new ArrayList<Food>());
        //button Add Food
        adapter.setOnClickListener(new MenuListViewAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(int position) {
                Toast toast = Toast.makeText(getApplicationContext(), "Added " + adapter.getFood(position).getName(), LENGTH_SHORT);
                toast.show();
                int sameFood = 0;
                for (CartItem cartShop : cartShops) {
                    if (cartShop.getFood().equals(adapter.getFood(position))) {
                        cartShop.setNumber(cartShop.getNumber() + 1);
                        sameFood++;
                    }
                }
                if (sameFood == 0) {
                    CartItem cartShop = new CartItem(adapter.getFood(position), 1);
                    cartShops.add(cartShop);
                }
            }
        });

        //Adapter Grid
        adapterGrid = new MenuGridViewAdapter(this, new ArrayList<Food>());
        //button Add Food
        adapterGrid.setOnClickListener(new MenuGridViewAdapter.OnItemClickListener() {
            public void onAddClick(int position) {
                Toast toast = Toast.makeText(getApplicationContext(), "Added " + adapter.getFood(position).getName(), LENGTH_SHORT);
                toast.show();
                int sameFood = 0;
                for (CartItem cartShop : cartShops) {
                    if (cartShop.getFood().equals(adapter.getFood(position))) {
                        cartShop.setNumber(cartShop.getNumber() + 1);
                        sameFood++;
                    }
                }
                if (sameFood == 0) {
                    CartItem cartShop = new CartItem(adapter.getFood(position), 1);
                    cartShops.add(cartShop);
                }
            }
        });


        final TabLayout tabLayout = findViewById(R.id.categoryTabLayout);
        final ViewPager viewPager = findViewById(R.id.categoryViewPager);

        ivRestaurant = findViewById(R.id.ivRestaurantPhoto);
        tvRestaurantName = findViewById(R.id.tvRestaurantName);
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
        final ViewGroup detailViewGroup = ((ViewGroup) findViewById(R.id.restaurant_detail_view));


        Repository.getInstance().getRestaurant(restaurantID, new UIThreadCallBack<RestaurantDetail, Error>() {
            @Override
            public void stopProgressIndicator() {
                detailViewGroup.removeView(view);
                detailViewGroup.addView(viewPager);
            }

            @Override
            public void startProgressIndicator() {
                ((ViewGroup) viewPager.getParent()).removeView(viewPager);
                //Specify layout_width & height to fill the rest of the screen
                //(addView ignore child's XML layout_width & height)
                detailViewGroup.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }

            @Override
            public void onResult(final RestaurantDetail result) {
                //Menu theo loai

                final FragmentCategoryCollectionAdapter collectionAdapter = new FragmentCategoryCollectionAdapter(getSupportFragmentManager(),
                        FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, result.getMenu(),isListView);


                viewPager.setAdapter(collectionAdapter);
                tabLayout.setupWithViewPager(viewPager);


                Picasso.get().load(result.getImage().getValue()).into(ivRestaurant);
                tvRestaurantName.setText(result.getName());

                findViewById(R.id.btnMap).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(RestaurantDetailScreen.this, MapScreen.class);
                        intent.putExtra("lat", result.getPosition().getLatitude());
                        intent.putExtra("long", result.getPosition().getLongitude());

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
        toolbar.setTitleTextAppearance(this, R.style.titleToolbar);
//        Transition from ListView to GridView and vice versa
        imgbtnList = findViewById(R.id.btnGrid);

        imgbtnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isListView.getValue()){
                    imgbtnList.setImageResource(R.drawable.ic_baseline_list_24);

                }else{
                    imgbtnList.setImageResource(R.drawable.ic_baseline_dehaze_24);
                }
                isListView.setValue(!isListView.getValue());
            }
        });


    }

    public void slideView(final View view,
                          int currentHeight,
                          final int newHeight,
                          final OnAnimationEnd callback
    ) {
        ValueAnimator slideAnimator = ValueAnimator
                .ofInt(currentHeight, newHeight)
                .setDuration(1000);

        /* We use an update listener which listens to each tick
         * and manually updates the height of the view  */

        slideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();

                view.getLayoutParams().height = value;
                view.requestLayout();

                if (newHeight == value) {
                    callback.onEnd();
                }

            }
        });

        AnimatorSet animationSet = new AnimatorSet();
        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animationSet.play(slideAnimator);
        animationSet.start();
    }

    private void showComponents() {

        slideView(restaurantCard, 0, cardHeight, new OnAnimationEnd() {
            @Override
            public void onEnd() {
                ViewGroup.LayoutParams layoutParams = restaurantCard.getLayoutParams();

                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                restaurantCard.setLayoutParams(layoutParams);
            }
        });


    }


    private void hideComponents() {

        if (cardHeight == -100) {
            cardHeight = restaurantCard.getMeasuredHeight();
        }

        slideView(restaurantCard, cardHeight, 0, new OnAnimationEnd() {
            @Override
            public void onEnd() {

            }
        });

        ViewGroup.LayoutParams layoutParams = restaurantCard.getLayoutParams();
        layoutParams.height = 0;
        restaurantCard.setVisibility(View.INVISIBLE);
        restaurantCard.setLayoutParams(layoutParams);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_restaurant, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.shopCart) {
            for (int i = 0; i < cartShops.size(); i++) {
                for (int j = i + 1; j < cartShops.size(); j++) {
                    if (cartShops.get(i).getFood().equals(cartShops.get(j).getFood())) {
                        cartShops.get(i).setNumber(cartShops.get(i).getNumber() + 1);
                        cartShops.remove(j);
                        j--;
                    }
                }
            }
            fm = getSupportFragmentManager();
            ArrayList<CartItem> al_Food;
            al_Food = new ArrayList<>(cartShops.size());
            al_Food.addAll(cartShops);

            Bundle bundleFragment = new Bundle();
            bundleFragment.putSerializable("ListFood", al_Food);
            FragmentTransaction ft_add = fm.beginTransaction();
            ft_add.setCustomAnimations(R.anim.animation_enter, R.anim.animation_example).replace(R.id.flrestaurant_detail_view, ShoppingCartFragment.
                    newInstance(bundleFragment))
                    .addToBackStack(null).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void callBackActivity(List<CartItem> cartShopList) {
        this.cartShops = cartShopList;
    }


    @Override
    public void onDataUpdate(List<CartItem> mData) {
        cartShops.addAll(mData);

    }


    @Override
    public void onScrollChangeListener(final RecyclerView rvMenuFood) {
        rvMenuFood.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (Math.abs(scrollY - oldScrollY) < 400 && Math.abs(scrollY - oldScrollY) > 50) {
                    if (scrollY - oldScrollY > 0 && isShowCard == true) {
                        Log.d(TAGKEO, "Keo len: " + "Y old: " + oldScrollY + " Y: " + scrollY);
                        Animation animation = AnimationUtils.loadAnimation(RestaurantDetailScreen.this, R.anim.rotate_restaurant_card);
                        restaurantCard.startAnimation(animation);

                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                hideComponents();
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        isShowCard = !isShowCard;


                    } else if (scrollY - oldScrollY < 0 && isShowCard == false) {
                        Animation animation = AnimationUtils.loadAnimation(RestaurantDetailScreen.this, R.anim.rotate_restaurant_card_reverse);
                        boolean shouldMove = false;

                        if (isListView.getValue() == false) {
                            shouldMove = 0 == ((LinearLayoutManager) rvMenuFood.getLayoutManager()).findFirstVisibleItemPosition();

                        } else {
                            shouldMove = 0 == ((GridLayoutManager) rvMenuFood.getLayoutManager()).findFirstVisibleItemPosition();

                        }
                        if (shouldMove) {
                            restaurantCard.startAnimation(animation);
                            animation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                    showComponents();
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            isShowCard = !isShowCard;
                            shouldMove = !shouldMove;
                        }
                    }
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


}