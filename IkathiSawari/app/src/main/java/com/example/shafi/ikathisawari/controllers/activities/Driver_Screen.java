package com.example.shafi.ikathisawari.controllers.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.controllers.fragments.driver.DriverHome;
import com.example.shafi.ikathisawari.controllers.fragments.driver.DriverHome1;
import com.example.shafi.ikathisawari.controllers.fragments.driver.DriverNotification;
import com.example.shafi.ikathisawari.controllers.fragments.driver.DriverRequestParent;
import com.example.shafi.ikathisawari.controllers.fragments.driver.DriverRequests;
import com.example.shafi.ikathisawari.controllers.fragments.driver.DriverRide;
import com.example.shafi.ikathisawari.directionhelpers.FetchURL;

public class Driver_Screen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener/*, DriverHome.OnRequirePoints */{

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver__screen);
        bottomNavigationView = findViewById(R.id.navigationDriver);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home_rider);
    }

    DriverHome driverHome = new DriverHome();
    DriverHome1 driverHome1 = new DriverHome1();
//    DriverRequests driverRequests = new DriverRequests();
    DriverRequestParent driverRequests = new DriverRequestParent();
    DriverNotification driverNotification = new DriverNotification();
    DriverRide driverRide = new DriverRide();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.navigation_home_rider:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.driver_container,driverHome1).addToBackStack(null).commit();
                return true;
            case R.id.navigation_requests_rider:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.driver_container,driverRequests).commit();
                return true;
            case R.id.navigation_notifications_rider:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.driver_container,driverNotification).commit();
                return true;
            case R.id.navigation_ride_rider:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.driver_container,driverRide).commit();
                return true;

        }
        return false;
    }

//    @Override
//    public void onRequirePoints(String str1, String str2) {
//        new FetchURL(getApplicationContext()).execute(str1,str2);
//    }
}
