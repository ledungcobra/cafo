package com.ledungcobra.cafo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.activity.RestaurantDetailScreen;
import com.ledungcobra.cafo.service.Repository;
import com.ledungcobra.cafo.models.restaurants_new.BriefRestaurantInfo;
import com.ledungcobra.cafo.models.user.TrackingRestaurant;
import com.ledungcobra.cafo.ui_calllback.RestaurantClickListener;
import com.ledungcobra.cafo.ui_calllback.UIThreadCallBack;
import com.ledungcobra.cafo.view_adapter.RestaurantOverviewItemAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.ledungcobra.cafo.activity.RestaurantsOverviewScreen.EXTRA_KEY;

public class RestaurantOverviewTabViewFragment extends Fragment {

    //VIEW
    RestaurantOverviewItemAdapter adapter;

    //DATA
    public static int NEW_PAGER = 0;
    public static int FAV_PAGER = 1;
    public static int VISITED_PAGER = 2;
    public static int SEARCHING_PAGER = 3;

    private int currentPage = 1;
    private int type;
    private MutableLiveData<ArrayList<BriefRestaurantInfo>> restaurantList = new MutableLiveData<>();
    private LiveData<ArrayList<BriefRestaurantInfo>> passedRestaurantList;

    public RestaurantOverviewTabViewFragment() {
    }

    public void setData(ArrayList<BriefRestaurantInfo> data){

        restaurantList.setValue(data);


    }

    public void clearData(){
        restaurantList.setValue(new ArrayList<BriefRestaurantInfo>());
    }


    public static RestaurantOverviewTabViewFragment newInstance(LiveData<ArrayList<BriefRestaurantInfo>> restaurantList, int type) {
        RestaurantOverviewTabViewFragment fragment = new RestaurantOverviewTabViewFragment();
        Bundle args = new Bundle();

        assert restaurantList != null;
        fragment.passedRestaurantList = restaurantList;
        fragment.setArguments(args);
        fragment.type = type;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_overview_visited_list, container, false);

        adapter = new RestaurantOverviewItemAdapter(getContext());

        final RecyclerView recyclerView = (RecyclerView) view;

        adapter.setOnRestaurantClickListener(new RestaurantClickListener() {
            @Override
            public void onClick(String restaurantID) {
                Intent intent = new Intent(getContext(), RestaurantDetailScreen.class);
                intent.putExtra(EXTRA_KEY, restaurantID);
                Repository.getInstance().insert(new TrackingRestaurant(restaurantID,TrackingRestaurant.VISITED));
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        if (type == NEW_PAGER) {

            recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {


                    if (oldScrollY < 0) {
                        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        int lastIndex = layoutManager.findLastVisibleItemPosition();

                        if (lastIndex == restaurantList.getValue().size() - 1) {
                            Repository.getInstance().fetchAllRestaurants(++currentPage, 10, new UIThreadCallBack<ArrayList<BriefRestaurantInfo>, Error>() {
                                @Override
                                public void stopProgressIndicator() {

                                }

                                @Override
                                public void startProgressIndicator() {

                                }

                                @Override
                                public void onResult(ArrayList<BriefRestaurantInfo> result) {

                                    ArrayList<BriefRestaurantInfo> currentRestaurantList = restaurantList.getValue();
                                    currentRestaurantList.addAll(result);

                                    restaurantList.setValue(currentRestaurantList);


                                }

                                @Override
                                public void onFailure(Error error) {

                                }
                            });
                        }

                    }

                }
            });

        }

        assert passedRestaurantList!=null;
        passedRestaurantList.observe(getViewLifecycleOwner(), new Observer<ArrayList<BriefRestaurantInfo>>() {
            @Override
            public void onChanged(ArrayList<BriefRestaurantInfo> briefRestaurantInfos) {
                restaurantList.setValue((ArrayList<BriefRestaurantInfo>) briefRestaurantInfos);

            }
        });

        if (restaurantList != null) {

            restaurantList.observe(getViewLifecycleOwner(), new Observer<List<BriefRestaurantInfo>>() {
                @Override
                public void onChanged(List<BriefRestaurantInfo> briefRestaurantInfos) {

                    if (briefRestaurantInfos != null && adapter != null) {
                        adapter.setRestaurants((ArrayList<BriefRestaurantInfo>) briefRestaurantInfos);

                    }
                }
            });

        }


        return view;
    }
}