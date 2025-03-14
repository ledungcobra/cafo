package com.ledungcobra.cafo.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.fragments.DashboardFragment;
import com.ledungcobra.cafo.fragments.DriverDetailOrderFragment;
import com.ledungcobra.cafo.fragments.DriverFragmentDetailFoodInOrder;
import com.ledungcobra.cafo.fragments.DriverOrdersFragment;
import com.ledungcobra.cafo.fragments.OrderViewPager;
import com.ledungcobra.cafo.fragments.ProfileUserFragment;
import com.ledungcobra.cafo.models.order.shipper.DetailOrderResponse;
import com.ledungcobra.cafo.view_adapter.DrawerAdapter;
import com.ledungcobra.cafo.view_adapter.DrawerItem;
import com.ledungcobra.cafo.view_adapter.SimpleItem;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class DriverScreen extends AppCompatActivity implements
        DrawerAdapter.OnItemSelectedListener,
        OrderViewPager.OrderViewPagerCallback,
        DriverOrdersFragment.CallBackToCreateFm {
    //VIEW
    private Drawable[] screenIcons;
    private SlidingRootNav slidingRootNav;

    //DATA
    private static final int POS_DASHBOARD = 0;
    private static final int POS_PROFILE = 1;
    private static final int POS_ORDERS = 2;
    private static final int POS_LOGOUT = 3;
    private String[] screenTitles ;

    private final int REQUEST_CODE = 9999;
    //LISTENER

    //INTERFACE


    private void showFragment(Fragment fragment) {
        if (fragment == null) return;
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_screen);
        screenTitles = new String[]{getString(R.string.dashboard), getString(R.string.profile), getString(R.string.your_orders),getString(R.string.logout)};
        initUI(savedInstanceState);

    }

    private void initUI(Bundle savedInstanceState) {
        //Setting up SlidingRootNav
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withDragDistance(100)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(true)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.drawer_menu_left)
                .inject();
        screenIcons = loadScreenIcons();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_DASHBOARD).setChecked(true),
                createItemFor(POS_PROFILE),
                createItemFor(POS_ORDERS),
                createItemFor(POS_LOGOUT)
        ));

        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        adapter.setSelected(POS_DASHBOARD);

        requestPermission();

    }


    private boolean requestPermission() {
        boolean wasEnabledNavigationLocation = checkForEnabledLocation();

        Log.d("PERMISSION", "requestPermission: "+ wasEnabledNavigationLocation);


        if (wasEnabledNavigationLocation) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            }
        } else {
            // notify user
            new AlertDialog.Builder(this)
                    .setMessage("Navigation has not been enabled yet")
                    .setPositiveButton("Open Location Setting", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
        return wasEnabledNavigationLocation;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE && grantResults.length == 2 && grantResults[1] == PERMISSION_GRANTED && grantResults[0] == PERMISSION_GRANTED) {
            Toast.makeText(this, getString(R.string.permission_granted), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkForEnabledLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }


        return gps_enabled;
    }


    @Override
    public void onItemSelected(int position) {
        Fragment fragment = null;

        if (position == POS_LOGOUT) {
            finish();
        } else if (position == POS_ORDERS) {
            //TODO: Order accepted
            fragment = DriverOrdersFragment.newInstance();
        } else if (position == POS_PROFILE) {
            fragment = ProfileUserFragment.newInstance();
        } else if (position == POS_DASHBOARD) {
            fragment = DashboardFragment.getInstance(screenTitles[position]);
        }

        slidingRootNav.closeMenu();
        showFragment(fragment);
    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.colorPrimary))
                .withTextTint(color(R.color.colorPrimaryDark))
                .withSelectedIconTint(color(R.color.colorPrimary))
                .withSelectedTextTint(color(R.color.colorPrimaryDark));
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @Override
    public void onSelectedOrder(DetailOrderResponse detailOrderResponse) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container, DriverFragmentDetailFoodInOrder.newInstance(detailOrderResponse));
        ft.addToBackStack(null);
        ft.commit();

    }

    //DriverOrdersFragment callback
    @Override
    public void onCreateFm(DetailOrderResponse detail) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.animation_enter, R.anim.animation_example, R.anim.animation_enter, R.anim.animation_example)
                .add(R.id.container, DriverFragmentDetailFoodInOrder.newInstance(detail))
                .addToBackStack(null)
                .commit();
    }
    //DriverOrdersFragment callback
}