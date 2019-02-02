package com.example.shafi.ikathisawari;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class Sign_Up_View_Pager_Adapter extends FragmentPagerAdapter {

    public List<Fragment> myFragmentsList=new ArrayList<>();
    public List<String> myFragmentsNameList=new ArrayList<>();

    public Sign_Up_View_Pager_Adapter(FragmentManager fm){
        super(fm);

    }
    @Override
    public Fragment getItem(int position) {
        return myFragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return myFragmentsList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return myFragmentsNameList.get(position);
    }
    public void addFragment(Fragment myFragment,String myFragmentName){
        myFragmentsList.add(myFragment);
        myFragmentsNameList.add(myFragmentName);
    }
}
