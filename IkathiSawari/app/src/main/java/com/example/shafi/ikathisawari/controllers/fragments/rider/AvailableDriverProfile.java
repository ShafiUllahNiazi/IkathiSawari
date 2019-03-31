package com.example.shafi.ikathisawari.controllers.fragments.rider;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.models.AvailableDriverInfo;
import com.example.shafi.ikathisawari.models.MakeRequest;
import com.example.shafi.ikathisawari.models.RiderInfo;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AvailableDriverProfile extends Fragment {

    private static final String TAG = "AvailableDriverProfile";

    TextView name;
    Button sendRequest;
    ArrayList<AvailableDriverInfo> availableDriversList;


    public AvailableDriverProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_available_driver_profile, container, false);
        availableDriversList=getArguments().getParcelableArrayList("availableDriversList");
        final int position = getArguments().getInt("position");
        final LatLng fromPosition = getArguments().getParcelable("from_position");
        final LatLng toPosition = getArguments().getParcelable("to_position");
        name = view.findViewById(R.id.availableDriverName);
        name.setText(availableDriversList.get(position).getDriverInfo().getName());
        sendRequest = view.findViewById(R.id.sendRequest);

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "hhhhh", Toast.LENGTH_SHORT).show();
                DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
                dateFormatter.setLenient(false);
                Date today = new Date();
                final String timeSting = dateFormatter.format(today);
                Toast.makeText(getActivity(), "ss "+ timeSting, Toast.LENGTH_SHORT).show();
                final String current_Rider = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("Driver").child(availableDriversList.get(position).getDriverKey()).child("request").child(timeSting);
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("requests").child("pending").child(availableDriversList.get(position).getDriverKey()).child(timeSting);

//                final RiderInfo[] riderInfo = new RiderInfo[1];
                DatabaseReference databaseReferenceRider = FirebaseDatabase.getInstance().getReference().child("users").child("Rider").child(current_Rider);

                databaseReferenceRider.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        RiderInfo riderInfo = new RiderInfo();
                        riderInfo = dataSnapshot.getValue(RiderInfo.class);
                        Log.d(TAG,"innnnnn");
                        MakeRequest makeRequest = new MakeRequest(current_Rider,riderInfo,fromPosition.latitude,fromPosition.longitude,toPosition.latitude,toPosition.longitude);
                        databaseReference.setValue(makeRequest);
//                        databaseReference.setValue(availableDriversList.get(position));


                        RiderHome1 riderHome1 = new RiderHome1();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.rider_container, riderHome1);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                Log.d(TAG,"ouuuttt");


            }
        });

        return view;
    }

}
