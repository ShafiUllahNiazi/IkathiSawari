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
import android.widget.TextView;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.controllers.fragments.driver.RequestRiderProfile;
import com.example.shafi.ikathisawari.models.RiderInfo;
import com.example.shafi.ikathisawari.models.RidersRequestsListInDriver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class DriverRequestsAdapter extends  RecyclerView.Adapter<DriverRequestsAdapter.ViewHolder>{
    Context context;
    ArrayList<RidersRequestsListInDriver> ridersRequestsListInDriver;
    FragmentManager supportFragmentManager;

    public DriverRequestsAdapter(Context context, FragmentManager supportFragmentManager, ArrayList<RidersRequestsListInDriver> ridersRequestsListInDriver) {
        this.context=context;
        this.ridersRequestsListInDriver= ridersRequestsListInDriver;
        this.supportFragmentManager = supportFragmentManager;
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
        viewHolder.driversKey.setText(ridersRequestsListInDriver.get(i).getDateAndTime()+ridersRequestsListInDriver.get(i).getRiderInfo().getName());
        final int position = i;
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestRiderProfile requestRiderProfile = new RequestRiderProfile();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("ridersRequestsListInDriver",ridersRequestsListInDriver);
                bundle.putInt("position",position);
                requestRiderProfile.setArguments(bundle);
//                FragmentManager fragmentManager = context.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.driver_container, requestRiderProfile);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });



    }

    @Override
    public int getItemCount() {
        return ridersRequestsListInDriver.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView driversKey;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            driversKey = itemView.findViewById(R.id.driverKey);

        }



    }
}
