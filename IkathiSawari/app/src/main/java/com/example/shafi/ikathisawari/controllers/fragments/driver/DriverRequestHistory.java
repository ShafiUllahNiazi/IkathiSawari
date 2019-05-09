package com.example.shafi.ikathisawari.controllers.fragments.driver;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.controllers.adapters.DriverHistoryAdapter;
import com.example.shafi.ikathisawari.models.RideHistory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DriverRequestHistory extends Fragment {
    String currentDriver;
    ArrayList<RideHistory> rideHistoryArrayList;
    RideHistory rideHistory;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    DriverHistoryAdapter driverHistoryAdapter;
    View view;
    TextView emptyView;


    public DriverRequestHistory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_driver_request_history, container, false);
        emptyView = view.findViewById(R.id.empty_view_driver_request_history);
        recyclerView = view.findViewById(R.id.driverRequestHistory);

        currentDriver = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("requests").child("history_driver").child(currentDriver).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    rideHistoryArrayList =new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        rideHistory = snapshot.getValue(RideHistory.class);
                        rideHistoryArrayList.add(rideHistory);


                    }

                    layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);
                    driverHistoryAdapter = new DriverHistoryAdapter(getActivity(),getChildFragmentManager(), rideHistoryArrayList);


                    recyclerView.setAdapter(driverHistoryAdapter);
                    driverHistoryAdapter.notifyDataSetChanged();

                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);






                }else {

                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);

//                    Toast.makeText(getActivity(), "ssss"+rideHistoryArrayList.size(), Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

}
