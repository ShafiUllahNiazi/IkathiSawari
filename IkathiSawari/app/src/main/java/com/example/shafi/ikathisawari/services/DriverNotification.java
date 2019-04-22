package com.example.shafi.ikathisawari.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.controllers.activities.Driver_Screen;
import com.example.shafi.ikathisawari.controllers.fragments.driver.DriverRequestParent;
import com.example.shafi.ikathisawari.models.MakeRequest;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DriverNotification extends Service {

    private static final String TAG = "DriverNotification";
    
    Context context;
    public DriverNotification() {

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Toast.makeText(this, "notification startt", Toast.LENGTH_SHORT).show();
        Log.d("cccccccc","startttttt");
        FirebaseApp.initializeApp(getApplicationContext());
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Log.d("cccccc", "notttt");
//            Toast.makeText(this, "req notification startt111", Toast.LENGTH_SHORT).show();
            String currentDriver = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("requests").child("unseen").child(currentDriver);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("cccc", "onDataChange1:  "+ dataSnapshot.toString());
//                    Toast.makeText(this, "notification startt2222", Toast.LENGTH_SHORT).show();
                    if(dataSnapshot.exists()){
                        int i = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Log.d("cccc", "onDataChange2:  "+ snapshot.getKey());
                            MakeRequest rider = snapshot.getValue(MakeRequest.class);
                            Log.d("cccc", "onDataChange:  "+ rider.getStatus());

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



                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),channelId);
                            builder.setSmallIcon(R.mipmap.ic_launcher);
                            builder.setContentTitle("Ride Request");
//                    builder.setContentText("Hello this is a test Firebase notification, a new database child has been added");
                            builder.setContentText(rider.getRiderInfo().getName() + "requested you");

                            Intent intent = new Intent(DriverNotification.this, Driver_Screen.class);
                            intent.putExtra("orderNotification", "request");
                            PendingIntent pendingIntent = PendingIntent.getActivity(DriverNotification.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            builder.setContentIntent(pendingIntent);
                            builder.setAutoCancel(true);



                            notificationManager.notify(i++, builder.build());

                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



//            databaseReference.addChildEventListener(new ChildEventListener() {
//                @Override
//                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
////                    for (DataSnapshot d: dataSnapshot.) {
////                        Log.d("cccc", "onChildAdded: getting keys  " + );
////                    }
//
////                    Log.d("newChild",dataSnapshot.child(dataSnapshot.getKey()).getValue() +" added "+ dataSnapshot.getKey());
//                    Log.d("newChild",dataSnapshot.child(dataSnapshot.getKey()).getValue() +" added "+ dataSnapshot.getKey());
//
//                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
//                    builder.setSmallIcon(R.mipmap.ic_launcher);
//                    builder.setContentTitle("Ride Request");
////                    builder.setContentText("Hello this is a test Firebase notification, a new database child has been added");
//                    builder.setContentText("d "+ dataSnapshot.getKey()+" "+dataSnapshot.getChildren());
//                    Log.d("cccc",dataSnapshot.getChildren()+" "+ dataSnapshot.getKey());
//                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
//                    notificationManager.notify(1, builder.build());
//                }
//
//                @Override
//                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                    //Log.d("newChild",dataSnapshot.getValue()+" changed"+ dataSnapshot.getKey());
//
//                }
//
//                @Override
//                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
//                    builder.setSmallIcon(R.mipmap.ic_launcher);
//                    builder.setContentTitle("Ride Request");
////                    builder.setContentText("Hello this is a test Firebase notification, a new database child has been added");
//                    builder.setContentText("ggggg");
//                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
//                    notificationManager.notify(1, builder.build());
//                }
//
//                @Override
//                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//
//            });
        }else{
            Log.d("cccccc", "null");
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
}
