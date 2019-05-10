package com.example.shafi.ikathisawari.controllers.fragments.driver;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
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
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DriverRide extends Fragment implements OnMapReadyCallback,RoutingListener {

    static final float COORDINATE_OFFSET = 0.00002f;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;

    ArrayList<LatLng> pointsDriverArrayList;
    PolylineOptions lineOptions = null;
    String currentDriver;
    LatLng latLng1;
    LatLng latLng2;

    ArrayList<RidersRequestsListInDriver> ridersRequestsListInDriver;


    private GoogleMap mMap;
    private Marker mMarker;

    Button stoppService;
    Button starttService;

    private RatingBar ratingBar;
    View view;

    ArrayList<LatLng> latLngArrayList;

    RatingBar driverRideDialog_rider_ratingBar2;
    int reqIndex;


    public DriverRide() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_driver_ride, container, false);


        pointsDriverArrayList = new ArrayList<>();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapDriverRideFragment);

        if (mapFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.mapDriverRideFragment, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);


        starttService = view.findViewById(R.id.startService);
        stoppService = view.findViewById(R.id.stopService);
        starttService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getActivity(), UpdateDriverLocation.class);
                intent2.putExtra("runService", 1);
                getActivity().startService(intent2);
                starttService.setVisibility(View.GONE);
                stoppService.setVisibility(View.VISIBLE);

            }
        });
        stoppService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent2 = new Intent(getActivity(), UpdateDriverLocation.class);
                intent2.putExtra("runService", 0);
                getActivity().stopService(intent2);
                starttService.setVisibility(View.VISIBLE);
                stoppService.setVisibility(View.GONE);


            }
        });


        return view;
    }


    boolean runService = false;


    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        latLngArrayList = new ArrayList<>();


//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        currentDriver = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("requests").child("seen").child(currentDriver);
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ridersRequestsListInDriver = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MakeRequest rider = snapshot.getValue(MakeRequest.class);
//                   RiderInfo rider = snapshot.getValue(RiderInfo.class);
                    RidersRequestsListInDriver riderRequestInDriver = new RidersRequestsListInDriver(snapshot.getKey(), rider);
                    if ((riderRequestInDriver.getMakeRequest().getStatus().equals("accepted"))) {
                        ridersRequestsListInDriver.add(riderRequestInDriver);
                        Log.d("Time1_Datess", snapshot.getKey() + " " + rider);
                    }

                    Log.d("Time_Datess", snapshot.getKey() + " " + rider);
                }

//


                for (final RidersRequestsListInDriver item : ridersRequestsListInDriver) {

                    latLng1 = new LatLng(item.getMakeRequest().getAvailableDriverInfo().getRiderOriginAtRoad().getLat(),
                            item.getMakeRequest().getAvailableDriverInfo().getRiderOriginAtRoad().getLng());
                    latLng2 = new LatLng(item.getMakeRequest().getAvailableDriverInfo().getRiderDestinationAtRoad().getLat(),
                            item.getMakeRequest().getAvailableDriverInfo().getRiderDestinationAtRoad().getLng());

                    while (markerAlreadyExist(latLngArrayList, latLng1)) {
                        latLng1 = new LatLng(item.getMakeRequest().getAvailableDriverInfo().getRiderOriginAtRoad().getLat() + COORDINATE_OFFSET,
                                item.getMakeRequest().getAvailableDriverInfo().getRiderOriginAtRoad().getLng() + COORDINATE_OFFSET);
                    }
                    while (markerAlreadyExist(latLngArrayList, latLng2)) {
                        latLng2 = new LatLng(item.getMakeRequest().getAvailableDriverInfo().getRiderDestinationAtRoad().getLat() + COORDINATE_OFFSET,
                                item.getMakeRequest().getAvailableDriverInfo().getRiderDestinationAtRoad().getLng() + COORDINATE_OFFSET);
                    }

                    latLngArrayList.add(latLng1);
                    latLngArrayList.add(latLng2);


//                    markerAlreadyExist(latLngArrayList,latLng1);

                    Marker mk = mMap.addMarker(new MarkerOptions()
                            .position(latLng1).title("origin")
                            .snippet(item.getMakeRequest().getAvailableDriverInfo().getRiderInfo().getName() + "  " +
                                    item.getMakeRequest().getAvailableDriverInfo().getRideCharges())

                    );
//                    Marker mk = mMap.addMarker(new MarkerOptions().position(it.getLatLng()).title(it.getName()).snippet(it.getAddress()));
                    mk.setTag(item.getDateAndTime());

                    Marker mk1 = mMap.addMarker(new MarkerOptions()
                            .position(latLng2)
                            .title("Destination")
                            .snippet(item.getMakeRequest().getAvailableDriverInfo().getRiderInfo().getName() + "  " +
                                    item.getMakeRequest().getAvailableDriverInfo().getRideCharges())

                    );
//                    Marker mk = mMap.addMarker(new MarkerOptions().position(it.getLatLng()).title(it.getName()).snippet(it.getAddress()));
                    mk1.setTag(item.getDateAndTime());




                }

                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {


                        final String id = (String) marker.getTag();
                        String markerTitle = marker.getTitle();
                        if (!(markerTitle.equals("Driver position"))) {

                            Toast.makeText(getActivity(), getRequestPos(id) + " k " + id, Toast.LENGTH_SHORT).show();
                            reqIndex = getRequestPos(id);
                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                            View mView = getLayoutInflater().inflate(R.layout.driver_ride_dialog, null);
                            final TextView name = mView.findViewById(R.id.dialog_nameRider);

                            TextView dialog_contactRider = mView.findViewById(R.id.dialog_contactRider);
                            TextView dialog_ageGenderRider = mView.findViewById(R.id.dialog_ageGenderRider);
                            TextView charges = mView.findViewById(R.id.dialogRidePrice);
                            TextView dialogRidetravellDistance = mView.findViewById(R.id.dialogRidetravellDistance);
                            driverRideDialog_rider_ratingBar2 = mView.findViewById(R.id.driverRideDialog_rider_ratingBar2);
                            Button button = mView.findViewById(R.id.dialogBtn);
                            name.setText(ridersRequestsListInDriver.get(reqIndex).getMakeRequest().getAvailableDriverInfo().getRiderInfo().getName());
                            dialog_contactRider.setText(ridersRequestsListInDriver.get(reqIndex).getMakeRequest().getAvailableDriverInfo().getRiderInfo().getMobile() + "");
                            dialog_ageGenderRider.setText(ridersRequestsListInDriver.get(reqIndex).getMakeRequest().getAvailableDriverInfo().getRiderInfo().getGender());
                            charges.setText("charges: " + ridersRequestsListInDriver.get(reqIndex).getMakeRequest().getAvailableDriverInfo().getRideCharges() + "");
                            dialogRidetravellDistance.setText("travelled Distance: " + ridersRequestsListInDriver.get(reqIndex).getMakeRequest().getAvailableDriverInfo().getTraveledDistanceRider() + "");
                            if (ridersRequestsListInDriver.get(reqIndex).getMakeRequest().isChargesReceived()) {
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
                                    final String currentRider = ridersRequestsListInDriver.get(reqIndex).getMakeRequest().getAvailableDriverInfo().getCurrent_Rider();
                                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("Rider").child(currentRider);
                                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                float ratingg = Float.valueOf(dataSnapshot.child("rating").getValue().toString());
                                                float newRating = driverRideDialog_rider_ratingBar2.getRating();
                                                float latestRating = (ratingg + newRating) / 2;
                                                databaseReference.child("rating").setValue(latestRating);
                                                FirebaseDatabase.getInstance().getReference().child("requests").child("seen").child(currentDriver).child(id).child("isChargesReceived").setValue(true);

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


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            Log.d("rrrrrr", "noooooooper");
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
//            Toast.makeText(getActivity(), "retttt", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("rrrrrr", "perrrrrr");
//        Toast.makeText(getActivity(), "nott", Toast.LENGTH_SHORT).show();

        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("DriverRidePoints").child(currentDriver);
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
                    if(mMarker!=null){
                        mMarker.remove();
                    }
                    BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.car);
                    Bitmap b=bitmapdraw.getBitmap();
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 34, 34, false);
                    mMarker = mMap.addMarker(new MarkerOptions()
                            .position(pointsDriverArrayList.get(pointsDriverArrayList.size() - 1))
                            .title("Driver position")
                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))

                    );




                    lineOptions = new PolylineOptions();
                    lineOptions.addAll(pointsDriverArrayList);
                    lineOptions.width(15);
                    lineOptions.color(Color.BLUE);
                    mMap.addPolyline(lineOptions);
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

    private boolean markerAlreadyExist(ArrayList<LatLng> latLngArrayList, LatLng latLngg) {
        if (latLngArrayList.contains(latLngg)) {
            return true;
        }
        return false;
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
        if (menu.findItem(R.id.action_search) != null) {
            MenuItem searchItem = menu.findItem(R.id.action_search);
            searchItem.setVisible(false);
        }


    }


    private int getRequestPos(String category) {
        for (int i = 0; i < this.ridersRequestsListInDriver.size(); ++i) {
            if (this.ridersRequestsListInDriver.get(i).getDateAndTime() == category) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light,R.color.colorPrimary};

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int i) {
        polylines = new ArrayList<>();
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(getResources().getColor(COLORS[1]));
        polyOptions.width(10);
        polyOptions.addAll(route.get(0).getPoints());
        Polyline polyline = mMap.addPolyline(polyOptions);
        polylines.add(polyline);
    }

    @Override
    public void onRoutingCancelled() {

    }

    private void makeRoute(LatLng start, LatLng end) {
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .key("AIzaSyBvR07aFM-1ddGVgt392lRnUge3weT6nUY")
                .withListener(this)
                .alternativeRoutes(false )
                .waypoints(start, end)
                .build();
        routing.execute();
    }
}
