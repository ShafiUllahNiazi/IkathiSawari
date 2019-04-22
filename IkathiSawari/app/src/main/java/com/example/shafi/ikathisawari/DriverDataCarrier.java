package com.example.shafi.ikathisawari;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DriverDataCarrier extends AppCompatActivity {

    Intent intent;
    Double currentLat, currentLong, destinationLat, destinationLong;
    private DatabaseReference mDriverAvailableRout;
    Button goBackHomeDriver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_data_carrier);

        goBackHomeDriver = findViewById(R.id.goBackHomeDriver);

        intent = getIntent();

        currentLat = intent.getDoubleExtra("currentLat", 0);
        currentLong = intent.getDoubleExtra("currentLong", 0);
        destinationLat = intent.getDoubleExtra("destinationLat", 1);
        destinationLong = intent.getDoubleExtra("destinationLong", 1);
        Log.i("receiveData", "receiveData: current " + currentLat + "  " + currentLong);
        Log.i("receiveData", "receiveData: destination " + destinationLat + "  " + destinationLong);

        saveIntoFirebase();

    }

    @Override
    protected void onResume() {
        super.onResume();
        goBackHomeDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DriverDataCarrier.this,Driver_Home.class));
            }
        });
    }

    private void saveIntoFirebase(){


//        String driver_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        mDriverAvailableRout = FirebaseDatabase.getInstance().getReference("Available Routs").child(driver_id);
//        GeoFire geoFire = new GeoFire(mDriverAvailableRout);
//        geoFire.setLocation("origin", new GeoLocation(currentLat, currentLong), new GeoFire.CompletionListener() {
//            @Override
//            public void onComplete(String key, DatabaseError error) {
//                if (error != null) {
//                    System.err.println("There was an error saving the location to GeoFire: " + error);
//                } else {
//                    System.out.println("Location saved on server successfully!");
//                }
//            }
//        });
//        geoFire.setLocation("destination", new GeoLocation(destinationLat, destinationLong), new GeoFire.CompletionListener() {
//            @Override
//            public void onComplete(String key, DatabaseError error) {
//                if (error != null) {
//                    System.err.println("There was an error saving the location to GeoFire: " + error);
//                } else {
//                    System.out.println("Location saved on server successfully!");
//                }
//            }
//        });



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
