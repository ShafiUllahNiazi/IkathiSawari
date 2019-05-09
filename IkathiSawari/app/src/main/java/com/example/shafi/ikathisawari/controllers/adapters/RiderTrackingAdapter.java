package com.example.shafi.ikathisawari.controllers.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.controllers.fragments.rider.RiderRide;
import com.example.shafi.ikathisawari.models.RideHistory;
import com.example.shafi.ikathisawari.models.RidersRequestsListInDriver;

import java.util.ArrayList;

public class RiderTrackingAdapter extends RecyclerView.Adapter<RiderTrackingAdapter.ViewHolder> {

    ArrayList<RidersRequestsListInDriver> ridersRequestsListInDriver;
    Context context;
    FragmentManager supportFragmentManager;

    public RiderTrackingAdapter(ArrayList<RidersRequestsListInDriver> ridersRequestsListInDriver, Context context, FragmentManager supportFragmentManager) {
        this.ridersRequestsListInDriver = ridersRequestsListInDriver;
        this.context = context;
        this.supportFragmentManager = supportFragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.rider_ride_tracking_card, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final int position = i;
        viewHolder.name.setText(ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getDriverInfo().getName());
        viewHolder.contact.setText(ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getDriverInfo().getMobile());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RiderRide riderRide = new RiderRide();
                Bundle b = new Bundle();
                b.putParcelableArrayList("ridersRequestsListInDriver", ridersRequestsListInDriver);

                b.putInt("position",position);
                riderRide.setArguments(b);
                FragmentManager fragmentManager = supportFragmentManager;
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.rider_container, riderRide);
//        fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


    }

    @Override
    public int getItemCount() {
        return ridersRequestsListInDriver.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, contact;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameww);
            contact = itemView.findViewById(R.id.contactww);

        }
    }
}
