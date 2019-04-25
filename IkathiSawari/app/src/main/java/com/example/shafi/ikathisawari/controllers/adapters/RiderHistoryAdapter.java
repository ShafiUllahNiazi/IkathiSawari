package com.example.shafi.ikathisawari.controllers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.models.MakeRequest;
import com.example.shafi.ikathisawari.models.RideHistory;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RiderHistoryAdapter extends RecyclerView.Adapter<RiderHistoryAdapter.ViewHolder> {

    ArrayList<RideHistory> rideHistoryArrayList;
    Context context;
    FragmentManager supportFragmentManager;


    public RiderHistoryAdapter(Context context, FragmentManager supportFragmentManager, ArrayList<RideHistory> rideHistoryArrayList) {
        this.context = context;
        this.supportFragmentManager = supportFragmentManager;
        this.rideHistoryArrayList =rideHistoryArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.rider_history_card, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.driverName.setText(rideHistoryArrayList.get(i).getMakeRequest().getAvailableDriverInfo().getDriverInfo().getName());
        viewHolder.driverMobileNo.setText(rideHistoryArrayList.get(i).getMakeRequest().getAvailableDriverInfo().getDriverInfo().getMobile());
        if(rideHistoryArrayList.get(i).getStatus().equals("rejected")){
            if(rideHistoryArrayList.get(i).getRejected_by().equals("driver")){
                viewHolder.rideStatus.setText("rejected by Driver");
            }else {
                viewHolder.rideStatus.setText("rejected by rider");
            }

        }else {
            viewHolder.rideStatus.setText("Completed");
        }


    }

    @Override
    public int getItemCount() {
        return rideHistoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView driverName,driverMobileNo, riderSeats, riderCharges,rideStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            driverName = itemView.findViewById(R.id.driver_name_rider_history);
            driverMobileNo = itemView.findViewById(R.id.mobileNo_rider_history);
            riderSeats = itemView.findViewById(R.id.rider_seats_rider_history);
            riderCharges = itemView.findViewById(R.id.rider_charges_rider_history);
            rideStatus = itemView.findViewById(R.id.ride_status_rider_history);

        }
    }
}
