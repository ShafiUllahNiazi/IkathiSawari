package com.example.shafi.ikathisawari.controllers.fragments.driver;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.controllers.adapters.DriverRequestsAdapter;
import com.example.shafi.ikathisawari.models.MakeRequest;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DriverRequests extends Fragment {

    ArrayList<RidersRequestsListInDriver> ridersRequestsListInDriver;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DriverRequestsAdapter driverRequestsAdapter;

    View view;
    TextView emptyView;




    public DriverRequests() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("cyyyy","create");
        setRetainInstance(true);
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_driver_requests, container, false);
        emptyView = view.findViewById(R.id.empty_view_driver_request);
        recyclerView = view.findViewById(R.id.recyclerViewRequestDrivers);


        final String currentDriver = FirebaseAuth.getInstance().getCurrentUser().getUid();


//        final String currentRequest= ridersRequestsListInDrivers.get(position).getDateAndTime();
        FirebaseDatabase.getInstance().getReference().child("requests").child("unseen").child(currentDriver).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    Map<String,Object> requestss = (Map<String, Object>) dataSnapshot.getValue();
                    for (String req: requestss.keySet()){
                        FirebaseDatabase.getInstance().getReference().child("requests").child("seen").child(currentDriver).child(req).setValue(requestss.get(req)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("tttttttt", "doneee"  );
                                FirebaseDatabase.getInstance().getReference().child("requests").child("unseen").child(currentDriver).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d("tttttttt", "movee"  );
                                    }
                                });
                            }
                        });



                    }

//
//                    MakeRequest riderInfo = dataSnapshot.getValue(MakeRequest.class);
//                    FirebaseDatabase.getInstance().getReference().child("requests").child("responded")
//                            .child(currentDriver).child(currentRequest).child("rejected").setValue(riderInfo)
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    FirebaseDatabase.getInstance().getReference().child("requests").child("pending")
//                                            .child(currentDriver).child(currentRequest)
//                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            Toast.makeText(getActivity(), "DOne", Toast.LENGTH_SHORT).show();
//                                            Toast.makeText(getActivity(), "DOne", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                }
//                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });











        //////////////////////////////////////////////////////////////////////











//        String currentDriver = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("requests").child("seen").child(currentDriver);
        databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(dataSnapshot.exists()){
                   ridersRequestsListInDriver =new ArrayList<>();
                   for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                       MakeRequest rider = snapshot.getValue(MakeRequest.class);
//                   RiderInfo rider = snapshot.getValue(RiderInfo.class);
                       RidersRequestsListInDriver riderRequestInDriver = new RidersRequestsListInDriver(snapshot.getKey(),rider);
                       if(!(riderRequestInDriver.getMakeRequest().getStatus().equals("rejected"))){
                           ridersRequestsListInDriver.add(riderRequestInDriver);
                       }

                       Log.d("Time_Datess",snapshot.getKey()+" "+ rider);
                   }

                   if (getActivity() != null) {

                       layoutManager = new LinearLayoutManager(getActivity());
                       recyclerView.setLayoutManager(layoutManager);
//        driverRequestsAdapter = new DriverRequestsAdapter(getActivity(),getActivity().getSupportFragmentManager(), driverRequestsList);


                       driverRequestsAdapter = new DriverRequestsAdapter(getActivity(),getActivity().getSupportFragmentManager(), ridersRequestsListInDriver);


                       recyclerView.setAdapter(driverRequestsAdapter);

                       driverRequestsAdapter.notifyDataSetChanged();
                       recyclerView.setVisibility(View.VISIBLE);
                       emptyView.setVisibility(View.GONE);
                   }
               }else {
                   recyclerView.setVisibility(View.GONE);
                   emptyView.setVisibility(View.VISIBLE);
               }



           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
//        recyclerView = view.findViewById(R.id.recyclerViewRequestDrivers);
//        layoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(layoutManager);
////        driverRequestsAdapter = new DriverRequestsAdapter(getActivity(),getActivity().getSupportFragmentManager(), driverRequestsList);
//
//
//        driverRequestsAdapter = new DriverRequestsAdapter(getActivity(), ridersRequestsListInDriver);
//
//        recyclerView.setAdapter(driverRequestsAdapter);

        return view;
    }

    @Override
    public void onStop() {
//        Toast.makeText(getActivity(), "onStop req", Toast.LENGTH_SHORT).show();

        super.onStop();
        Log.d("cyyyy","stop");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("cyyyy","pause");
    }

    @Override
    public void onDestroy() {
//        Toast.makeText(getActivity(), "onDestroy req", Toast.LENGTH_SHORT).show();
        Log.d("cyyyy","onDestroy");
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("cyyyy","resume");
//        Toast.makeText(getActivity(), "resumme req", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        MenuInflater menuInflater = getActivity().getMenuInflater();

        super.onCreateOptionsMenu(menu, inflater);
//        menu.clear();
        inflater.inflate(R.menu.toolbar_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (driverRequestsAdapter != null){
                    driverRequestsAdapter.getFilter().filter(newText);
                }

                return false;
            }
        });

    }
}
