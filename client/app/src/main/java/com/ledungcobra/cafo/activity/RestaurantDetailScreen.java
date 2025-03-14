package com.ledungcobra.cafo.activity;


import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.fragments.RestaurantCategoryFoodFragment;
import com.ledungcobra.cafo.fragments.ShoppingCartFragment;
import com.ledungcobra.cafo.models.common_new.CartItem;
import com.ledungcobra.cafo.models.common_new.Food;
import com.ledungcobra.cafo.models.restaurant_detail_new.RestaurantDetail;
import com.ledungcobra.cafo.models.user.TrackingRestaurant;
import com.ledungcobra.cafo.service.Repository;
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

public class    RestaurantDetailScreen extends AppCompatActivity implements
        ShoppingCartFragment.callBack,
        RestaurantCategoryFoodFragment.DataUpdateListener {

    //VIEW
    private TextView tvRestaurantAddress;
    private TextView tvRestaurantStatus;
    private ImageView ivLoc;
    private ImageView ivDist;
    private TextView tvTimeOpenOff;
    private MenuListViewAdapter adapter;
    private MenuGridViewAdapter adapterGrid;
    private ImageView ivRestaurant;
    private TextView tvRestaurantName;
    private TextView tvRestaurantPhone;
    private LinearLayout phoneContainer;
    private LinearLayout restaurantCard;
    private ImageButton imgbtnList;
    private FragmentManager fm;
    private ImageView imvLove;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;

    //DATA
    private static final String TAGKEO = "SCROLL";
    private List<CartItem> cartShops;
    private boolean isShowCard = true;
    private MutableLiveData<Boolean> isListView = new MutableLiveData<>(false);
    int cardHeight = -100;
    private String restaurantID;
    private MutableLiveData<Boolean> isFavoriteRestaurant = new MutableLiveData<>(false);
    private boolean needUpdate = false;


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail_screen);

        //Get passed data
        Intent intent = getIntent();
        restaurantID = intent.getStringExtra(EXTRA_KEY);
        cartShops = (List<CartItem>) intent.getSerializableExtra("CartShop");

        initUI();

        checkIfAFavoriteRestaurant();

        setListener();


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
                        FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, result.getMenu(), isListView);


                viewPager.setAdapter(collectionAdapter);
                tabLayout.setupWithViewPager(viewPager);

                if(result.getImage()!=null){
                    Picasso.get().load(result.getImage().getValue()).into(ivRestaurant);
                }

                tvRestaurantName.setText(result.getName());
                tvRestaurantAddress.setText(result.getAddress());
                //Only first number
                if(result.getPhones()!=null && result.getPhones().size()>0){
                    tvRestaurantPhone.setText(result.getPhones().get(0));
                }

                if (result.getOperating() != null) {
                    tvTimeOpenOff.setText(result.getOperating().toString());
                } else {
                    tvTimeOpenOff.setText("chưa cập nhật");

                }

                findViewById(R.id.btnMap).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new AlertDialog.Builder(RestaurantDetailScreen.this).
                                setTitle("Choose type of map do you want to use")
                                .setPositiveButton("Google Map (Recommended)", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + MapScreen.getAddress(RestaurantDetailScreen.this, result.getPosition().getLatitude(), result.getPosition().getLongitude()) + "&mode=d"));
                                        intent.setPackage("com.google.android.apps.maps");
                                        startActivity(intent);

                                    }
                                })
                                .setNegativeButton("Built-in map", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(RestaurantDetailScreen.this, MapScreen.class);
                                        intent.putExtra("lat", result.getPosition().getLatitude());
                                        intent.putExtra("long", result.getPosition().getLongitude());
                                        startActivity(intent);
                                    }
                                })
                                .create()
                                .show();


                    }
                });

            }

            @Override
            public void onFailure(Error error) {
                Toast.makeText(RestaurantDetailScreen.this, getString(R.string.cannot_get_restaurant), LENGTH_SHORT).show();
            }
        });


        isFavoriteRestaurant.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    imvLove.setImageDrawable(getDrawable(R.drawable.ic_like));
                } else {
                    imvLove.setImageDrawable(getDrawable(R.drawable.ic_heart));
                }
            }
        });

    }


    public void initUI() {
        restaurantCard = findViewById(R.id.restaurantCard);
        imvLove = findViewById(R.id.imAddToFavorite);
        //TODO: Bind views
        ivRestaurant = findViewById(R.id.ivRestaurantPhoto);
        tvRestaurantName = findViewById(R.id.tvRestaurantName);
        tvRestaurantAddress = findViewById(R.id.address_restaurant);
        tvRestaurantStatus = findViewById(R.id.timeOpenOff);
        tvRestaurantPhone = findViewById(R.id.tvRestaurantPhone);


        tabLayout = findViewById(R.id.categoryTabLayout);
        viewPager = findViewById(R.id.categoryViewPager);


        ivLoc = findViewById(R.id.ivLoc);
        ivDist = findViewById(R.id.ivDist);
        phoneContainer = findViewById(R.id.phoneContainer);
        toolbar = findViewById(R.id.toolbarDetail);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextAppearance(this, R.style.titleToolbar);
        imgbtnList = findViewById(R.id.btnGrid);
        tvTimeOpenOff = findViewById(R.id.timeOpenOff);


    }

    public void setListener() {

        imvLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFavoriteRestaurant.getValue()) {
                    Repository.getInstance().insert(new TrackingRestaurant(restaurantID, TrackingRestaurant.FAVORITE));
                } else {
                    Repository.getInstance().removeARestaurantFromFavList(restaurantID);
                }

                isFavoriteRestaurant.setValue(!isFavoriteRestaurant.getValue());


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

        phoneContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + tvRestaurantPhone.getText().toString()));
                startActivity(callIntent);
            }
        });

        //Toolbar setup menu

//        Transition from ListView to GridView and vice versa

        imgbtnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isListView.getValue()) {
                    imgbtnList.setImageResource(R.drawable.ic_baseline_list_24);

                } else {
                    imgbtnList.setImageResource(R.drawable.ic_baseline_dehaze_24);
                }
                isListView.setValue(!isListView.getValue());

                if (needUpdate == false) {
                    needUpdate = true;
                }

            }
        });

    }

    public void checkIfAFavoriteRestaurant() {

        LiveData<List<TrackingRestaurant>> trackedRestaurant = Repository.getInstance().getAllTrackingRestaurants();

        if (trackedRestaurant.getValue() != null) {
            for (TrackingRestaurant res : trackedRestaurant.getValue()) {

                if (res.getType() == TrackingRestaurant.FAVORITE && res.getId().equals(restaurantID)) {
                    isFavoriteRestaurant.setValue(true);
                    break;
                }

            }
        }

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
            bundleFragment.putString("resID", restaurantID);
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

                        if (rvMenuFood.getLayoutManager() != null) {

                            if (!isListView.getValue()) {

                                shouldMove = 0 == ((LinearLayoutManager) rvMenuFood.getLayoutManager()).findFirstVisibleItemPosition();

                            } else {
                                shouldMove = 0 == ((GridLayoutManager) rvMenuFood.getLayoutManager()).findFirstVisibleItemPosition();

                            }
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
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.need_update), needUpdate);
        setResult(1234, intent);
        finish();
    }


}