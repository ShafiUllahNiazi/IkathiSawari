package com.example.shafi.ikathisawari.controllers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.models.RideHistory;

import java.util.ArrayList;

public class DriverHistoryAdapter extends RecyclerView.Adapter<DriverHistoryAdapter.ViewHolder> {
    ArrayList<RideHistory> rideHistoryArrayList;
    Context context;
    FragmentManager supportFragmentManager;

    public DriverHistoryAdapter(Context context, FragmentManager supportFragmentManager, ArrayList<RideHistory> rideHistoryArrayList) {
        this.rideHistoryArrayList = rideHistoryArrayList;
        this.context = context;
        this.supportFragmentManager = supportFragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.driver_history_card, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.driverName.setText(rideHistoryArrayList.get(i).getMakeRequest().getAvailableDriverInfo().getRiderInfo().getName());
        viewHolder.gender.setText(rideHistoryArrayList.get(i).getMakeRequest().getAvailableDriverInfo().getRiderInfo().getGender());
        viewHolder.driverMobileNo.setText(rideHistoryArrayList.get(i).getMakeRequest().getAvailableDriverInfo().getRiderInfo().getMobile());
        viewHolder.ride_date.setText(rideHistoryArrayList.get(i).getMakeRequest().getAvailableDriverInfo().getDate());
        viewHolder.vehicle.setText(rideHistoryArrayList.get(i).getMakeRequest().getAvailableDriverInfo().getVehicle_Model1());

        if(rideHistoryArrayList.get(i).getStatus().equals("rejected")){
            if(rideHistoryArrayList.get(i).getRejected_by().equals("driver")){
                viewHolder.status.setText("rejected by Driver");
            }else {
                viewHolder.status.setText("rejected by rider");
            }

        }else {
            viewHolder.status.setText("Completed");
        }


    }

    @Override
    public int getItemCount() {
        return rideHistoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView driverName,gender, driverMobileNo, charges,riderSeats,vehicle,status,ride_date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            driverName = itemView.findViewById(R.id.driver_name_driver_history);
            gender = itemView.findViewById(R.id.age_gender_driver_history);
            driverMobileNo = itemView.findViewById(R.id.mobileNo_driver_history);
            ride_date = itemView.findViewById(R.id.ride_date_driver_history);
            riderSeats = itemView.findViewById(R.id.rider_seats_driver_history);
            charges = itemView.findViewById(R.id.rider_charges_driver_history);
            status = itemView.findViewById(R.id.ride_status_driver_history);
            vehicle = itemView.findViewById(R.id.vehicle_model_driver_history);

        }
    }
}
