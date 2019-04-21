package com.example.shafi.ikathisawari.controllers.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.shafi.ikathisawari.MainActivity;
import com.example.shafi.ikathisawari.R;

public class AppHome extends AppCompatActivity {

    Button takeRide, offerRide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
