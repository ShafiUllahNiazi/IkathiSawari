package com.example.shafi.ikathisawari.controllers.fragments.rider;


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
import com.example.shafi.ikathisawari.controllers.adapters.RiderRequestsAdapter;
import com.example.shafi.ikathisawari.models.MakeRequest;
import com.example.shafi.ikathisawari.models.RidersRequestsListInDriver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;


public class RiderRequests extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RiderRequestsAdapter riderRequestsAdapter;
    ArrayList<RidersRequestsListInDriver> ridersRequestsListInDriver;
    View view;

    String currentRider;


    public RiderRequests() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_rider_requests, container, false);
        currentRider = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("requestsRiders").child("unseen").child(currentRider).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    Map<String,Object> requestss = (Map<String, Object>) dataSnapshot.getValue();
                    for (String req: requestss.keySet()){
                        Log.d("tttttttt", "requestss- " + requestss.get(req) );
//                        Map<String,Object> dd = (Map<String, Object>) myDrivers.get(driver);
//


                        FirebaseDatabase.getInstance().getReference().child("requestsRiders").child("seen").child(currentRider).child(req).setValue(requestss.get(req)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("tttttttt", "doneee"  );
                                FirebaseDatabase.getInstance().getReference().child("requestsRiders").child("unseen").child(currentRider).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d("tttttttt", "movee"  );
                                    }
                                });
                            }
                        });



                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("requestsRiders").child("seen").child(currentRider);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    ridersRequestsListInDriver =new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        MakeRequest makeRequest = snapshot.getValue(MakeRequest.class);
//                   RiderInfo rider = snapshot.getValue(RiderInfo.class);
                        RidersRequestsListInDriver riderRequestInDriver = new RidersRequestsListInDriver(snapshot.getKey(),makeRequest);
                        if(!(riderRequestInDriver.getMakeRequest().getStatus().equals("rejected"))){
                            ridersRequestsListInDriver.add(riderRequestInDriver);
                        }

                        Log.d("Time_Datess",snapshot.getKey()+" "+ makeRequest);
                    }
                    recyclerView = view.findViewById(R.id.recyclerViewRequestRiders);
                    layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);
                    riderRequestsAdapter = new RiderRequestsAdapter(getActivity(),getActivity().getSupportFragmentManager(), ridersRequestsListInDriver);


                    recyclerView.setAdapter(riderRequestsAdapter);

                    riderRequestsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

}
