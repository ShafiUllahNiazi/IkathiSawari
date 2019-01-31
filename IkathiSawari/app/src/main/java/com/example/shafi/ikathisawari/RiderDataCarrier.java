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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RiderDataCarrier extends AppCompatActivity {

    Intent intent;
    Double currentLat, currentLong, destinationLat, destinationLong;
    private DatabaseReference mRiderAvailableRout;
    Button goBackHomeRider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_data_carrier);
        goBackHomeRider = findViewById(R.id.goBackHomeRider);

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
        goBackHomeRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RiderDataCarrier.this,Rider_Home.class));
            }
        });
    }

    private void saveIntoFirebase(){
        mRiderAvailableRout = FirebaseDatabase.getInstance().getReference("Rider Routs");
        String rider_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map locationData = new HashMap();
        locationData.put("currentLat", currentLat);
        locationData.put("currentLong", currentLong);
        locationData.put("destinationLat", destinationLat);
        locationData.put("destinationLong", destinationLong);
        mRiderAvailableRout.child(rider_id).setValue(locationData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RiderDataCarrier.this, "data has been added.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(RiderDataCarrier.this, "Unable to add data", Toast.LENGTH_SHORT).show();
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
