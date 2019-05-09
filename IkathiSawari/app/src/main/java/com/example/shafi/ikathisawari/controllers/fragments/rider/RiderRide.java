package com.example.shafi.ikathisawari.controllers.fragments.rider;


import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.controllers.adapters.RiderRequestsAdapter;
import com.example.shafi.ikathisawari.models.MakeRequest;
import com.example.shafi.ikathisawari.models.RiderRidePointsDriver;
import com.example.shafi.ikathisawari.models.RidersRequestsListInDriver;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class RiderRide extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    PolylineOptions lineOptions = null;

    ArrayList<LatLng> pointsDriverArrayList;
    RatingBar driverRideDialog_rider_ratingBar2;
    private Marker mMarker;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RiderRequestsAdapter riderRequestsAdapter;
    ArrayList<RidersRequestsListInDriver> ridersRequestsListInDriver;
    String currentRider;

    int position;


    public RiderRide() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_rider_ride, container, false);
        ridersRequestsListInDriver = getArguments().getParcelableArrayList("ridersRequestsListInDriver");
        position = getArguments().getInt("position");


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

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("DriverRidePoints").child(ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getDriverKey());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    dataSnapshot.toString();

                    GenericTypeIndicator<ArrayList<RiderRidePointsDriver>> t = new GenericTypeIndicator<ArrayList<RiderRidePointsDriver>>() {
                    };
                    ArrayList<RiderRidePointsDriver> yourStringArray = dataSnapshot.getValue(t);
//                    Toast.makeText(getContext(),yourStringArray.get(0).toString(),Toast.LENGTH_LONG).show();

                    Log.d("tststs", yourStringArray.toString());

                    for (RiderRidePointsDriver item : yourStringArray) {
                        LatLng latLng = new LatLng(item.getLat(), item.getLng());

                        pointsDriverArrayList.add(latLng);


                        Log.d("tststs", "my " + item.toString());
                    }


//                    mMap.clear();
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(pointsDriverArrayList.get(pointsDriverArrayList.size() - 1)));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
                    mMap.clear();


                    LatLng latLng1 = new LatLng(ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getRiderOriginAtRoad().getLat(),
                            ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getRiderOriginAtRoad().getLng());
                    LatLng latLng2 = new LatLng(ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getRiderDestinationAtRoad().getLat(),
                            ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getRiderDestinationAtRoad().getLng());

                    Marker mMarker1 = mMap.addMarker(new MarkerOptions()
                            .position(latLng1)
                            .title("Your origin")
                            .snippet(ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getRiderInfo().getName() + "  " +
                                    ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getRideCharges())
                    );
                    mMarker1.setTag(ridersRequestsListInDriver.get(position).getDateAndTime());
//                    Toast.makeText(getActivity(), "taggg"+ridersRequestsListInDriver.get(position), Toast.LENGTH_SHORT).show();

                    Marker mMarker2 = mMap.addMarker(new MarkerOptions()
                            .position(latLng2)
                            .title("your destination")
                            .snippet(ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getRiderInfo().getName() + "  " +
                                    ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getRideCharges())
                    );
                    mMarker2.setTag(ridersRequestsListInDriver.get(position).getDateAndTime());

                    mMarker = mMap.addMarker(new MarkerOptions()
                            .position(pointsDriverArrayList.get(pointsDriverArrayList.size() - 1))
                            .title("Driver position")
                    );
                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {


                            final String id = (String) marker.getTag();
                            String markerTitle = marker.getTitle();
                            if (!(markerTitle.equals("Driver position"))) {


                                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                                View mView = getLayoutInflater().inflate(R.layout.driver_ride_dialog, null);
                                final TextView name = mView.findViewById(R.id.dialog_nameRider);

                                TextView dialog_contactRider = mView.findViewById(R.id.dialog_contactRider);
                                TextView dialog_ageGenderRider = mView.findViewById(R.id.dialog_ageGenderRider);
                                TextView charges = mView.findViewById(R.id.dialogRidePrice);
                                TextView dialogRidetravellDistance = mView.findViewById(R.id.dialogRidetravellDistance);
                                driverRideDialog_rider_ratingBar2 = mView.findViewById(R.id.driverRideDialog_rider_ratingBar2);
                                Button button = mView.findViewById(R.id.dialogBtn);
                                name.setText(ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getRiderInfo().getName());
                                dialog_contactRider.setText(ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getRiderInfo().getMobile() + "");
                                dialog_ageGenderRider.setText(ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getRiderInfo().getGender());
                                charges.setText("charges: " + ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getRideCharges() + "");
                                dialogRidetravellDistance.setText("travelled Distance: " + ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getTraveledDistanceRider() + "");
                                if (ridersRequestsListInDriver.get(position).getMakeRequest().isChargesReceived()) {
                                    button.setVisibility(View.GONE);
                                }

                                driverRideDialog_rider_ratingBar2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                    @Override
                                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                        Toast.makeText(getActivity(), "rating rride" + rating, Toast.LENGTH_SHORT).show();
                                    }
                                });

                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(getActivity(), "dialog btn", Toast.LENGTH_SHORT).show();
                                        final String currentRider = ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getCurrent_Rider();
                                        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("Rider").child(currentRider);
                                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    float ratingg = Float.valueOf(dataSnapshot.child("rating").getValue().toString());
                                                    float newRating = driverRideDialog_rider_ratingBar2.getRating();
                                                    float latestRating = (ratingg + newRating) / 2;
                                                    databaseReference.child("rating").setValue(latestRating);
                                                    FirebaseDatabase.getInstance().getReference().child("requests").child("seen").child(ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getDriverKey()).child(id).child("isChargesReceived").setValue(true);

                                                }


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                });

                                mBuilder.setView(mView);
                                AlertDialog dialog = mBuilder.create();
                                dialog.show();
                            }


                        }

                    });


                    lineOptions = new PolylineOptions();
                    lineOptions.addAll(pointsDriverArrayList);
                    lineOptions.width(15);
                    lineOptions.color(Color.BLUE);
                    mMap.addPolyline(lineOptions);
                    if (lineOptions != null) {


                        mMap.addPolyline(lineOptions);
                    }


                }

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
