package com.example.shafi.ikathisawari;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyIntentService extends IntentService {


    DatabaseReference mydatabse;

    public static final int id = 45612;

    NotificationCompat.Builder notification;

    Boolean checkstart;

    public MyIntentService() {
        super("myintentservice");
        checkstart = false;

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String driver = FirebaseAuth.getInstance().getCurrentUser().getUid();


        if (true) {
            mydatabse = FirebaseDatabase.getInstance().getReference("users").child("Driver").child(driver);

            Log.i("hyhello", "i am in service");

            notification = new NotificationCompat.Builder(this);
            notification.setAutoCancel(true);

            mydatabse.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.i("hyhello11", "i am out of service databse");

                    String mymail = "";

                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        mymail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    }

                    if (!dataSnapshot.child("request").getValue().toString().equals("")) {

                        notification.setSmallIcon(R.drawable.common_google_signin_btn_icon_dark);
                        notification.setTicker("Appointment Notification");
                        notification.setWhen(System.currentTimeMillis());
                        notification.setContentTitle("asdsa");
                        notification.setContentText("ssad");
                        Log.i("hyhello12", "i am in service databse");
                        Intent intent = new Intent(MyIntentService.this, DriverDataCarrier.class);

                            /*Intent intent = new Intent(MyIntentService.this, ReadNotification.class);

                            intent.putExtra("name", dataSnapshot.child(ds.getKey()).child("name").getValue()+"");
                            intent.putExtra("phonenumber", dataSnapshot.child(ds.getKey()).child("phonenumber").getValue()+"");
                            intent.putExtra("body", dataSnapshot.child(ds.getKey()).child("body").getValue()+"");
                            intent.putExtra("mykey", ds.getKey()+"")*/
                        ;

                        PendingIntent pendingIntent = PendingIntent.getActivity(MyIntentService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        notification.setContentIntent(pendingIntent);


                        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        nm.notify(id, notification.build());
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        checkstart = true;

    }
}
