package com.example.shafi.ikathisawari.controllers.fragments.rider;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.models.MakeRequest;
import com.example.shafi.ikathisawari.models.RideHistory;
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


    private Button cancelRequest;
    ArrayList<RidersRequestsListInDriver> ridersRequestsListInDriver;
    int position;
    private String currentRider;

    TextView originName, destinationName,name, ageGender,vehicleType,contactNo,cnic,
            travellDate,offerSeats,availableSeats, riderSeats, riderRidePrice, driverMessage,riderMessage;


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

        originName = view.findViewById(R.id.requestedDriverProfileOriginName);
        destinationName = view.findViewById(R.id.requestedDriverProfileDestinationName);
        name = view.findViewById(R.id.requestedDriverProfilename);
        ageGender = view.findViewById(R.id.requestedDriverProfileAgeGender);
        vehicleType = view.findViewById(R.id.requestedDriverProfileVehicleModel);
        contactNo = view.findViewById(R.id.requestedDriverProfilecontactNo);
        cnic = view.findViewById(R.id.requestedDriverProfilecnic);
        travellDate = view.findViewById(R.id.requestedDriverProfiledateTime);
        offerSeats = view.findViewById(R.id.requestedDriverProfileOfferedSeats);
        availableSeats = view.findViewById(R.id.requestedDriverProfileAvailableSeats);
        riderSeats = view.findViewById(R.id.requestedDriverProfileReserverdSeats);
        riderRidePrice = view.findViewById(R.id.requestedDriverProfileRidePrice);
        driverMessage = view.findViewById(R.id.requestedDriverProfileMessageDriver);
        riderMessage = view.findViewById(R.id.requestedDriverProfileMessageRider);
        cancelRequest = view.findViewById(R.id.requestedDriverProfileCancelRequest);

        originName.setText(ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getDriver_origin_name());
        destinationName.setText(ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getDriver_destination_name());

        name.setText(ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getDriverInfo().getName() + "");
        ageGender.setText(ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getDriverInfo().getGender() + "");
        vehicleType.setText("Vehicle Model: "+ ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getVehicle_Model1() + "");
        contactNo.setText(ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getDriverInfo().getMobile() + "");
        cnic.setText(ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getDriverInfo().getCnic() + "");
        travellDate.setText("Date: "+ ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getDate() + "");
        offerSeats.setText("Offered seats: "+ ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getSeats() + "");
        availableSeats.setText("Available seats: "+ ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getNo_of_available_seats() + "");
        riderSeats.setText("Your seats: "+ ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getSeatsRider() + "");
        riderRidePrice.setText("Charges: "+ ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getRideCharges() + "");
        String driverMsg = ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getMessagedriver()+"";
        String riderMsg = ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getMessageRider()+"";
        if(!(driverMsg.equals(""))){
            driverMessage.setText("Driver message: "+ driverMsg);
        }
        if(!(riderMsg.equals(""))){
            riderMessage.setText("Your Message: "+ riderMsg);
        }



        cancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String driver = ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getDriverKey();
                currentRider = FirebaseAuth.getInstance().getCurrentUser().getUid();
                MakeRequest makeRequest = ridersRequestsListInDriver.get(position).getMakeRequest();



                final RideHistory rideHistory = new RideHistory(makeRequest,"rejected","rider");

                FirebaseDatabase.getInstance().getReference().child("requestsRiders").child("history_rider").child(currentRider).child(ridersRequestsListInDriver.get(position).getDateAndTime()).setValue(rideHistory).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseDatabase.getInstance().getReference().child("requestsRiders").child("seen").child(currentRider).child(ridersRequestsListInDriver.get(position).getDateAndTime()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                FirebaseDatabase.getInstance().getReference().child("requests").child("history_driver").child(driver).child(ridersRequestsListInDriver.get(position).getDateAndTime()).setValue(rideHistory).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        FirebaseDatabase.getInstance().getReference().child("requests").child("seen").child(driver).child(ridersRequestsListInDriver.get(position).getDateAndTime()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getActivity(), "Successfully cancel Request", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                        FirebaseDatabase.getInstance().getReference().child("requests").child("unseen").child(driver).child(ridersRequestsListInDriver.get(position).getDateAndTime()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getActivity(), "Successfully cancel Request", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });







//        driverName = view.findViewById(R.id.canceDriverName);
//        cancelRequest = view.findViewById(R.id.cancel_request_review);
//        driverName.setText(ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getDriverInfo().getName());
//        cancelRequest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                currentRider = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                FirebaseDatabase.getInstance().getReference().child("requestsRiders").child("seen").child(currentRider).child(ridersRequestsListInDriver.get(position).getDateAndTime()).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if(dataSnapshot.exists()){
//                            MakeRequest makeRequest = dataSnapshot.getValue(MakeRequest.class);
//                            FirebaseDatabase.getInstance().getReference().child("requestsRiders").child("history_rider").child(currentRider).child(ridersRequestsListInDriver.get(position).getDateAndTime()).setValue(makeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    FirebaseDatabase.getInstance().getReference().child("requestsRiders").child("seen").child(currentRider).child(ridersRequestsListInDriver.get(position).getDateAndTime()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//
//                                        }
//                                    });
//                                }
//                            });
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//            }
//        });
        return view;
    }

}
