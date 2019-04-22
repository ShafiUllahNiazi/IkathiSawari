package com.example.shafi.ikathisawari;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;

public class Driver_Home extends AppCompatActivity {

    private static final String TAG = "Driver_Home";
    private static final int PICKUP_PLACE_PICKER_REQUEST = 101;

    private Button home_btn,driverRouteMap;

    // google maps
    private static final int ERROR_DIALOGE_REQUEST = 9001;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1122;
    private final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private boolean mLocationPermissionGranted = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver__home);
        Intent intent = new Intent(this,MyIntentService.class);
        startService(intent);
        initializeViews();
    }

    private void initializeViews(){
        home_btn = (Button) findViewById(R.id.driverHome);
        driverRouteMap = findViewById(R.id.driverRouteMap);
    }

    @Override
    protected void onResume() {
        super.onResume();

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isServicesOK()){
                    if(isLocationEnabled()){
                        getLocationPermission();
                        if (mLocationPermissionGranted){
                           navigateToMap();
                        }
                    }
                }else {
                    showSettingAlert();
                }
            }
        });

        driverRouteMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (isServicesOK()){
//                    if(isLocationEnabled()){
//                        getLocationPermission();
//                        if (mLocationPermissionGranted){
//                            navigateToMapRoute();
//                        }
//                    }
//                }else {
//                    showSettingAlert();
//                }
                placePicker();

            }
        });
    }

    private void placePicker() {

        PlacePicker.IntentBuilder builder =  new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PICKUP_PLACE_PICKER_REQUEST);

        } catch (Exception e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKUP_PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                final Place pickUpPlace = PlacePicker.getPlace(this, data);
                Toast.makeText(this, ""+pickUpPlace.getLatLng().longitude, Toast.LENGTH_SHORT).show();


            }
        }
    }

    private void navigateToMapRoute() {
        startActivity(new Intent(Driver_Home.this,DriverMapRouteDirection.class));
    }

    public void navigateToMap() {
        startActivity(new Intent(Driver_Home.this,DriverHomeMap.class));
    }

    private boolean isServicesOK() {
       int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(Driver_Home.this);
        if (available == ConnectionResult.SUCCESS) {
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            // an error occured but we can resolve it
           Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(Driver_Home.this, available, ERROR_DIALOGE_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(Driver_Home.this, "You can't make map requests", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.sign_out){

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this,MainActivity.class));

            return true;
        }

        return true;

    }


}
