package com.example.shafi.ikathisawari.controllers.fragments.rider;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.models.RiderRidePointsDriver;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RiderRide extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    PolylineOptions lineOptions = null;

    ArrayList<LatLng> pointsDriverArrayList;

    public RiderRide() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_rider_ride, container, false);
        pointsDriverArrayList= new ArrayList<>();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapRiderRideFragment);

        if (mapFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.mapRiderRideFragment, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("DriverRidePoints");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//                if(dataSnapshot.exists()){
//
//                    dataSnapshot.toString();
//
//                    GenericTypeIndicator<ArrayList<RiderRidePointsDriver>> t = new GenericTypeIndicator<ArrayList<RiderRidePointsDriver>>() {};
//                    ArrayList<RiderRidePointsDriver> yourStringArray = dataSnapshot.getValue(t);
////                    Toast.makeText(getContext(),yourStringArray.get(0).toString(),Toast.LENGTH_LONG).show();
//
//                    Log.d("tststs",yourStringArray.toString());
//
//                    for (RiderRidePointsDriver item:yourStringArray) {
//                        LatLng latLng = new LatLng(item.getLat(),item.getLng());
//
//                        pointsDriverArrayList.add(latLng);
//
//
//                        Log.d("tststs","my "+item.toString());
//                    }
//
//                    mMap.clear();
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(pointsDriverArrayList.get(pointsDriverArrayList.size()-1)));
//                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//                    mMap.addMarker(new MarkerOptions().position(pointsDriverArrayList.get(pointsDriverArrayList.size()-1)));
//
//                    lineOptions = new PolylineOptions();
//                    lineOptions.addAll(pointsDriverArrayList);
//                    lineOptions.width(15);
//                    lineOptions.color(Color.BLUE);
//                    if (lineOptions != null) {
//
//
//                        mMap.addPolyline(lineOptions);
//                    }
//
//
//                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);
        if(menu.findItem(R.id.action_search) !=null ){
            MenuItem searchItem = menu.findItem(R.id.action_search);
            searchItem.setVisible(false);
        }


    }
}
