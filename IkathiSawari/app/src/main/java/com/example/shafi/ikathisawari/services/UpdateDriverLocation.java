package com.example.shafi.ikathisawari.services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.shafi.ikathisawari.models.RiderRidePointsDriver;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateDriverLocation extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;

    private static final String TAG = "UpdateDriverLocation";

    public UpdateDriverLocation() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"startt");
        if(true){
            Log.d(TAG,"sss "+11);
//            final DatabaseReference dbReference= FirebaseDatabase.getInstance().getReference("test").child("testt");
//            dbReference.setValue("111");


        }
//        for(int i = 0;i<10;i++){
//
//            Log.d(TAG,"sss "+i);
//            for(int j = 0;j<100000000;j++){
//
//
//            }
//        }
//        buildGoogleApiClient();
        Toast.makeText(this, "Service update location started", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"start");
//        return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"Destroy");
        Toast.makeText(this, "Service update location stopped", Toast.LENGTH_SHORT).show();
    }

    protected synchronized void buildGoogleApiClient() {
        Log.d(TAG,"buildApiclient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG,"connected");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG,"location change");

        mLastLocation = location;
//        mMap.clear();
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        RiderRidePointsDriver points = new RiderRidePointsDriver(location.getLatitude(),location.getLongitude());
        Log.d(TAG,points.toString());
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
//
//        mMap.addMarker(new MarkerOptions().position(latLng));
//        Log.d("Updatee",""+ latLng.latitude +" "+ latLng.longitude);

        String currentDriver = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("DriverRidePoints").setValue(points);
    }
}
