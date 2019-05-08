package com.example.shafi.ikathisawari.controllers.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.controllers.fragments.rider.AvailableDriverProfile;
import com.example.shafi.ikathisawari.controllers.fragments.rider.RiderHome1;
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

public class AvailableDriversAdapter extends RecyclerView.Adapter<AvailableDriversAdapter.ViewHolder> {

    Context context;
    FragmentManager supportFragmentManager;
    ArrayList<AvailableDriverInfo> availableDriversList;
    LatLng fromPosition;
    LatLng toPosition;

    public AvailableDriversAdapter(Context context, FragmentManager supportFragmentManager, ArrayList<AvailableDriverInfo> availableDriversList) {
        this.context = context;
        this.supportFragmentManager = supportFragmentManager;
        this.availableDriversList = availableDriversList;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.available_drivers_card1, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {


        viewHolder.available_driver_name.setText(availableDriversList.get(i).getDriverInfo().getName());
        viewHolder.available_driver_ride_date.setText("Date: "+availableDriversList.get(i).getDate()+" "+availableDriversList.get(i).getTime());
        viewHolder.available_driver_age_gender.setText(availableDriversList.get(i).getDriverInfo().getGender()+"");
        viewHolder.available_driver_contactNo.setText(availableDriversList.get(i).getDriverInfo().getMobile()+"");



        viewHolder.available_driver_vehicle_model.setText("Vehicle Model: "+availableDriversList.get(i).getVehicle_Model1()+"");
        viewHolder.available_driver_offered_seats.setText("Offered seats: "+availableDriversList.get(i).getSeats()+"");

        viewHolder.available_driver_available_seats.setText("Available seats: "+availableDriversList.get(i).getNo_of_available_seats()+"");
        viewHolder.available_driver_reserved_seats.setText("Your seats: "+availableDriversList.get(i).getSeatsRider()+"");
        viewHolder.available_driver_price_of_ride.setText("Charges: "+availableDriversList.get(i).getRideCharges()+"");



        final int position = i;

        viewHolder.availableDriverSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
                        RiderHome1 riderHome1 = new RiderHome1();

                        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.rider_container, riderHome1);
//                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });








            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AvailableDriverProfile availableDriverProfile = new AvailableDriverProfile();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("availableDriversList",availableDriversList);
                bundle.putInt("position",position);
//                bundle.putParcelable("from_position", fromPosition);
//                bundle.putParcelable("to_position", toPosition);
                availableDriverProfile.setArguments(bundle);
//                FragmentManager fragmentManager = context.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.rider_container, availableDriverProfile);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return availableDriversList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView available_driver_image;
        TextView  available_driver_name,available_driver_age_gender,
                available_driver_contactNo,available_driver_vehicle_model,available_driver_offered_seats,
                available_driver_ride_date,available_driver_available_seats,available_driver_reserved_seats,available_driver_price_of_ride;
        Button availableDriverSendRequest;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            available_driver_name = itemView.findViewById(R.id.availableDriver1name);
            available_driver_ride_date = itemView.findViewById(R.id.availableDriver1dateTime);
            available_driver_contactNo = itemView.findViewById(R.id.availableDriver1contactNo);
            available_driver_age_gender = itemView.findViewById(R.id.availableDriver1AgeGender);
            available_driver_vehicle_model = itemView.findViewById(R.id.availableDriver1VehicleModel);
            available_driver_offered_seats = itemView.findViewById(R.id.availableDriver1OfferedSeats);

            available_driver_available_seats = itemView.findViewById(R.id.availableDriver1AvailableSeats);
            available_driver_reserved_seats = itemView.findViewById(R.id.availableDriver1ReservedSeats);
            available_driver_price_of_ride = itemView.findViewById(R.id.availableDriver1RidePrice);
            availableDriverSendRequest = itemView.findViewById(R.id.availableDriver1SendRequest);


        }
    }
}
