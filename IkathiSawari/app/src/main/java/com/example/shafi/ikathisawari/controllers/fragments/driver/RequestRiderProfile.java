package com.example.shafi.ikathisawari.controllers.fragments.driver;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.models.DriverInfo;
import com.example.shafi.ikathisawari.models.DriverRoutInfo;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestRiderProfile extends Fragment {


    Button acceptCancelRequest, rejectRequest;
    ArrayList<RidersRequestsListInDriver> ridersRequestsListInDrivers;
    DriverInfo driverInfo;
    DriverRoutInfo driverRoutInfo;

    TextView originName, destinationName,name, ageGender,contactNo,cnic,
            travellDate,offerSeats,availableSeats, status, riderSeats, riderRidePrice, driverMessage,riderMessage;

    public RequestRiderProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_rider_profile, container, false);


//        String currentDriver = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("Driver").child(currentDriver);
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()){
//                    driverInfo = dataSnapshot.getValue(DriverInfo.class);
//
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
//        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Available Routs").child(currentDriver);
//        databaseReference1.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                driverRoutInfo = dataSnapshot.getValue(DriverRoutInfo.class);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


        ridersRequestsListInDrivers=getArguments().getParcelableArrayList("ridersRequestsListInDriver");
        final int position = getArguments().getInt("position");

        originName = view.findViewById(R.id.requestedRiderProfileOriginName);
        destinationName = view.findViewById(R.id.requestedRiderProfileDestinationName);
        name = view.findViewById(R.id.requestedRiderProfilename);
        ageGender = view.findViewById(R.id.requestedRiderProfileAgeGender);

        contactNo = view.findViewById(R.id.requestedRiderProfilecontactNo);
        cnic = view.findViewById(R.id.requestedRiderProfilecnic);
        travellDate = view.findViewById(R.id.requestedRiderProfiledateTime);
        offerSeats = view.findViewById(R.id.requestedRiderProfileOfferedSeats);
        availableSeats = view.findViewById(R.id.requestedRiderProfileAvailableSeats);
        status = view.findViewById(R.id.requestedRiderProfileStatus);
        riderSeats = view.findViewById(R.id.requestedRiderProfileReserverdSeats);
        riderRidePrice = view.findViewById(R.id.requestedRiderProfileRidePrice);
        driverMessage = view.findViewById(R.id.requestedRiderProfileMessageDriver);
        riderMessage = view.findViewById(R.id.requestedRiderProfileMessageRider);




        originName.setText(ridersRequestsListInDrivers.get(position).getMakeRequest().getAvailableDriverInfo().getRider_origin_name());
        destinationName.setText(ridersRequestsListInDrivers.get(position).getMakeRequest().getAvailableDriverInfo().getRider_destination_name());

        name.setText(ridersRequestsListInDrivers.get(position).getMakeRequest().getAvailableDriverInfo().getRiderInfo().getName() + "");
        ageGender.setText(ridersRequestsListInDrivers.get(position).getMakeRequest().getAvailableDriverInfo().getRiderInfo().getGender() + "");
        contactNo.setText(ridersRequestsListInDrivers.get(position).getMakeRequest().getAvailableDriverInfo().getRiderInfo().getMobile() + "");
        cnic.setText(ridersRequestsListInDrivers.get(position).getMakeRequest().getAvailableDriverInfo().getRiderInfo().getCnic() + "");
        travellDate.setText("Date: "+ ridersRequestsListInDrivers.get(position).getMakeRequest().getAvailableDriverInfo().getTimeAndDateRider() + "");
        offerSeats.setText("Offered seats: "+ ridersRequestsListInDrivers.get(position).getMakeRequest().getAvailableDriverInfo().getSeats() + "");
        availableSeats.setText("Available seats: "+ ridersRequestsListInDrivers.get(position).getMakeRequest().getAvailableDriverInfo().getNo_of_available_seats() + "");
        status.setText("Status: "+ ridersRequestsListInDrivers.get(position).getMakeRequest().getStatus() + "");
        riderSeats.setText("Requested seats: "+ ridersRequestsListInDrivers.get(position).getMakeRequest().getAvailableDriverInfo().getSeatsRider() + "");
        riderRidePrice.setText("Charges: "+ ridersRequestsListInDrivers.get(position).getMakeRequest().getAvailableDriverInfo().getRideCharges() + "");
        String driverMsg = ridersRequestsListInDrivers.get(position).getMakeRequest().getAvailableDriverInfo().getMessagedriver()+"";
        String riderMsg = ridersRequestsListInDrivers.get(position).getMakeRequest().getAvailableDriverInfo().getMessageRider()+"";
        if(!(driverMsg.equals(""))){
            driverMessage.setText("Your message: "+ driverMsg);
        }
        if(!(riderMsg.equals(""))){
            riderMessage.setText("Rider Message: "+ riderMsg);
        }



        acceptCancelRequest = view.findViewById(R.id.requestedRiderProfileAcceptCancelBtn);
        rejectRequest = view.findViewById(R.id.requestedRiderProfileRejectBtn);
        acceptCancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





            }
        });

        rejectRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        return view;
    }

}
