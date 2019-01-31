package com.example.shafi.ikathisawari;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DriverDataCarrier extends AppCompatActivity {

    Intent intent;
    float currentLat, currentLong, destinationLat, destinationLong;
    private DatabaseReference mDriverAvailableRout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_data_carrier);

        intent = getIntent();
        currentLat = intent.getFloatExtra("currentLat", 0);
        currentLong = intent.getFloatExtra("currentLong", 0);
        destinationLat = intent.getFloatExtra("destinationLat", 0);
        destinationLong = intent.getFloatExtra("destinationLong", 0);

        saveIntoFirebase();

    }

    private void saveIntoFirebase(){
        mDriverAvailableRout = FirebaseDatabase.getInstance().getReference("Available Routs");
        String driver_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map locationData = new HashMap();
        locationData.put("currentLat", currentLat);
        locationData.put("currentLong", currentLong);
        locationData.put("destinationLat", destinationLat);
        locationData.put("destinationLong", destinationLong);
        mDriverAvailableRout.child(driver_id).setValue(locationData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(DriverDataCarrier.this, "data has been added.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(DriverDataCarrier.this, "Unable to add data", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}
