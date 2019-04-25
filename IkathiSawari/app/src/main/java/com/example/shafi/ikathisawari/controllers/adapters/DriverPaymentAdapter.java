package com.example.shafi.ikathisawari.controllers.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.controllers.fragments.driver.RequestRiderProfile;
import com.example.shafi.ikathisawari.models.RidersRequestsListInDriver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DriverPaymentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<RidersRequestsListInDriver> ridersRequestsListInDriver;
    ArrayList<RidersRequestsListInDriver> ridersRequestsListInDriverFull;
    FragmentManager supportFragmentManager;

    public DriverPaymentAdapter(Context context, FragmentManager supportFragmentManager, ArrayList<RidersRequestsListInDriver> ridersRequestsListInDriver) {
        this.context=context;
        this.ridersRequestsListInDriver= ridersRequestsListInDriver;
        ridersRequestsListInDriverFull = new ArrayList<>(ridersRequestsListInDriver);
        this.supportFragmentManager = supportFragmentManager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Log.d("Time_Datess","inflate" );
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.driver_ride_end_card, viewGroup, false);

        LayoutInflater inflater1 = LayoutInflater.from(viewGroup.getContext());
        View view2 = inflater1.inflate(R.layout.driver_ride_end_card2, viewGroup, false);

        switch (i) {
            case 0: return new ViewHolder0(view);
//            case 1: return new ViewHolder2(view);
            case 2: return new ViewHolder2(view2);
            default:return new ViewHolder0(view);

        }

    }

    @Override
    public int getItemViewType(int position) {
        switch (ridersRequestsListInDriver.get(position).getMakeRequest().getStatus()) {
            case "pending":
                return 0;
            case "accepted":
                return 2;
            case "rejected":
                return 2;
            default:
                return 0;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final int position = i;
        switch (viewHolder.getItemViewType()) {
            case 0:
                ViewHolder0 viewHolder0 = (ViewHolder0) viewHolder;
                viewHolder0.ridername.setText(ridersRequestsListInDriver.get(i).getDateAndTime()+ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getRiderInfo().getName());
                viewHolder0.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                viewHolder0.ride_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "okkk", Toast.LENGTH_SHORT).show();
//                        final String currentDriver = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                        String currentRequest= ridersRequestsListInDriver.get(position).getDateAndTime();
//                        FirebaseDatabase.getInstance().getReference().child("requests").child("seen").child(currentDriver).child(currentRequest).child("status").setValue("accepted");
                    }
                });
                viewHolder0.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        Toast.makeText(context, "rating", Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case 2:
                final ViewHolder2 viewHolder2 = (ViewHolder2)viewHolder;
                viewHolder2.ridername.setText(ridersRequestsListInDriver.get(i).getDateAndTime()+ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getRiderInfo().getName());
                viewHolder2.distance.setText(""+ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getTraveledDistanceRider());
                viewHolder2.charges.setText(""+ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getRideCharges());

                viewHolder2.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                viewHolder2.ride_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "okkk"+viewHolder2.ratingBar.getRating(), Toast.LENGTH_SHORT).show();
                        final String currentRider = ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getCurrent_Rider();
                        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Rating");
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.hasChild(currentRider)) {

                                    float ratingg = Float.valueOf( dataSnapshot.child(currentRider).getValue().toString());
                                    float newRating = viewHolder2.ratingBar.getRating();
                                    float latestRating = (ratingg + newRating)/2;
                                    Toast.makeText(context, "aaaa "+ ratingg+" "+newRating+"="+latestRating, Toast.LENGTH_SHORT).show();
                                    databaseReference.child(currentRider).setValue(latestRating);


                                    Log.d("rrrr",ratingg+" "+newRating+"="+latestRating);
                                    Toast.makeText(context, "ratinggg "+dataSnapshot.child(currentRider).getValue(), Toast.LENGTH_SHORT).show();
                                }else{
                                    databaseReference.child(currentRider).setValue(viewHolder2.ratingBar.getRating());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                });

                viewHolder2.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        Toast.makeText(context, "rating "+rating, Toast.LENGTH_SHORT).show();
                    }
                });
                break;

        }

    }

    @Override
    public int getItemCount() {
        return ridersRequestsListInDriver.size();
    }

    class ViewHolder0 extends RecyclerView.ViewHolder {
        ImageView riderIcon;
        TextView ridername,distance,charges;
        RatingBar ratingBar;
        Button ride_ok;

        public ViewHolder0(@NonNull View itemView) {
            super(itemView);
            riderIcon = itemView.findViewById(R.id.driverRide_rider_photo);
            ridername = itemView.findViewById(R.id.driverRide_rider_name);
            distance = itemView.findViewById(R.id.driverRide_rider_travelledDistance);
            charges = itemView.findViewById(R.id.driverRide_rider_charges);
            ratingBar = itemView.findViewById(R.id.driverRide_rider_ratingBar);
            ride_ok = itemView.findViewById(R.id.driverRide_rider_ok);

        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder {
        ImageView riderIcon;
        TextView ridername,distance,charges;
        RatingBar ratingBar;
        Button ride_ok;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            riderIcon = itemView.findViewById(R.id.driverRide_rider_photo2);
            ridername = itemView.findViewById(R.id.driverRide_rider_name2);
            distance = itemView.findViewById(R.id.driverRide_rider_travelledDistance2);
            charges = itemView.findViewById(R.id.driverRide_rider_charges2);
            ratingBar = itemView.findViewById(R.id.driverRide_rider_ratingBar2);
            ride_ok = itemView.findViewById(R.id.driverRide_rider_ok2);

        }




    }
}
