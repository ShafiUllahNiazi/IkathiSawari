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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.controllers.fragments.rider.CancelRequestDriverProfile;
import com.example.shafi.ikathisawari.models.MakeRequest;
import com.example.shafi.ikathisawari.models.RideHistory;
import com.example.shafi.ikathisawari.models.RidersRequestsListInDriver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RiderRequestsAdapter extends RecyclerView.Adapter<RiderRequestsAdapter.ViewHolder> implements Filterable {
    Context context;

    ArrayList<RidersRequestsListInDriver> ridersRequestsListInDriver;
    ArrayList<RidersRequestsListInDriver> ridersRequestsListInDriverFull;
    FragmentManager supportFragmentManager;
    private String currentRider;

    public RiderRequestsAdapter(Context context, FragmentManager supportFragmentManager, ArrayList<RidersRequestsListInDriver> ridersRequestsListInDriver) {
        this.context=context;
        this.ridersRequestsListInDriver= ridersRequestsListInDriver;
        this.supportFragmentManager = supportFragmentManager;
        ridersRequestsListInDriverFull = new ArrayList<>(ridersRequestsListInDriver);
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
//        viewHolder.content_rider_request.setText("Driver");
        viewHolder.nameDriver.setText(ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getDriverInfo().getName());
        viewHolder.driver_age_gender.setText(ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getDriverInfo().getGender());
        viewHolder.driver_vehicle_model.setText("Vehicle Model: "+ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getVehicle_Model1());
        viewHolder.driver_mobile.setText(ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getDriverInfo().getMobile()+"");
        viewHolder.driver_ride_date.setText("Date: "+ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getDate() +" at"+ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getTime());
        viewHolder.driver_offered_seats.setText("Offered seats: "+ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getSeats());
        viewHolder.driver_available_seats.setText("Available seats: "+ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getNo_of_available_seats()+"");
        viewHolder.status.setText("Status: "+ridersRequestsListInDriver.get(i).getMakeRequest().getStatus()+"");
        viewHolder.reserved_seats.setText("Your seats: "+ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getSeatsRider());


        viewHolder.driver_price_of_ride.setText("Charges: "+ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getRideCharges()+"");
//        viewHolder.driver_age_gender.setText(ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getDriverInfo().getGender()+"");





        viewHolder.cancelRiderRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "canclll "+viewHolder.cancelRiderRequest.getText(), Toast.LENGTH_SHORT).show();


                final String driver = ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getDriverKey();
                currentRider = FirebaseAuth.getInstance().getCurrentUser().getUid();
                MakeRequest makeRequest = ridersRequestsListInDriver.get(position).getMakeRequest();



                final RideHistory rideHistory = new RideHistory(makeRequest,"rejected","rider");

                FirebaseDatabase.getInstance().getReference().child("requestsRiders").child("history_rider").child(currentRider).child(ridersRequestsListInDriver.get(position).getDateAndTime()).setValue(rideHistory).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseDatabase.getInstance().getReference().child("requestsRiders").child("seen").child(currentRider).child(ridersRequestsListInDriver.get(position).getDateAndTime()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                FirebaseDatabase.getInstance().getReference().child("requests").child("history_driver").child(driver).child(ridersRequestsListInDriver.get(position).getDateAndTime()).setValue(rideHistory).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        FirebaseDatabase.getInstance().getReference().child("requests").child("seen").child(driver).child(ridersRequestsListInDriver.get(position).getDateAndTime()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(context, "Successfully cancel Request", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                        FirebaseDatabase.getInstance().getReference().child("requests").child("unseen").child(driver).child(ridersRequestsListInDriver.get(position).getDateAndTime()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(context, "Successfully cancel Request", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }
                        });
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
                fragmentTransaction.replace(R.id.rider_container, cancelRequestDriverProfile);
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
        TextView nameDriver;
        TextView driver_age_gender,driver_vehicle_model,driver_offered_seats,
                driver_ride_date,driver_available_seats,driver_price_of_ride;
        TextView driver_mobile,status,reserved_seats;

        Button cancelRiderRequest;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameDriver = itemView.findViewById(R.id.requestedDrivername);
            driver_age_gender = itemView.findViewById(R.id.requestedDriverAgeGender);
            driver_vehicle_model = itemView.findViewById(R.id.requestedDriverVehicleModel);
            driver_mobile= itemView.findViewById(R.id.requestedDrivercontactNo);
            driver_ride_date = itemView.findViewById(R.id.requestedDriverdateTime);
            driver_offered_seats = itemView.findViewById(R.id.requestedDriverOfferedSeats);
            driver_available_seats = itemView.findViewById(R.id.requestedDriverAvailableSeats);
            status = itemView.findViewById(R.id.requestedDriverStatus);
            reserved_seats = itemView.findViewById(R.id.requestedDriverReseerrvedSeats);
            driver_price_of_ride = itemView.findViewById(R.id.requestedDriverRidePrice);

            cancelRiderRequest = itemView.findViewById(R.id.requestedDriverCancelRequest);

        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<RidersRequestsListInDriver> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() ==0){
                filteredList.addAll(ridersRequestsListInDriverFull);
            }else {
                String filteredPattern = constraint.toString().toLowerCase().trim();
                for (RidersRequestsListInDriver item:ridersRequestsListInDriverFull) {
                    if(item.getMakeRequest().getAvailableDriverInfo().getDriverInfo().getName().toLowerCase().contains(filteredPattern)){
                        filteredList.add(item);
                    }
                    if(item.getMakeRequest().getAvailableDriverInfo().getDriverInfo().getMobile().toLowerCase().contains(filteredPattern)){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;


        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            ridersRequestsListInDriver.clear();
            ridersRequestsListInDriver.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
