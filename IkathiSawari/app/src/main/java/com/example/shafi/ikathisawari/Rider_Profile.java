package com.example.shafi.ikathisawari;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.shafi.ikathisawari.models.DriverInfo;
import com.example.shafi.ikathisawari.models.RiderInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Rider_Profile extends AppCompatActivity {


    TextView name_RiderProfile,mob_RiderProfile,cnic_RiderProfile;
    private DatabaseReference mRiderData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rider__profile);

        initializeView();
        getDataFromFirebase();
    }

    private void getDataFromFirebase() {
        String uid = FirebaseAuth.getInstance().getUid();
        mRiderData = FirebaseDatabase.getInstance().getReference("users").child("Rider");
        mRiderData.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RiderInfo riderInfo = dataSnapshot.getValue(RiderInfo.class);
                setViewsValues(riderInfo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setViewsValues(RiderInfo riderInfo) {
        name_RiderProfile.setText(riderInfo.getName());
        mob_RiderProfile.setText(riderInfo.getMobile());
        cnic_RiderProfile.setText(riderInfo.getCnic());

    }

    private void initializeView() {
        name_RiderProfile = findViewById(R.id.profile_rider_Name);
        mob_RiderProfile = findViewById(R.id.profile_rider_mob);
        cnic_RiderProfile = findViewById(R.id.profile_rider_cnic);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.profile){


            startActivity(new Intent(this,Rider_Profile.class));

            return true;
        }
        if(item.getItemId()==R.id.sign_out){

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this,MainActivity.class));

            return true;
        }

        return true;

    }
}
