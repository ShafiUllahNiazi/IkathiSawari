package com.example.shafi.ikathisawari.controllers.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.controllers.fragments.rider.RiderHome;
import com.example.shafi.ikathisawari.controllers.fragments.rider.RiderNotification;
import com.example.shafi.ikathisawari.controllers.fragments.rider.RiderRequests;

public class Rider_Screen extends AppCompatActivity  implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rider__screen);
        bottomNavigationView = findViewById(R.id.navigationRider);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home_rider);


    }

    RiderHome riderHome = new RiderHome();
    RiderRequests riderRequests = new RiderRequests();
    RiderNotification riderNotification = new RiderNotification();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.d("asdfg","clicked");
        switch (menuItem.getItemId()){
            case R.id.navigation_home_rider:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.rider_container,riderHome).addToBackStack(null).commit();
                return true;
            case R.id.navigation_requests_rider:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.rider_container,riderRequests).commit();
                return true;
            case R.id.navigation_notifications_rider:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.rider_container,riderNotification).commit();
                return true;
        }
        return false;
    }
}
