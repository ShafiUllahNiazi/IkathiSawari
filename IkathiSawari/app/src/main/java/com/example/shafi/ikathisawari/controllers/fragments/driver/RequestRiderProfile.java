package com.example.shafi.ikathisawari.controllers.fragments.driver;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.models.RiderInfo;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestRiderProfile extends Fragment {


    TextView name;
    Button acceptRequest;
    ArrayList<RidersRequestsListInDriver> ridersRequestsListInDrivers;

    public RequestRiderProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_rider_profile, container, false);

        ridersRequestsListInDrivers=getArguments().getParcelableArrayList("ridersRequestsListInDriver");
        final int position = getArguments().getInt("position");
        name = view.findViewById(R.id.requestRiderName);
        name.setText(ridersRequestsListInDrivers.get(position).getRiderInfo().getName()+" "+ ridersRequestsListInDrivers.get(position).getDateAndTime());
        acceptRequest = view.findViewById(R.id.acceptRequest);
        acceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "hhhhhh", Toast.LENGTH_SHORT).show();
                final String currentDriver = FirebaseAuth.getInstance().getCurrentUser().getUid();


                final String currentRequest= ridersRequestsListInDrivers.get(position).getDateAndTime();
                FirebaseDatabase.getInstance().getReference().child("requests").child(currentDriver).child(currentRequest).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            RiderInfo riderInfo = dataSnapshot.getValue(RiderInfo.class);
                            FirebaseDatabase.getInstance().getReference().child("requestsShift")
                                    .child(currentDriver).child(currentRequest).setValue(riderInfo)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    FirebaseDatabase.getInstance().getReference().child("requests")
                                            .child(currentDriver).child(currentRequest)
                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getActivity(), "DOne", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(getActivity(), "DOne", Toast.LENGTH_SHORT).show();
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
//                DatabaseReference to = FirebaseDatabase.getInstance().getReference().child("requestsShift").child(currentDriver);
//                from.child(currentRequest).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
//                        final Object dataSnapshot1 = dataSnapshot.getValue();
//                        Log.i("onCompletee", "place Holder1 "+dataSnapshot1);
//                        to.child(currentRequest).setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                Log.i("onCompletee", "place Holder2 "+dataSnapshot1);
//                                Log.i("onCompletee", "onComplete: success 1");
//                                if (task.isSuccessful()) {
//                                    Log.i("onCompletee", "onComplete: success");
//                                    // In order to complete the move, we are going to erase
//                                    // the original copy by assigning null as its value.
////                                    from.child(currentRequest).removeValue();
//                                }else {
//                                    Log.i("onCompletee", "onComplete: failure");
//                                }
//
//
//                                                            }
//                        });
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });



//                from.child(currentRequest).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
//                        from.child(currentRequest).removeValue();
//                        Log.i("onCompletee", "place Holderremove ");
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });





            }
        });

        return view;
    }

}