package com.example.shafi.ikathisawari.controllers.fragments.rider;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.controllers.adapters.DriverRequestViewPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class RiderRequestParent extends Fragment {

    private TabLayout rider_request_tablayout;
    public ViewPager rider_request_viewPager;
    public DriverRequestViewPagerAdapter riderRequestViewPagerAdapter;
    View view;


    public RiderRequestParent() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_rider_request_parent, container, false);

        riderRequestViewPagerAdapter=new DriverRequestViewPagerAdapter(getChildFragmentManager());
        riderRequestViewPagerAdapter.addFragment(new RiderRequests(),"Current Ride");
        riderRequestViewPagerAdapter.addFragment(new RiderRequestHistory(),"History");

        rider_request_tablayout = view.findViewById(R.id.rider_request_parent_tablayout);
        rider_request_viewPager = view.findViewById(R.id.rider_request_parent_viewPager);
        rider_request_viewPager.setAdapter(riderRequestViewPagerAdapter);
        rider_request_tablayout.setupWithViewPager(rider_request_viewPager);

        return view;
    }

}
