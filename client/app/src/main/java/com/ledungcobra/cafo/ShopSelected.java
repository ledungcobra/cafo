package com.ledungcobra.cafo;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;


public class ShopSelected extends AppCompatActivity {
    GridView gridView;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ShopAdapterRecycleView shopAdapterRecycleView;
    private boolean appBarExpanded = true;

    String[] items = {"Item1","Item2","Item3","Item4","Item5","Item6","Item7","Item8","Item7","Item8"};
    String[] address = {"ADDRItem1","ADDRItem2","ADDRItem3","ADDRItem4","ADDRItem5","ADDRItem6","ADDRItem7","ADDRItem8","ADDRItem7","ADDRItem8"};
    Integer[] thumbnails = {R.drawable.ic_launcher_background,R.drawable.ic_launcher_background,R.drawable.ic_launcher_background,R.drawable.ic_launcher_background
    ,R.drawable.ic_launcher_background,R.drawable.ic_launcher_background,R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,R.drawable.ic_launcher_background, R.drawable.ic_launcher_background};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_selected);
        //RecycleView
        recyclerView = findViewById(R.id.recycleView);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        shopAdapterRecycleView = new ShopAdapterRecycleView(this,thumbnails, items,address);

        recyclerView.setAdapter(shopAdapterRecycleView);

        //Toolbar action button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        setSupportActionBar(toolbar);
        //Title
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("CaFoe");
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.black));
        collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.black));

        //SearchView
        final SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });

        AppBarLayout appBarLayout=findViewById(R.id.appbar);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapsingToolbar.setTitle("Click");
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shop_selected_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id==R.id.action_info){
            Toast.makeText(ShopSelected.this, "Action clicked", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}