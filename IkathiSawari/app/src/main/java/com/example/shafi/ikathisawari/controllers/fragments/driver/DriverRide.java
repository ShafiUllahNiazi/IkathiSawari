package com.example.shafi.ikathisawari.controllers.fragments.driver;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.controllers.adapters.CustomInfoWindowAdapter;
import com.example.shafi.ikathisawari.controllers.adapters.DriverPaymentAdapter;
import com.example.shafi.ikathisawari.models.MakeRequest;
import com.example.shafi.ikathisawari.models.RiderRidePointsDriver;
import com.example.shafi.ikathisawari.models.RidersRequestsListInDriver;
import com.example.shafi.ikathisawari.services.UpdateDriverLocation;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
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
public class DriverRide extends Fragment implements OnMapReadyCallback {

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;

    ArrayList<LatLng> pointsDriverArrayList;
    PolylineOptions lineOptions = null;
    String currentDriver;

    ArrayList<RidersRequestsListInDriver> ridersRequestsListInDriver;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    DriverPaymentAdapter driverPaymentAdapter;


    private GoogleMap mMap;
    private Marker mMarker;

    Button stoppService;

    private RatingBar ratingBar;
    View view;


    public DriverRide() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_driver_ride, container, false);



        currentDriver = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("requests").child("seen").child(currentDriver);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ridersRequestsListInDriver =new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MakeRequest rider = snapshot.getValue(MakeRequest.class);
//                   RiderInfo rider = snapshot.getValue(RiderInfo.class);
                    RidersRequestsListInDriver riderRequestInDriver = new RidersRequestsListInDriver(snapshot.getKey(),rider);
                    if((riderRequestInDriver.getMakeRequest().getStatus().equals("accepted"))){
                        ridersRequestsListInDriver.add(riderRequestInDriver);
                        Log.d("Time1_Datess",snapshot.getKey()+" "+ rider);
                    }

                    Log.d("Time_Datess",snapshot.getKey()+" "+ rider);
                }

                if (getActivity() != null) {
                    recyclerView = view.findViewById(R.id.driverRide_rider_recyclerView);
                    layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);
//        driverRequestsAdapter = new DriverRequestsAdapter(getActivity(),getActivity().getSupportFragmentManager(), driverRequestsList);


                    driverPaymentAdapter = new DriverPaymentAdapter(getActivity(),getActivity().getSupportFragmentManager(), ridersRequestsListInDriver);


                    recyclerView.setAdapter(driverPaymentAdapter);

                    driverPaymentAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








        pointsDriverArrayList= new ArrayList<>();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapDriverRideFragment);

        if (mapFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.mapDriverRideFragment, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);


        stoppService = view.findViewById(R.id.stopservice);
        stoppService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stoppService.getText().toString().equals("start ride")){
                    Intent intent2 = new Intent(getActivity(), UpdateDriverLocation.class);
                    getActivity().startService(intent2);
                    stoppService.setText("stop ride");
                }
                if(stoppService.getText().toString().equals("stop ride")){
                    Intent intent2 = new Intent(getActivity(), UpdateDriverLocation.class);
                    getActivity().stopService(intent2);
                }
//                Intent intent = new Intent(getActivity(),UpdateDriverLocation.class);
//                getActivity().stopService(intent);
//                ratingBar.setVisibility(View.VISIBLE);


            }
        });


        return view;
    }




    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            Log.d("rrrrrr","noooooooper");
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
//            Toast.makeText(getActivity(), "retttt", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("rrrrrr","perrrrrr");
//        Toast.makeText(getActivity(), "nott", Toast.LENGTH_SHORT).show();

        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);




        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("DriverRidePoints");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    dataSnapshot.toString();

                    GenericTypeIndicator<ArrayList<RiderRidePointsDriver>> t = new GenericTypeIndicator<ArrayList<RiderRidePointsDriver>>() {};
                    ArrayList<RiderRidePointsDriver> yourStringArray = dataSnapshot.getValue(t);
                    Toast.makeText(getContext(),yourStringArray.get(0).toString(),Toast.LENGTH_LONG).show();

                    Log.d("tststs",yourStringArray.toString());

                    for (RiderRidePointsDriver item:yourStringArray) {
                        LatLng latLng = new LatLng(item.getLat(),item.getLng());

                        pointsDriverArrayList.add(latLng);


                        Log.d("tststs","my "+item.toString());
                    }

                    ArrayList<String> s = new ArrayList<>();
                    s.add("1");
                    s.add("2");
                    s.add("3");

                    mMap.clear();
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(pointsDriverArrayList.get(pointsDriverArrayList.size()-1)));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                    mMarker = mMap.addMarker(new MarkerOptions()
                            .position(pointsDriverArrayList.get(pointsDriverArrayList.size()-1))
                    .title("abc")
                    );
                    onMarkerClicked();
                    mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getActivity(),s));

                    lineOptions = new PolylineOptions();
                    lineOptions.addAll(pointsDriverArrayList);
                    lineOptions.width(15);
                    lineOptions.color(Color.BLUE);
                    if (lineOptions != null) {


                        mMap.addPolyline(lineOptions);
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onStop() {
        super.onStop();
//        String currentDriver = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("DriverRidePoints");
//        databaseReference.removeValue();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);
        if(menu.findItem(R.id.action_search) !=null ){
            MenuItem searchItem = menu.findItem(R.id.action_search);
            searchItem.setVisible(false);
        }


    }

    public void onMarkerClicked() {
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
//                LatLng latLng = marker.getPosition();
//                Toast.makeText(getActivity(), "markerclicked "+latLng.latitude, Toast.LENGTH_SHORT).show();
                try{
                    if(mMarker.isInfoWindowShown()){
                        mMarker.hideInfoWindow();
                    }else{

                        mMarker.showInfoWindow();
                    }
                }catch (NullPointerException e){
//                    Log.e(TAG, "onClick: NullPointerException: " + e.getMessage() );
                }


                return true;
            }
        });
    }
}
