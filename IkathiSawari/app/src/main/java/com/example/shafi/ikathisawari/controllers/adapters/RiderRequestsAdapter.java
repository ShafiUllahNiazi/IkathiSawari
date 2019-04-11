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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.controllers.fragments.rider.CancelRequestDriverProfile;
import com.example.shafi.ikathisawari.models.MakeRequest;
import com.example.shafi.ikathisawari.models.RidersRequestsListInDriver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RiderRequestsAdapter extends RecyclerView.Adapter<RiderRequestsAdapter.ViewHolder>{
    Context context;
    ArrayList<RidersRequestsListInDriver> ridersRequestsListInDriver;
    FragmentManager supportFragmentManager;
    private String currentRider;

    public RiderRequestsAdapter(Context context, FragmentManager supportFragmentManager, ArrayList<RidersRequestsListInDriver> ridersRequestsListInDriver) {
        this.context=context;
        this.ridersRequestsListInDriver= ridersRequestsListInDriver;
        this.supportFragmentManager = supportFragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.rider_requests_card, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final int position = i;
        viewHolder.content_rider_request.setText("Driver");
//        viewHolder.content_rider_request.setText("Driver"+ ridersRequestsListInDriver.get(i).getMakeRequest().getDriverInfo().getName());
        viewHolder.rider_request_status.setText(ridersRequestsListInDriver.get(i).getMakeRequest().getStatus());
        viewHolder.cancelRiderRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "canclll "+viewHolder.cancelRiderRequest.getText(), Toast.LENGTH_SHORT).show();


                currentRider = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseDatabase.getInstance().getReference().child("requestsRiders").child("seen").child(currentRider).child(ridersRequestsListInDriver.get(position).getDateAndTime()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            MakeRequest makeRequest = dataSnapshot.getValue(MakeRequest.class);
                            FirebaseDatabase.getInstance().getReference().child("requestsRiders").child("history_rider").child(currentRider).child(ridersRequestsListInDriver.get(position).getDateAndTime()).setValue(makeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    FirebaseDatabase.getInstance().getReference().child("requestsRiders").child("seen").child(currentRider).child(ridersRequestsListInDriver.get(position).getDateAndTime()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    });
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                
                
                
                
            }
        });



        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                viewHolder.cancelRiderRequest.setText("changed");
                CancelRequestDriverProfile cancelRequestDriverProfile = new CancelRequestDriverProfile();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("ridersRequestsListInDriver",ridersRequestsListInDriver);
                bundle.putInt("position",position);
                cancelRequestDriverProfile.setArguments(bundle);
                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.driver_container, cancelRequestDriverProfile);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });


    }

    @Override
    public int getItemCount() {
        return ridersRequestsListInDriver.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView content_rider_request;
        TextView rider_request_status;
        Button cancelRiderRequest;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            content_rider_request = itemView.findViewById(R.id.content_rider_request);
            rider_request_status = itemView.findViewById(R.id.rider_request_status);
            cancelRiderRequest = itemView.findViewById(R.id.cancel_rider_request);

        }
    }
}
