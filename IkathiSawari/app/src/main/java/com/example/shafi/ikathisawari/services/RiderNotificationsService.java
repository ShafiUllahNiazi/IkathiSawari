package com.example.shafi.ikathisawari.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.controllers.activities.Driver_Screen;
import com.example.shafi.ikathisawari.controllers.activities.Rider_Screen;
import com.example.shafi.ikathisawari.models.MakeRequest;
import com.example.shafi.ikathisawari.models.RiderRidePointsDriver;
import com.example.shafi.ikathisawari.models.RidersRequestsListInDriver;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RiderNotificationsService extends Service {
    ArrayList<RidersRequestsListInDriver> ridersRequestsListInDriver;
    private float radius = 2000;

    public RiderNotificationsService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        FirebaseApp.initializeApp(getApplicationContext());
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            Toast.makeText(this, "notification Driver", Toast.LENGTH_SHORT).show();
            String currentRider = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("requestsRiders").child("unseen").child(currentRider);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        ridersRequestsListInDriver = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            MakeRequest request = snapshot.getValue(MakeRequest.class);
//                   RiderInfo rider = snapshot.getValue(RiderInfo.class);
                            RidersRequestsListInDriver riderRequestInDriver = new RidersRequestsListInDriver(snapshot.getKey(), request);
                            if (!(riderRequestInDriver.getMakeRequest().getStatus().equals("rejected"))) {
                                ridersRequestsListInDriver.add(riderRequestInDriver);
                            }

                            Log.d("Time_Datess", snapshot.getKey() + " " + request);
                        }
            //////////////////////////////////////////////////////////////////////////////////
                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("DriverRidePoints");
                        databaseReference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {

                                    GenericTypeIndicator<ArrayList<RiderRidePointsDriver>> t = new GenericTypeIndicator<ArrayList<RiderRidePointsDriver>>() {
                                    };
                                    ArrayList<RiderRidePointsDriver> driverUpdatedPointsList = dataSnapshot.getValue(t);
//                        Toast.makeText(this,driverUpdatedPointsList.get(0).toString(),Toast.LENGTH_LONG).show();

                                    Log.d("tststs", driverUpdatedPointsList.toString());

                                    Location driverCurrentLocation = new Location(LocationManager.GPS_PROVIDER);
                                    driverCurrentLocation.setLatitude(driverUpdatedPointsList.get(driverUpdatedPointsList.size() - 1).getLat());
                                    driverCurrentLocation.setLatitude(driverUpdatedPointsList.get(driverUpdatedPointsList.size() - 1).getLng());


                                    int i = 0;
                                    for (RidersRequestsListInDriver item : ridersRequestsListInDriver) {
                                        Location riderOriginLocation = new Location(LocationManager.GPS_PROVIDER);
                                        riderOriginLocation.setLatitude(item.getMakeRequest().getRiderLatOriginAtRoad());
                                        riderOriginLocation.setLatitude(item.getMakeRequest().getRiderLngOriginAtRoad());

                                        Location riderDestinationLocation = new Location(LocationManager.GPS_PROVIDER);
                                        riderDestinationLocation.setLatitude(item.getMakeRequest().getRiderLatDestinationAtRoad());
                                        riderDestinationLocation.setLatitude(item.getMakeRequest().getRiderLngDestinationAtRoad());

                                        float originDistance = riderOriginLocation.distanceTo(driverCurrentLocation);
                                        float destinationDistance = riderDestinationLocation.distanceTo(driverCurrentLocation);

                                        if (originDistance < radius) {
                                            int notificationId = 1;
                                            String channelId = "channel-01";
                                            String channelName = "Channel Name";
                                            int importance = NotificationManager.IMPORTANCE_HIGH;
                                            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);


                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                NotificationChannel mChannel = new NotificationChannel(
                                                        channelId, channelName, importance);
                                                notificationManager.createNotificationChannel(mChannel);
                                            }


                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);
                                            builder.setSmallIcon(R.mipmap.ic_launcher);
                                            builder.setContentTitle("Notification");
//                    builder.setContentText("Hello this is a test Firebase notification, a new database child has been added");
                                            builder.setContentText(item.getMakeRequest().getRiderInfo().getName() + "Driver is near 2KM to your origin");

                                            Intent intent = new Intent(RiderNotificationsService.this, Rider_Screen.class);
                                            intent.putExtra("orderNotification", "request");
                                            PendingIntent pendingIntent = PendingIntent.getActivity(RiderNotificationsService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                            builder.setContentIntent(pendingIntent);
                                            builder.setAutoCancel(true);


                                            notificationManager.notify(i++, builder.build());


                                        }
                                        if (destinationDistance < radius) {
                                            int notificationId = 1;
                                            String channelId = "channel-01";
                                            String channelName = "Channel Name";
                                            int importance = NotificationManager.IMPORTANCE_HIGH;
                                            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);


                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                NotificationChannel mChannel = new NotificationChannel(
                                                        channelId, channelName, importance);
                                                notificationManager.createNotificationChannel(mChannel);
                                            }


                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);
                                            builder.setSmallIcon(R.mipmap.ic_launcher);
                                            builder.setContentTitle("Notification");
//                    builder.setContentText("Hello this is a test Firebase notification, a new database child has been added");
                                            builder.setContentText(item.getMakeRequest().getRiderInfo().getName() + "Driver is near 2KM to your destination");

                                            Intent intent = new Intent(RiderNotificationsService.this, Rider_Screen.class);
                                            intent.putExtra("orderNotification", "request");
                                            PendingIntent pendingIntent = PendingIntent.getActivity(RiderNotificationsService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                            builder.setContentIntent(pendingIntent);
                                            builder.setAutoCancel(true);


                                            notificationManager.notify(i++, builder.build());
                                        }


                                    }

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


            return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}