package com.ledungcobra.cafo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ledungcobra.cafo.R;
import com.ledungcobra.cafo.models.restaurants_new.BriefRestaurantInfo;
import com.ledungcobra.cafo.models.user.TrackingRestaurant;
import com.ledungcobra.cafo.service.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.ledungcobra.cafo.models.user.TrackingRestaurant.FAVORITE;
import static com.ledungcobra.cafo.models.user.TrackingRestaurant.VISITED;


public class RestaurantOverViewFragment extends Fragment {

    //VIEW
    private RestaurantOverViewFragment.OverviewViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;


    //DATA
    private MutableLiveData<ArrayList<BriefRestaurantInfo>> restaurantList = new MutableLiveData(new ArrayList<>());
    private MutableLiveData<ArrayList<BriefRestaurantInfo>> searchedRestaurants = new MutableLiveData<>(new ArrayList<BriefRestaurantInfo>());

    public RestaurantOverViewFragment() {
        // Required empty public constructor
    }

    public static RestaurantOverViewFragment newInstance() {
        RestaurantOverViewFragment fragment = new RestaurantOverViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        ArrayList<BriefRestaurantInfo> restaurantInfos = (ArrayList<BriefRestaurantInfo>) getArguments().getSerializable(getString(R.string.list_restaurants));
        restaurantList.setValue(restaurantInfos);

        restaurantList.observe(getActivity(), new Observer<ArrayList<BriefRestaurantInfo>>() {
            @Override
            public void onChanged(ArrayList<BriefRestaurantInfo> briefRestaurantInfos) {
                if (viewPagerAdapter != null) {
                    viewPagerAdapter.notifyDataSetChanged();
                }
            }
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_over_view, container, false);


        tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager viewPager = view.findViewById(R.id.vpRestaurant);

        viewPagerAdapter = new RestaurantOverViewFragment.OverviewViewPagerAdapter(getChildFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        return view;
    }


    public class OverviewViewPagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private Fragment searchingFragment = null;


        public OverviewViewPagerAdapter(@NonNull FragmentManager fm,
                                        int behavior
        ) {
            super(fm, behavior);

            fragments.add(RestaurantOverviewTabViewFragment.newInstance(restaurantList, RestaurantOverviewTabViewFragment.NEW_PAGER));

            fragments.add(RestaurantOverviewTabViewFragment.newInstance(filter(VISITED), RestaurantOverviewTabViewFragment.VISITED_PAGER));

            fragments.add(RestaurantOverviewTabViewFragment.newInstance(filter(FAVORITE), RestaurantOverviewTabViewFragment.FAV_PAGER));

        }

        public LiveData<ArrayList<BriefRestaurantInfo>> filter(final int type) {
            final MutableLiveData<ArrayList<BriefRestaurantInfo>> filterResult = new MutableLiveData<>(new ArrayList<BriefRestaurantInfo>());
            Repository.getInstance().getAllTrackingRestaurants().observe(getViewLifecycleOwner(), new Observer<List<TrackingRestaurant>>() {
                @Override
                public void onChanged(final List<TrackingRestaurant> trackingRestaurants) {
                    if (trackingRestaurants != null && trackingRestaurants.size() > 0) {
                        final List<BriefRestaurantInfo> filterData = new ArrayList<>();

                        restaurantList.observe(getViewLifecycleOwner(), new Observer<ArrayList<BriefRestaurantInfo>>() {
                            @Override
                            public void onChanged(ArrayList<BriefRestaurantInfo> briefRestaurantInfos) {
                                if (briefRestaurantInfos != null) {
                                    for (BriefRestaurantInfo briefRes : briefRestaurantInfos) {

                                        boolean exist = false;
                                        for (TrackingRestaurant res : trackingRestaurants) {

                                            if (res.getType() == type && res.getId().equals(briefRes.getId())) {
                                                exist = true;
                                                break;
                                            }

                                        }

                                        if (exist) {
                                            filterData.add(briefRes);

                                        }

                                    }
                                }
                                filterResult.setValue((ArrayList<BriefRestaurantInfo>) filterData);
                            }
                        });

                    }

                }
            });
            return filterResult;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);

        }

        @Override
        public int getCount() {
            return fragments.size();
        }


        // This is used for filling data to the tab of the view pager which named Searching
        public void setDataSearchingFragment(ArrayList<BriefRestaurantInfo> searchingData) {


            if (fragments.size() == 3) {
                searchingFragment = RestaurantOverviewTabViewFragment.newInstance(restaurantList, RestaurantOverviewTabViewFragment.SEARCHING_PAGER);
                ((RestaurantOverviewTabViewFragment) searchingFragment).setData(searchingData);
                fragments.add(searchingFragment);
            } else {
                ((RestaurantOverviewTabViewFragment) fragments.get(3)).setData(searchingData);
            }
            notifyDataSetChanged();


        }

        public void removeSearchingFragment() {

            if (fragments.size() == 4) {
                ((RestaurantOverviewTabViewFragment) searchingFragment).clearData();
                fragments.remove(fragments.size() - 1);
                notifyDataSetChanged();
            }

        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            if (position == 0) {
                return getString(R.string.new_tab);
            } else if (position == 1) {
                return getString(R.string.visited);
            } else if (position == 2) {
                return getString(R.string.favorite);
            } else if (position == 3) {
                return getString(R.string.searching);
            }

            return null;
        }


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //Inflate xml to android UI
        inflater.inflate(R.menu.shop_selected_menu, menu);

        //Get two components from action bar menu
        MenuItem searchButton = menu.findItem(R.id.menu_search);
        MenuItem infoButton = menu.findItem(R.id.action_info);

        //Implement searching
        final SearchView searchView = (SearchView) searchButton.getActionView();
        View searchPlate = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        //Change the background color for the palate search view
        searchPlate.setBackgroundColor(getActivity().getColor(R.color.white));

        //Text change listener for search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                Repository.getInstance().searchRestaurant(query, 1,5)
                        .observe(getViewLifecycleOwner(), new Observer<ArrayList<BriefRestaurantInfo>>() {
                                    @Override
                                    public void onChanged(ArrayList<BriefRestaurantInfo> briefRestaurantInfos) {
                                        viewPagerAdapter.setDataSearchingFragment(briefRestaurantInfos);
                                        tabLayout.getTabAt(3).select();
                                    }
                                }
                        );

                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() == 0) {
                    viewPagerAdapter.removeSearchingFragment();
                    tabLayout.getTabAt(0).select();
                }

                return false;
            }
        });

    }
}

