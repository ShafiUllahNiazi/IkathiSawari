package com.example.shafi.ikathisawari.controllers.activities;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shafi.ikathisawari.Driver_Profile;
import com.example.shafi.ikathisawari.MainActivity;
import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.controllers.fragments.driver.DriverHome;
import com.example.shafi.ikathisawari.controllers.fragments.driver.DriverHome1;
import com.example.shafi.ikathisawari.controllers.fragments.driver.DriverNotification;
import com.example.shafi.ikathisawari.controllers.fragments.driver.DriverRequestParent;
import com.example.shafi.ikathisawari.controllers.fragments.driver.DriverRequests;
import com.example.shafi.ikathisawari.controllers.fragments.driver.DriverRide;
import com.example.shafi.ikathisawari.directionhelpers.FetchURL;
import com.example.shafi.ikathisawari.services.DriverNotificationsService;
import com.example.shafi.ikathisawari.services.UpdateDriverLocation;
import com.google.firebase.auth.FirebaseAuth;

public class Driver_Screen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener/*, DriverHome.OnRequirePoints */{

    BottomNavigationView bottomNavigationView;
    private int saveState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver__screen);
//        Toolbar toolbar = findViewById(R.id.driverToolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);



        Intent intent = new Intent(this, com.example.shafi.ikathisawari.services.DriverNotification.class);
        startService(intent);
//        Intent intent2 = new Intent(this, UpdateDriverLocation.class);
//        startService(intent2);
        Intent intent3 = new Intent(this, DriverNotificationsService.class);
        startService(intent3);

        bottomNavigationView = findViewById(R.id.navigationDriver);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
//        bottomNavigationView.setSelectedItemId(R.id.navigation_home_rider);




        if(getIntent().getExtras() != null) {
            Toast.makeText(this, "EXXXXXXtra", Toast.LENGTH_SHORT).show();
            String orderNotification = getIntent().getStringExtra("orderNotification");

            if (orderNotification.equals("request")){
                Toast.makeText(this, "reqqqqqq", Toast.LENGTH_SHORT).show();
                bottomNavigationView.setSelectedItemId(R.id.navigation_requests_rider);
            }


        }else if(savedInstanceState!=null){
            bottomNavigationView.setSelectedItemId(saveState);
        }else{
            bottomNavigationView.setSelectedItemId(R.id.navigation_home_rider);
        }






    }

    DriverHome driverHome = new DriverHome();
    DriverHome1 driverHome1 = new DriverHome1();
//    DriverRequests driverRequests = new DriverRequests();
    DriverRequestParent driverRequests = new DriverRequestParent();
    DriverNotification driverNotification = new DriverNotification();
    DriverRide driverRide = new DriverRide();

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(saveState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        saveState = bottomNavigationView.getSelectedItemId();
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
//        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.profile){


            startActivity(new Intent(this,Driver_Profile.class));

            return true;
        }
        if(item.getItemId()==R.id.sign_out){

            Intent intent = new Intent(this, com.example.shafi.ikathisawari.services.DriverNotification.class);
            stopService(intent);
            Intent intent2 = new Intent(this, UpdateDriverLocation.class);
            stopService(intent2);
            Intent intent3 = new Intent(this, DriverNotificationsService.class);
            stopService(intent3);

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this,MainActivity.class));

            return true;
        }

        return true;

    }
}
