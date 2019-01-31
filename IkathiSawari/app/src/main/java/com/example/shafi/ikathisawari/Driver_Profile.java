package com.example.shafi.ikathisawari;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Driver_Profile extends AppCompatActivity {

    TextView name_DriverProfile,mob_DriverProfile,cnic_DriverProfile,vehicleTypeModelProfile, vehicleRegNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver__profile);
        initializeView();
        getDataFromFirebase();

    }

    private void getDataFromFirebase() {
    }

    private void initializeView() {
        name_DriverProfile = findViewById(R.id.profile_driver_Name);
        mob_DriverProfile = findViewById(R.id.profile_driver_mob);
        cnic_DriverProfile = findViewById(R.id.profile_driver_cnic);
        vehicleTypeModelProfile = findViewById(R.id.profile_Vehicle_TypeModel);
        vehicleRegNo = findViewById(R.id.profile_Vehicle_regNo);
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


            startActivity(new Intent(this,Driver_Profile.class));

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