package com.example.shafi.ikathisawari.controllers.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.shafi.ikathisawari.Driver_Profile;
import com.example.shafi.ikathisawari.MainActivity;
import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.Rider_Profile;

import com.example.shafi.ikathisawari.controllers.fragments.rider.RiderHome1;
import com.example.shafi.ikathisawari.controllers.fragments.rider.RiderNotification;
import com.example.shafi.ikathisawari.controllers.fragments.rider.RiderRequestParent;
import com.example.shafi.ikathisawari.controllers.fragments.rider.RiderRequests;
import com.example.shafi.ikathisawari.controllers.fragments.rider.RiderRide;
import com.google.firebase.auth.FirebaseAuth;

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

    RiderHome1 riderHome1 = new RiderHome1();
    RiderRequestParent riderRequestParent = new RiderRequestParent();

    RiderRequests riderRequests = new RiderRequests();
    RiderNotification riderNotification = new RiderNotification();
    RiderRide riderRide = new RiderRide();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.d("asdfg","clicked");
        switch (menuItem.getItemId()){
            case R.id.navigation_home_rider:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.rider_container,riderHome1).addToBackStack(null).commit();
//                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.rider_container,riderHome).addToBackStack(null).commit();
                return true;
            case R.id.navigation_requests_rider:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.rider_container,riderRequestParent).commit();
                return true;
            case R.id.navigation_notifications_rider:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.rider_container,riderNotification).commit();
                return true;
            case R.id.navigation_ride_rider:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.rider_container,riderRide).commit();
                return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.profile){


            startActivity(new Intent(this,Rider_Profile.class));

            return true;
        }
        if(item.getItemId()==R.id.sign_out){

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this,MainActivity.class));

            return true;
        }

        return true;

    }
}
