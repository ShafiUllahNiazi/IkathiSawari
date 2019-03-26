package com.example.shafi.ikathisawari.controllers.fragments.driver;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.controllers.adapters.DriverRequestsAdapter;
import com.example.shafi.ikathisawari.models.RiderInfo;
import com.example.shafi.ikathisawari.models.RidersRequestsListInDriver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DriverRequests extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DriverRequestsAdapter driverRequestsAdapter;

    ArrayList<RidersRequestsListInDriver> ridersRequestsListInDriver;


    public DriverRequests() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_driver_requests, container, false);
        ridersRequestsListInDriver =new ArrayList<>();
        final String uid = FirebaseAuth.getInstance().getUid();

//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child("Driver").child(uid).child("request");
        String currentDriver = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("requests").child(currentDriver);
        databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                   RiderInfo rider = snapshot.getValue(RiderInfo.class);
                   RidersRequestsListInDriver riderRequestInDriver = new RidersRequestsListInDriver(snapshot.getKey(),rider);
                   ridersRequestsListInDriver.add(riderRequestInDriver);
                   Log.d("Time_Datess",snapshot.getKey()+" "+ rider);
               }
               recyclerView = view.findViewById(R.id.recyclerViewRequestDrivers);
               layoutManager = new LinearLayoutManager(getActivity());
               recyclerView.setLayoutManager(layoutManager);
//        driverRequestsAdapter = new DriverRequestsAdapter(getActivity(),getActivity().getSupportFragmentManager(), driverRequestsList);


               driverRequestsAdapter = new DriverRequestsAdapter(getActivity(),getActivity().getSupportFragmentManager(), ridersRequestsListInDriver);

               recyclerView.setAdapter(driverRequestsAdapter);

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
//        recyclerView = view.findViewById(R.id.recyclerViewRequestDrivers);
//        layoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(layoutManager);
////        driverRequestsAdapter = new DriverRequestsAdapter(getActivity(),getActivity().getSupportFragmentManager(), driverRequestsList);
//
//
//        driverRequestsAdapter = new DriverRequestsAdapter(getActivity(), ridersRequestsListInDriver);
//
//        recyclerView.setAdapter(driverRequestsAdapter);

        return view;
    }

}
