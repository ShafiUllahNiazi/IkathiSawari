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
import com.example.shafi.ikathisawari.controllers.fragments.driver.RequestRiderProfile;
import com.example.shafi.ikathisawari.models.MakeRequest;
import com.example.shafi.ikathisawari.models.RideHistory;
import com.example.shafi.ikathisawari.models.RidersRequestsListInDriver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverRequestsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    Context context;

    ArrayList<RidersRequestsListInDriver> ridersRequestsListInDriver;
    ArrayList<RidersRequestsListInDriver> ridersRequestsListInDriverFull;
    FragmentManager supportFragmentManager;

    public DriverRequestsAdapter(Context context, FragmentManager supportFragmentManager, ArrayList<RidersRequestsListInDriver> ridersRequestsListInDriver) {
        this.context=context;
        this.ridersRequestsListInDriver= ridersRequestsListInDriver;
        ridersRequestsListInDriverFull = new ArrayList<>(ridersRequestsListInDriver);
        this.supportFragmentManager = supportFragmentManager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.driver_pending_request_card1, viewGroup, false);

        LayoutInflater inflater1 = LayoutInflater.from(viewGroup.getContext());
        View view2 = inflater1.inflate(R.layout.accepted_requests_card1, viewGroup, false);

        switch (i) {
            case 0: return new ViewHolder0(view);
//            case 1: return new ViewHolder2(view);
            case 2: return new ViewHolder2(view2);
            default:return new ViewHolder0(view);

        }
    }

    @Override
    public int getItemViewType(int position) {
//        return position % 2 * 2;
//        ridersRequestsListInDriver
//        return super.getItemViewType(position);
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
                viewHolder0.originName.setText(ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getRider_origin_name());
                viewHolder0.destinationName.setText(ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getRider_destination_name());
                viewHolder0.name.setText(ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getRiderInfo().getName());
                viewHolder0.ageGender.setText(ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getRiderInfo().getGender());
                viewHolder0.contactNo.setText(ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getRiderInfo().getMobile());
                viewHolder0.travellDate.setText(ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getTimeAndDateRider());
                viewHolder0.offerSeats.setText(ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getSeats());
                viewHolder0.availableSeats.setText(ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getNo_of_available_seats());

                viewHolder0.status.setText(ridersRequestsListInDriver.get(i).getMakeRequest().getStatus());
                viewHolder0.riderSeats.setText(ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getSeatsRider());
                viewHolder0.riderRidePrice.setText(ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getRideCharges()+"");
                viewHolder0.itemView.setOnClickListener(new View.OnClickListener() {
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
                viewHolder0.accept_cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(context, "accepttt", Toast.LENGTH_SHORT).show();

                        Toast.makeText(context, "hhhhhh", Toast.LENGTH_SHORT).show();
                        String currentDriver = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        final String currentRequest= ridersRequestsListInDriver.get(position).getDateAndTime();
                        FirebaseDatabase.getInstance().getReference().child("requests").child("seen").child(currentDriver).child(currentRequest).child("status").setValue("accepted");
                        String requestRider = ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getCurrent_Rider();
                        String request = ridersRequestsListInDriver.get(position).getDateAndTime();
                        FirebaseDatabase.getInstance().getReference().child("requestsRiders").child("unseen").child(requestRider).child(request).child("status").setValue("accepted");


//                        final String currentDriver = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                        String currentRequest= ridersRequestsListInDriver.get(position).getDateAndTime();
//                        FirebaseDatabase.getInstance().getReference().child("requests").child("seen").child(currentDriver).child(currentRequest).child("status").setValue("accepted");
                    }
                });
                viewHolder0.reject_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        String currentDriver = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                        String currentRequest= ridersRequestsListInDriver.get(position).getDateAndTime();
//                        FirebaseDatabase.getInstance().getReference().child("requests").child("seen").child(currentDriver).child(currentRequest).child("status").setValue("rejected");
                        final String currentDriver = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        final String requestRider = ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getCurrent_Rider();
                        final String request = ridersRequestsListInDriver.get(position).getDateAndTime();
                        final MakeRequest makeRequest = ridersRequestsListInDriver.get(position).getMakeRequest();


                        final RideHistory rideHistory = new RideHistory(makeRequest,"rejected","driver");

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("requestsRiders");
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                boolean isUnseen = dataSnapshot.child("unseen").child(requestRider).hasChild(request);
                                boolean isSeen = dataSnapshot.child("seen").child(requestRider).hasChild(request);
                                if(isUnseen){
                                    addInHistoryUnseen(currentDriver,requestRider,request,rideHistory,position);
                                }
                                if(isSeen){
                                    addInHistorySeen(currentDriver,requestRider,request,rideHistory,position);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
                break;

            case 2:
                ViewHolder2 viewHolder2 = (ViewHolder2)viewHolder;

                viewHolder2.originName.setText(ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getRider_origin_name());
                viewHolder2.destinationName.setText(ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getRider_destination_name());
                viewHolder2.name.setText(ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getRiderInfo().getName());
                viewHolder2.ageGender.setText(ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getRiderInfo().getGender());
                viewHolder2.contactNo.setText(ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getRiderInfo().getMobile());
                viewHolder2.travellDate.setText(ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getTimeAndDateRider());
                viewHolder2.offerSeats.setText(ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getSeats());
                viewHolder2.availableSeats.setText(ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getNo_of_available_seats());

                viewHolder2.status.setText(ridersRequestsListInDriver.get(i).getMakeRequest().getStatus());
                viewHolder2.riderSeats.setText(ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getSeatsRider());
                viewHolder2.riderRidePrice.setText(ridersRequestsListInDriver.get(i).getMakeRequest().getAvailableDriverInfo().getRideCharges()+"");



                viewHolder2.itemView.setOnClickListener(new View.OnClickListener() {
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

                viewHolder2.cancelRequest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String currentDriver = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String currentRequest= ridersRequestsListInDriver.get(position).getDateAndTime();

                        FirebaseDatabase.getInstance().getReference().child("requests").child("seen").child(currentDriver).child(currentRequest).child("status").setValue("pending");
                        String requestRider = ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getCurrent_Rider();
                        String request = ridersRequestsListInDriver.get(position).getDateAndTime();
                        FirebaseDatabase.getInstance().getReference().child("requestsRiders").child("unseen").child(requestRider).child(request).child("status").setValue("pending");
                    }
                });
                viewHolder2.reject_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String currentDriver = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        final String requestRider = ridersRequestsListInDriver.get(position).getMakeRequest().getAvailableDriverInfo().getCurrent_Rider();
                        final String request = ridersRequestsListInDriver.get(position).getDateAndTime();
                        final MakeRequest makeRequest = ridersRequestsListInDriver.get(position).getMakeRequest();



                        final RideHistory rideHistory = new RideHistory(makeRequest,"rejected","driver");

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("requestsRiders");
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                boolean isUnseen = dataSnapshot.child("unseen").child(requestRider).hasChild(request);
                                boolean isSeen = dataSnapshot.child("seen").child(requestRider).hasChild(request);
                                if(isUnseen){
                                    addInHistoryUnseen(currentDriver,requestRider,request,rideHistory,position);
                                }
                                if(isSeen){
                                    addInHistorySeen(currentDriver,requestRider,request,rideHistory,position);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


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
        TextView originName, destinationName,name, ageGender,contactNo, status,
                travellDate,offerSeats,availableSeats, riderSeats, riderRidePrice, riderMessage;
        Button accept_cancel_btn, reject_btn;

        public ViewHolder0(@NonNull View itemView) {
            super(itemView);

            originName = itemView.findViewById(R.id.requestedRider1OriginName_p);
            destinationName = itemView.findViewById(R.id.requestedRider1DestinationName_p);
            name = itemView.findViewById(R.id.requestedRider1name_p);
            ageGender = itemView.findViewById(R.id.requestedRider1AgeGender_p);
            contactNo = itemView.findViewById(R.id.requestedRider1contactNo_p);
            travellDate = itemView.findViewById(R.id.requestedRider1dateTime_p);
            offerSeats = itemView.findViewById(R.id.requestedRider1OfferedSeats_p);
            availableSeats = itemView.findViewById(R.id.requestedRider1AvailableSeats_p);

            status = itemView.findViewById(R.id.requestedDriverStatus_p);
            riderSeats = itemView.findViewById(R.id.requestedDriverReseerrvedSeats_p);
            riderRidePrice = itemView.findViewById(R.id.requestedDriverRidePrice_p);

            accept_cancel_btn = itemView.findViewById(R.id.accept_cancel_btn_p);
            reject_btn = itemView.findViewById(R.id.reject_btn_p);

        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder {

        TextView originName, destinationName,name, ageGender,contactNo, status,
                travellDate,offerSeats,availableSeats, riderSeats, riderRidePrice, riderMessage;
        Button cancelRequest, reject_btn;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);

            reject_btn = itemView.findViewById(R.id.reject_btn_ac);

            originName = itemView.findViewById(R.id.requestedRider1OriginName_ac);
            destinationName = itemView.findViewById(R.id.requestedRider1DestinationName_ac);
            name = itemView.findViewById(R.id.requestedRider1name_ac);
            ageGender = itemView.findViewById(R.id.requestedRider1AgeGender_ac);
            contactNo = itemView.findViewById(R.id.requestedRider1contactNo_ac);
            travellDate = itemView.findViewById(R.id.requestedRider1dateTime_ac);
            offerSeats = itemView.findViewById(R.id.requestedRider1OfferedSeats_ac);
            availableSeats = itemView.findViewById(R.id.requestedRider1AvailableSeats_ac);

            status = itemView.findViewById(R.id.requestedDriverStatus_ac);
            riderSeats = itemView.findViewById(R.id.requestedDriverReseerrvedSeats_ac);
            riderRidePrice = itemView.findViewById(R.id.requestedDriverRidePrice_ac);

            cancelRequest = itemView.findViewById(R.id.accept_cancel_btn_ac);
            reject_btn = itemView.findViewById(R.id.reject_btn_ac);

        }




    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<RidersRequestsListInDriver>  filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() ==0){
                filteredList.addAll(ridersRequestsListInDriverFull);
            }else {
                String filteredPattern = constraint.toString().toLowerCase().trim();
                for (RidersRequestsListInDriver item:ridersRequestsListInDriverFull) {
                    if(item.getMakeRequest().getAvailableDriverInfo().getRiderInfo().getName().toLowerCase().contains(filteredPattern)){
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

    private void addInHistoryUnseen(String currentDriver1, final String requestRider1, String request1, RideHistory rideHistory1, int position1){

        Toast.makeText(context, "unseen", Toast.LENGTH_SHORT).show();
        final String currentDriver = currentDriver1;
        final String requestRider = requestRider1;
        final String request = request1;
        final RideHistory rideHistory = rideHistory1;
        final int position = position1;
        FirebaseDatabase.getInstance().getReference().child("requests").child("seen").child(currentDriver).child(request).child("status").setValue("rejected").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                FirebaseDatabase.getInstance().getReference().child("requestsRiders").child("unseen").child(requestRider).child(request).child("status").setValue("rejected").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseDatabase.getInstance().getReference().child("requests").child("history_driver").child(currentDriver).child(ridersRequestsListInDriver.get(position).getDateAndTime()).setValue(rideHistory).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                FirebaseDatabase.getInstance().getReference().child("requests").child("seen").child(currentDriver).child(request).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        FirebaseDatabase.getInstance().getReference().child("requestsRiders").child("history_rider").child(requestRider).child(ridersRequestsListInDriver.get(position).getDateAndTime()).setValue(rideHistory).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                FirebaseDatabase.getInstance().getReference().child("requestsRiders").child("unseen").child(requestRider).child(request).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(context, "Successfully rejected request", Toast.LENGTH_SHORT).show();
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



            }
        });



    }
    private void addInHistorySeen(String currentDriver1, final String requestRider1, String request1, RideHistory rideHistory1, int position1) {
        Toast.makeText(context, "seen", Toast.LENGTH_SHORT).show();
        final String currentDriver = currentDriver1;
        final String requestRider = requestRider1;
        final String request = request1;
        final RideHistory rideHistory = rideHistory1;
        final int position = position1;

        FirebaseDatabase.getInstance().getReference().child("requestsRiders").child("seen").child(requestRider).child(request).child("status").setValue("rejected").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                FirebaseDatabase.getInstance().getReference().child("requests").child("history_driver").child(currentDriver).child(ridersRequestsListInDriver.get(position).getDateAndTime()).setValue(rideHistory).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseDatabase.getInstance().getReference().child("requests").child("seen").child(currentDriver).child(request).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                FirebaseDatabase.getInstance().getReference().child("requestsRiders").child("history_rider").child(requestRider).child(ridersRequestsListInDriver.get(position).getDateAndTime()).setValue(rideHistory).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        FirebaseDatabase.getInstance().getReference().child("requestsRiders").child("seen").child(requestRider).child(request).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(context, "Successfully rejected request", Toast.LENGTH_SHORT).show();
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
    }
}