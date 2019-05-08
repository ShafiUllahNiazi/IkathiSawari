package com.example.shafi.ikathisawari.controllers.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.shafi.ikathisawari.Driver_Profile;
import com.example.shafi.ikathisawari.MainActivity;
import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.Rider_Profile;

import com.example.shafi.ikathisawari.controllers.fragments.driver.DriverProfile;
import com.example.shafi.ikathisawari.controllers.fragments.rider.RiderHome1;
import com.example.shafi.ikathisawari.controllers.fragments.rider.RiderNotification;
import com.example.shafi.ikathisawari.controllers.fragments.rider.RiderRequestParent;
import com.example.shafi.ikathisawari.controllers.fragments.rider.RiderRequests;
import com.example.shafi.ikathisawari.controllers.fragments.rider.RiderRide;
import com.example.shafi.ikathisawari.services.RiderNotificationsService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;

public class Rider_Screen extends AppCompatActivity  implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;
    private static final String TAG = "Rider_Screen";
    private static final int ERROR_DIALOGE_REQUEST = 9001;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1122;
    private final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private boolean mLocationPermissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();
        setContentView(R.layout.rider__screen);



//        Intent intent = new Intent(this, RiderNotificationsService.class);
//        startService(intent);


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

        bottomNavigationView = findViewById(R.id.navigationRider);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home_rider);


    }

    RiderHome1 riderHome1 = new RiderHome1();
    RiderRequestParent riderRequestParent = new RiderRequestParent();

    RiderRequests riderRequests = new RiderRequests();
    RiderNotification riderNotification = new RiderNotification();
    RiderRide riderRide = new RiderRide();

    DriverProfile profile = new DriverProfile();

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
            case R.id.navigation_profile_rider:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.rider_container,profile).commit();
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


        if(item.getItemId()==R.id.sign_out){

            Intent intent = new Intent(this, RiderNotificationsService.class);
            stopService(intent);

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this,MainActivity.class));

            return true;
        }

        return true;

    }




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
