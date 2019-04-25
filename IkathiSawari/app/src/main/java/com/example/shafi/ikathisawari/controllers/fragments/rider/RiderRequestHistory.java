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
import com.example.shafi.ikathisawari.controllers.adapters.RiderHistoryAdapter;
import com.example.shafi.ikathisawari.models.MakeRequest;
import com.example.shafi.ikathisawari.models.RideHistory;
import com.example.shafi.ikathisawari.models.RidersRequestsListInDriver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class RiderRequestHistory extends Fragment {

    String currentRider;
    ArrayList<RideHistory> rideHistoryArrayList;
    RideHistory rideHistory;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RiderHistoryAdapter riderHistoryAdapter;
    View view;



    public RiderRequestHistory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_rider_request_history, container, false);

        currentRider = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("requestsRiders").child("history_rider").child(currentRider).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    rideHistoryArrayList =new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        rideHistory = snapshot.getValue(RideHistory.class);
                        rideHistoryArrayList.add(rideHistory);


                    }
                    recyclerView = view.findViewById(R.id.riderRequestHistory);
                    layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);
                    riderHistoryAdapter = new RiderHistoryAdapter(getActivity(),getActivity().getSupportFragmentManager(), rideHistoryArrayList);


                    recyclerView.setAdapter(riderHistoryAdapter);

                    riderHistoryAdapter.notifyDataSetChanged();





                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

}
