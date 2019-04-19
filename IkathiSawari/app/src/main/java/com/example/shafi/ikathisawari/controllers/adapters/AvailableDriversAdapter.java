package com.example.shafi.ikathisawari.controllers.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.controllers.fragments.rider.AvailableDriverProfile;
import com.example.shafi.ikathisawari.models.AvailableDriverInfo;
import com.google.android.gms.maps.model.LatLng;

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
        View view = inflater.inflate(R.layout.available_drivers_card, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.available_driver_name.setText(availableDriversList.get(i).getDriverInfo().getName());
        int traveledDistanceRider = availableDriversList.get(i).getTraveledDistanceRider();
        int pricePerKm = Integer.valueOf(availableDriversList.get(i).getPrice());
        Toast.makeText(context, traveledDistanceRider+"ggggg"+pricePerKm , Toast.LENGTH_SHORT).show();

        int  charges = (pricePerKm*traveledDistanceRider)/1000;
        Toast.makeText(context, charges+" ddd"+traveledDistanceRider+"ggggg"+pricePerKm , Toast.LENGTH_SHORT).show();




        viewHolder.available_driver_price_of_ride.setText(charges+"");

        final int position = i;
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
        TextView available_driver_name,available_driver_age_gender,available_driver_vehicle_model,available_driver_offered_seats,
                available_driver_ride_date,available_driver_available_seats,available_driver_price_of_ride;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            available_driver_name = itemView.findViewById(R.id.available_driver_name);
            available_driver_age_gender = itemView.findViewById(R.id.available_driver_age_gender);
            available_driver_vehicle_model = itemView.findViewById(R.id.available_driver_vehicle_model);
            available_driver_offered_seats = itemView.findViewById(R.id.available_driver_offered_seats);
            available_driver_ride_date = itemView.findViewById(R.id.available_driver_ride_date);
            available_driver_available_seats = itemView.findViewById(R.id.available_driver_available_seats);
            available_driver_price_of_ride = itemView.findViewById(R.id.available_driver_price_of_ride);


        }
    }
}
