package com.example.shafi.ikathisawari.controllers.fragments.driver;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shafi.ikathisawari.DriverSignUp;
import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.RiderSignUp;
import com.example.shafi.ikathisawari.controllers.adapters.DriverRequestViewPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class DriverRequestParent extends Fragment {

    private TabLayout driver_request_tablayout;
    public ViewPager driver_request_viewPager;
    public DriverRequestViewPagerAdapter driverRequestViewPagerAdapter;
    View view;;


    public DriverRequestParent() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_driver_request_parent, container, false);
//        Toast.makeText(getActivity(), "req oncreate parent", Toast.LENGTH_SHORT).show();
        driverRequestViewPagerAdapter=new DriverRequestViewPagerAdapter(getChildFragmentManager());
        driverRequestViewPagerAdapter.addFragment(new DriverRequests(),"Current Ride");
        driverRequestViewPagerAdapter.addFragment(new DriverRequests(),"History");

        driver_request_tablayout = view.findViewById(R.id.driver_request_parent_tablayout);
        driver_request_viewPager = view.findViewById(R.id.driver_request_parent_viewPager);
        driver_request_viewPager.setAdapter(driverRequestViewPagerAdapter);
        driver_request_tablayout.setupWithViewPager(driver_request_viewPager);
//        if(driverRequestViewPagerAdapter== null) {
//            Toast.makeText(getActivity(), "Nulllllllllll", Toast.LENGTH_SHORT).show();
//            driverRequestViewPagerAdapter=new DriverRequestViewPagerAdapter(getActivity().getSupportFragmentManager());
//            driverRequestViewPagerAdapter.addFragment(new DriverRequests(),"No Response");
//            driverRequestViewPagerAdapter.addFragment(new DriverRequests(),"Accept/Reject");
//            driver_request_viewPager.setAdapter(driverRequestViewPagerAdapter);
//            driver_request_tablayout.setupWithViewPager(driver_request_viewPager);
//        }
//        else {
//            Toast.makeText(getActivity(), "not null", Toast.LENGTH_SHORT).show();
//            driver_request_viewPager.setAdapter(driverRequestViewPagerAdapter);
//            driver_request_tablayout.setupWithViewPager(driver_request_viewPager);
//
//        }
        return view;
    }
}