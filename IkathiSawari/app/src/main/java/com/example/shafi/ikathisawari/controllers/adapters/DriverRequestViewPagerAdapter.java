package com.example.shafi.ikathisawari.controllers.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class DriverRequestViewPagerAdapter extends FragmentPagerAdapter {
    public List<Fragment> myFragmentsList=new ArrayList<>();
    public List<String> myFragmentsNameList=new ArrayList<>();
    public DriverRequestViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return myFragmentsList.get(i);
    }

    @Override
    public int getCount() {
        return myFragmentsList.size();
    }

    public CharSequence getPageTitle(int position) {
        return myFragmentsNameList.get(position);
    }

    public void addFragment(Fragment myFragment,String myFragmentName){
        myFragmentsList.add(myFragment);
        myFragmentsNameList.add(myFragmentName);
    }
}
