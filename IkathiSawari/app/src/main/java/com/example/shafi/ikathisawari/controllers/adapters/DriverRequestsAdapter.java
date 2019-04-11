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
import com.example.shafi.ikathisawari.controllers.fragments.driver.RequestRiderProfile;
import com.example.shafi.ikathisawari.models.RidersRequestsListInDriver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DriverRequestsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.driver_pending_request_card, viewGroup, false);

        LayoutInflater inflater1 = LayoutInflater.from(viewGroup.getContext());
        View view2 = inflater1.inflate(R.layout.accepted_requests_card, viewGroup, false);

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
                viewHolder0.content.setText(ridersRequestsListInDriver.get(i).getDateAndTime()+ridersRequestsListInDriver.get(i).getMakeRequest().getRiderInfo().getName());
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
                viewHolder0.acceptRequest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(context, "accepttt", Toast.LENGTH_SHORT).show();
                        final String currentDriver = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String currentRequest= ridersRequestsListInDriver.get(position).getDateAndTime();
                        FirebaseDatabase.getInstance().getReference().child("requests").child("seen").child(currentDriver).child(currentRequest).child("status").setValue("accepted");
                    }
                });
                viewHolder0.rejectRequest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String currentDriver = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String currentRequest= ridersRequestsListInDriver.get(position).getDateAndTime();
                        FirebaseDatabase.getInstance().getReference().child("requests").child("seen").child(currentDriver).child(currentRequest).child("status").setValue("rejected");
                    }
                });
                break;

            case 2:
                ViewHolder2 viewHolder2 = (ViewHolder2)viewHolder;
                viewHolder2.content.setText(ridersRequestsListInDriver.get(i).getDateAndTime()+ridersRequestsListInDriver.get(i).getMakeRequest().getRiderInfo().getName());
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
                        FirebaseDatabase.getInstance().getReference().child("requests").child("seen").child(currentDriver).child(currentRequest).child("status").setValue("rejected");
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
        TextView content,date;
        Button acceptRequest, rejectRequest;

        public ViewHolder0(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content_pending);
            date = itemView.findViewById(R.id.date_pending);
            acceptRequest = itemView.findViewById(R.id.accept_request);
            rejectRequest = itemView.findViewById(R.id.reject_request);

        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder {
        TextView content,date;
        Button cancelRequest;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content_accepted);
            date = itemView.findViewById(R.id.date_accepted);
            cancelRequest = itemView.findViewById(R.id.cancel_request);

        }




    }
}