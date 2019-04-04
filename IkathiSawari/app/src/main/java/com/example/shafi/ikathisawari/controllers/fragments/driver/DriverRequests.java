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
import android.widget.Toast;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.controllers.adapters.DriverRequestsAdapter;
import com.example.shafi.ikathisawari.models.MakeRequest;
import com.example.shafi.ikathisawari.models.RiderInfo;
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

    View view;

    ArrayList<RidersRequestsListInDriver> ridersRequestsListInDriver;


    public DriverRequests() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
//        Toast.makeText(getActivity(), "OnSTae", Toast.LENGTH_SHORT).show();
        Log.d("SSSSSSSSSSSSS", "SSSSSSS");
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_driver_requests, container, false);
//        ridersRequestsListInDriver =new ArrayList<>();
        final String uid = FirebaseAuth.getInstance().getUid();
//        Toast.makeText(getActivity(), "req oncreate", Toast.LENGTH_SHORT).show();

//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child("Driver").child(uid).child("request");


        ///////////////////////////////////////////////////////////////////////////////



        final String currentDriver = FirebaseAuth.getInstance().getCurrentUser().getUid();


//        final String currentRequest= ridersRequestsListInDrivers.get(position).getDateAndTime();
        FirebaseDatabase.getInstance().getReference().child("requests").child("unseen").child(currentDriver).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    Map<String,Object> requestss = (Map<String, Object>) dataSnapshot.getValue();
                    for (String req: requestss.keySet()){
                        Log.d("tttttttt", "requestss- " + requestss.get(req) );
//                        Map<String,Object> dd = (Map<String, Object>) myDrivers.get(driver);
//


                        FirebaseDatabase.getInstance().getReference().child("requests").child("seen").child(currentDriver).child(req).setValue(requestss.get(req)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("tttttttt", "doneee"  );
                                FirebaseDatabase.getInstance().getReference().child("requests").child("unseen").child(currentDriver).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d("tttttttt", "movee"  );
                                    }
                                });
                            }
                        });



                    }

//
//                    MakeRequest riderInfo = dataSnapshot.getValue(MakeRequest.class);
//                    FirebaseDatabase.getInstance().getReference().child("requests").child("responded")
//                            .child(currentDriver).child(currentRequest).child("rejected").setValue(riderInfo)
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    FirebaseDatabase.getInstance().getReference().child("requests").child("pending")
//                                            .child(currentDriver).child(currentRequest)
//                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            Toast.makeText(getActivity(), "DOne", Toast.LENGTH_SHORT).show();
//                                            Toast.makeText(getActivity(), "DOne", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                }
//                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });











        //////////////////////////////////////////////////////////////////////











//        String currentDriver = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("requests").child("seen").child(currentDriver);
        databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               ridersRequestsListInDriver =new ArrayList<>();
               for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                   MakeRequest rider = snapshot.getValue(MakeRequest.class);
//                   RiderInfo rider = snapshot.getValue(RiderInfo.class);
                   RidersRequestsListInDriver riderRequestInDriver = new RidersRequestsListInDriver(snapshot.getKey(),rider);
                   if(!(riderRequestInDriver.getRiderInfo().getStatus().equals("rejected"))){
                       ridersRequestsListInDriver.add(riderRequestInDriver);
                   }

                   Log.d("Time_Datess",snapshot.getKey()+" "+ rider);
               }
               recyclerView = view.findViewById(R.id.recyclerViewRequestDrivers);
               layoutManager = new LinearLayoutManager(getActivity());
               recyclerView.setLayoutManager(layoutManager);
//        driverRequestsAdapter = new DriverRequestsAdapter(getActivity(),getActivity().getSupportFragmentManager(), driverRequestsList);


               driverRequestsAdapter = new DriverRequestsAdapter(getActivity(),getActivity().getSupportFragmentManager(), ridersRequestsListInDriver);


               recyclerView.setAdapter(driverRequestsAdapter);

               driverRequestsAdapter.notifyDataSetChanged();

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

    @Override
    public void onStop() {
//        Toast.makeText(getActivity(), "onStop req", Toast.LENGTH_SHORT).show();
        super.onStop();
    }

    @Override
    public void onDestroy() {
//        Toast.makeText(getActivity(), "onDestroy req", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
//        Toast.makeText(getActivity(), "resumme req", Toast.LENGTH_SHORT).show();
    }
}
