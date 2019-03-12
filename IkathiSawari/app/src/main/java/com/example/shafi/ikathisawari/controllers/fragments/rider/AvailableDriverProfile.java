package com.example.shafi.ikathisawari.controllers.fragments.rider;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.models.AvailableDriverInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AvailableDriverProfile extends Fragment {

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
        name = view.findViewById(R.id.availableDriverName);
        name.setText(availableDriversList.get(position).getDriverInfo().getName());
        sendRequest = view.findViewById(R.id.sendRequest);
        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "hhhhh", Toast.LENGTH_SHORT).show();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("Driver").child(availableDriversList.get(position).getDriverKey()).child("request");

                databaseReference.setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

            }
        });

        return view;
    }

}
