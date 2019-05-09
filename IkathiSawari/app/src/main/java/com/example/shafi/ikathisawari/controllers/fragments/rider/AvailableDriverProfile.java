package com.example.shafi.ikathisawari.controllers.fragments.rider;


import android.content.Intent;
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
import com.example.shafi.ikathisawari.controllers.activities.Rider_Screen;
import com.example.shafi.ikathisawari.models.AvailableDriverInfo;
import com.example.shafi.ikathisawari.models.MakeRequest;
import com.example.shafi.ikathisawari.models.RiderInfo;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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


    Button sendRequest;
    ArrayList<AvailableDriverInfo> availableDriversList;

    TextView originName, destinationName,name, ageGender,vehicleType,contactNo,cnic,
            travellDate,offerSeats,availableSeats, riderSeats, riderRidePrice, driverMessage,riderMessage;


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
//        final LatLng fromPosition = getArguments().getParcelable("from_position");
//        final LatLng toPosition = getArguments().getParcelable("to_position");
//        TextView originName, destinationName,name, ageGender,vehicleType,contactNo,cnic,
//                travellDate,offerSeats,availableSeats, riderSeats, riderRidePrice, driverMessage,riderMessage;
        originName = view.findViewById(R.id.availableDriverProfileOriginName);
        destinationName = view.findViewById(R.id.availableDriverProfileDestinationName);
        name = view.findViewById(R.id.availableDriverProfilename);
        ageGender = view.findViewById(R.id.availableDriverProfileAgeGender);
        vehicleType = view.findViewById(R.id.availableDriverProfileVehicleModel);
        contactNo = view.findViewById(R.id.availableDriverProfilecontactNo);
        cnic = view.findViewById(R.id.availableDriverProfilecnic);
        travellDate = view.findViewById(R.id.availableDriverProfiledateTime);
        offerSeats = view.findViewById(R.id.availableDriverProfileOfferedSeats);
        availableSeats = view.findViewById(R.id.availableDriverProfileAvailableSeats);
        riderSeats = view.findViewById(R.id.availableDriverProfileReserverdSeats);
        riderRidePrice = view.findViewById(R.id.availableDriverProfileRidePrice);
        driverMessage = view.findViewById(R.id.availableDriverProfileMessageDriver);
        riderMessage = view.findViewById(R.id.availableDriverProfileMessageRider);



        originName.setText(availableDriversList.get(position).getDriver_origin_name());
        destinationName.setText(availableDriversList.get(position).getDriver_destination_name());

        name.setText(availableDriversList.get(position).getDriverInfo().getName() + "");
        ageGender.setText(availableDriversList.get(position).getDriverInfo().getGender() + "");
        vehicleType.setText("Vehicle Model: "+ availableDriversList.get(position).getVehicle_Model1() + "");
        contactNo.setText(availableDriversList.get(position).getDriverInfo().getMobile() + "");
        cnic.setText(availableDriversList.get(position).getDriverInfo().getCnic() + "");
        travellDate.setText("Date: "+ availableDriversList.get(position).getDate() + "");
        offerSeats.setText("Offered seats: "+ availableDriversList.get(position).getSeats() + "");
        availableSeats.setText("Available seats: "+ availableDriversList.get(position).getNo_of_available_seats() + "");
        riderSeats.setText("Your seats: "+ availableDriversList.get(position).getSeatsRider() + "");
        riderRidePrice.setText("Charges: "+ availableDriversList.get(position).getRideCharges() + "");
        driverMessage.setText( availableDriversList.get(position).getMessagedriver()+"");
        sendRequest = view.findViewById(R.id.availableDriverProfileSendRequest);


        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(riderMessage.getText().toString().equals(""))){
                    availableDriversList.get(position).setMessageRider(riderMessage.getText().toString());
                }

                DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                dateFormatter.setLenient(false);
                Date today = new Date();
                final String timeSting = dateFormatter.format(today);

                final String current_Rider = FirebaseAuth.getInstance().getCurrentUser().getUid();

                String driver = availableDriversList.get(position).getDriverKey();
//                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("Driver").child(availableDriversList.get(position).getDriverKey()).child("request").child(timeSting);
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("requests").child("unseen").child(driver).child(timeSting);

                final MakeRequest makeRequest1 = new MakeRequest("pending",false,availableDriversList.get(position));

                databaseReference.setValue(makeRequest1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseDatabase.getInstance().getReference().child("requestsRiders").child("unseen").child(current_Rider).child(timeSting).setValue(makeRequest1);


                        Intent intent = new Intent(getActivity(),Rider_Screen.class);
                        startActivity(intent);
                        getActivity().finish();


                        RiderHome1 riderHome1 = new RiderHome1();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.rider_container, riderHome1);
//                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });



            }
        });

        return view;
    }

}
