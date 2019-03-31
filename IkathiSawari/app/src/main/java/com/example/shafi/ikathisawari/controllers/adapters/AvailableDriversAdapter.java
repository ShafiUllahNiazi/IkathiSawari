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

    public AvailableDriversAdapter(Context context, FragmentManager supportFragmentManager, ArrayList<AvailableDriverInfo> availableDriversList, LatLng fromPosition, LatLng toPosition) {
        this.context = context;
        this.supportFragmentManager = supportFragmentManager;
        this.availableDriversList = availableDriversList;
        this.fromPosition = fromPosition;
        this.toPosition = toPosition;
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
        viewHolder.driversKey.setText(availableDriversList.get(i).getDriverInfo().getName());
        final int position = i;
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AvailableDriverProfile availableDriverProfile = new AvailableDriverProfile();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("availableDriversList",availableDriversList);
                bundle.putInt("position",position);
                bundle.putParcelable("from_position", fromPosition);
                bundle.putParcelable("to_position", toPosition);
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

        TextView driversKey;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            driversKey = itemView.findViewById(R.id.driverKey);

        }
    }
}
