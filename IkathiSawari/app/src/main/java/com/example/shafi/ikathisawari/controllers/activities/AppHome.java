package com.example.shafi.ikathisawari.controllers.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.shafi.ikathisawari.MainActivity;
import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.services.DriverNotificationsService;
import com.example.shafi.ikathisawari.services.UpdateDriverLocation;
import com.google.firebase.auth.FirebaseAuth;

public class AppHome extends AppCompatActivity {

    Button takeRide, offerRide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_app_home);
        takeRide= findViewById(R.id.takeRide);
        offerRide = findViewById(R.id.offerRide);
        takeRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppHome.this, Rider_Screen.class));

            }
        });

        offerRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppHome.this, Driver_Screen.class));

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
//        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId()==R.id.sign_out){

            Intent intent = new Intent(this, com.example.shafi.ikathisawari.services.DriverNotification.class);
            stopService(intent);
            Intent intent2 = new Intent(this, UpdateDriverLocation.class);
            stopService(intent2);
            Intent intent3 = new Intent(this, DriverNotificationsService.class);
            stopService(intent3);

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this,MainActivity.class));

            return true;
        }

        return true;

    }
}
