package com.example.shafi.ikathisawari.controllers.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.example.shafi.ikathisawari.controllers.fragments.driver.DriverProfile;
import com.example.shafi.ikathisawari.controllers.fragments.driver.DriverRequestParent;
import com.example.shafi.ikathisawari.controllers.fragments.driver.DriverRequests;
import com.example.shafi.ikathisawari.controllers.fragments.driver.DriverRide;
import com.example.shafi.ikathisawari.directionhelpers.FetchURL;
import com.example.shafi.ikathisawari.services.DriverNotificationsService;
import com.example.shafi.ikathisawari.services.UpdateDriverLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;

public class Driver_Screen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener/*, DriverHome.OnRequirePoints */{

    BottomNavigationView bottomNavigationView;
    private int saveState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver__screen);
        Log.d(TAG,"creattt");
        bottomNavigationView = findViewById(R.id.navigationDriver);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
//        Toolbar toolbar = findViewById(R.id.driverToolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            Toast.makeText(this, "no per", Toast.LENGTH_SHORT).show();
            Log.d(TAG,"no perrrr");
            if (isServicesOK()){
                if(isLocationEnabled()){
                    getLocationPermission();

                }
            }else {
                showSettingAlert();
            }
            return;
        }

        Toast.makeText(this, " permiiiii", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"perrrr");



        Intent intent = new Intent(this, com.example.shafi.ikathisawari.services.DriverNotification.class);
        startService(intent);
//        Intent intent2 = new Intent(this, UpdateDriverLocation.class);
//        startService(intent2);
        Intent intent3 = new Intent(this, DriverNotificationsService.class);
        startService(intent3);


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
    DriverProfile driverProfile = new DriverProfile();
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
            case R.id.navigation_profile_rider:
//                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.driver_container,driverNotification).commit();
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.driver_container,driverProfile).commit();
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

    private static final int ERROR_DIALOGE_REQUEST = 9001;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1122;
    private final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private boolean mLocationPermissionGranted = false;
    private static final String TAG = "Driver_Screen";

    private boolean isServicesOK() {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (available == ConnectionResult.SUCCESS) {
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            // an error occured but we can resolve it
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, available, ERROR_DIALOGE_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void getLocationPermission(){
        Log.i(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION};
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "getLocationPermission: all permission granted");
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionsResult: called");

        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED ){
                    Log.i(TAG, "onRequestPermissionsResult: permission failed");
                    return;
                }
            }
            mLocationPermissionGranted = true;

            if(android.os.Build.VERSION.SDK_INT >= 11){
                Log.d(TAG, "onRequestPermissionsResult: in1");
                recreate();
            }else {
                Log.d(TAG, "onRequestPermissionsResult: in2");
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }



            Log.i(TAG, "onRequestPermissionsResult: permission granted");
        }
    }

    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
                !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            showSettingAlert();
            return false;
        }
        return true;
    }


    public void showSettingAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Confirm");
        alertDialog.setMessage("location?");
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}
