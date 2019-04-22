package com.example.shafi.ikathisawari;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.shafi.ikathisawari.models.DriverInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Driver_Profile extends AppCompatActivity {

    TextView name_DriverProfile,mob_DriverProfile,cnic_DriverProfile;

    private DatabaseReference mDriverData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver__profile);
        initializeView();

        getDataFromFirebase();


    }

    private void getDataFromFirebase() {
        String uid = FirebaseAuth.getInstance().getUid();
        mDriverData = FirebaseDatabase.getInstance().getReference("users").child("Driver");
        mDriverData.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DriverInfo driverInfo = dataSnapshot.getValue(DriverInfo.class);
                setViewsValues(driverInfo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setViewsValues(DriverInfo driverInfo) {
        name_DriverProfile.setText(driverInfo.getName());
        mob_DriverProfile.setText(driverInfo.getMobile());
        cnic_DriverProfile.setText(driverInfo.getCnic());

    }

    private void initializeView() {
        name_DriverProfile = findViewById(R.id.profile_driver_Name);
        mob_DriverProfile = findViewById(R.id.profile_driver_mob);
        cnic_DriverProfile = findViewById(R.id.profile_driver_cnic);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId()==R.id.sign_out){

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this,MainActivity.class));

            return true;
        }

        return true;

    }

}
