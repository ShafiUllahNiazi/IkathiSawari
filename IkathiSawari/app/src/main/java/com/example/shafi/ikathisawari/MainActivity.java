package com.example.shafi.ikathisawari;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Switch roleSwitch;
    private EditText logIn_email;
    private EditText logIn_password;
    private Button logIn_button;

    private TextView sign_up;


    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressBar logIn_progressBar;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
//        logIn_progressBar=(ProgressBar) findViewById(R.id.logIn_progressbar);
        progressDialog = new ProgressDialog(this);

        logIn_email = (EditText) findViewById(R.id.email_Login);
        logIn_password = (EditText) findViewById(R.id.password_Login);
        logIn_button = (Button) findViewById(R.id.signin_Btn);


//        logIn_progressBar=(ProgressBar) findViewById(R.id.logIn_progressbar);
        sign_up = findViewById(R.id.signUp);

        logIn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log_in_function();
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUpScreen.class));
            }
        });


    }



    public  void  log_in_function() {

        String email = logIn_email.getText().toString();
        String password = logIn_password.getText().toString();


        if (TextUtils.isEmpty(email)) {
//            logIn_progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(MainActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
            logIn_email.setError("Write Your Email");
            logIn_email.setText(null);
            return;
        }
        if (TextUtils.isEmpty(password)) {
//            logIn_progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(MainActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            logIn_password.setError("Write Your Password");
            logIn_password.setText(null);
            return;
        }


//        email=email.toString().concat("@email.com");

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            final FirebaseUser user = firebaseAuth.getCurrentUser();
                            firebaseDatabase = FirebaseDatabase.getInstance();
                            firebaseDatabase.getReference().child("users").addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot dt : dataSnapshot.child("Driver").getChildren()) {
                                        if (dt.getKey().toString().equals(user.getUid().toString())) {
//                                            logIn_progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(MainActivity.this, "Authentication is Sucessful.", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(MainActivity.this, Driver_Home.class));
                                        }
                                    }
                                    for (DataSnapshot dt : dataSnapshot.child("Rider").getChildren()) {
                                        if (dt.getKey().toString().equals(user.getUid().toString())) {
//                                            logIn_progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(MainActivity.this, "Authentication is Sucessful.", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(MainActivity.this, Rider_Home.class));
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }


                            });
                        }
                    }


                });

    }


}
