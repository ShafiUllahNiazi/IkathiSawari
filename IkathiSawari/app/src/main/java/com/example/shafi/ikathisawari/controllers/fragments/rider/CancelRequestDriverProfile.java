package com.example.shafi.ikathisawari.controllers.fragments.rider;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.models.MakeRequest;
import com.example.shafi.ikathisawari.models.RidersRequestsListInDriver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CancelRequestDriverProfile extends Fragment {

    private TextView driverName ;
    private Button cancelRequest;
    ArrayList<RidersRequestsListInDriver> ridersRequestsListInDriver;
    int position;
    private String currentRider;


    public CancelRequestDriverProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_cancel_request_driver_profile, container, false);
        ridersRequestsListInDriver = getArguments().getParcelableArrayList("ridersRequestsListInDriver");
        position = getArguments().getInt("position");
        driverName = view.findViewById(R.id.canceDriverName);
        cancelRequest = view.findViewById(R.id.cancel_request_review);
        driverName.setText(ridersRequestsListInDriver.get(position).getMakeRequest().getDriverInfo().getName());
        cancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentRider = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseDatabase.getInstance().getReference().child("requestsRiders").child("seen").child(currentRider).child(ridersRequestsListInDriver.get(position).getDateAndTime()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            MakeRequest makeRequest = dataSnapshot.getValue(MakeRequest.class);
                            FirebaseDatabase.getInstance().getReference().child("requestsRiders").child("history_rider").child(currentRider).child(ridersRequestsListInDriver.get(position).getDateAndTime()).setValue(makeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    FirebaseDatabase.getInstance().getReference().child("requestsRiders").child("seen").child(currentRider).child(ridersRequestsListInDriver.get(position).getDateAndTime()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    });
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        return view;
    }

}
