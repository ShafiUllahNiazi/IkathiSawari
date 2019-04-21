package com.example.shafi.ikathisawari;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shafi.ikathisawari.models.DriverInfo;
import com.example.shafi.ikathisawari.models.RiderInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class SignUpScreen extends AppCompatActivity {


    private EditText name_signup;
    private EditText email_signup;
    private EditText password_signup;
    private EditText rePassword_signup;
    private EditText mobile_signup;
    private EditText cnic_signup;
    private EditText dob;
    private EditText gender;
    private Button signUpBtn_signup;
    private Button signIn_Btn_signup;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    RiderInfo riderInfo;
    String currentUserUid;
    int myYear, myMonth, myDay;
    boolean isDate = true;


    String email;



//    private TabLayout sign_up_tablayout;
//    public ViewPager sign_up_viewPager;
//    public Sign_Up_View_Pager_Adapter sign_up_view_pager_adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);
//        sign_up_tablayout=findViewById(R.id.sign_up_tablayout);
//        sign_up_viewPager=findViewById(R.id.sign_up_viewPager);
//        sign_up_view_pager_adapter=new Sign_Up_View_Pager_Adapter(getSupportFragmentManager());
//
//        sign_up_view_pager_adapter.addFragment(new DriverSignUp(),"User Sign up");
//        sign_up_viewPager.setAdapter(sign_up_view_pager_adapter);
//        sign_up_tablayout.setupWithViewPager(sign_up_viewPager);
        name_signup = findViewById(R.id.name_signup);
        email_signup = findViewById(R.id.email_signup);
        password_signup = findViewById(R.id.password_signup);
        rePassword_signup = findViewById(R.id.repassword_signup);
        mobile_signup = findViewById(R.id.mobileNo_signup);
        cnic_signup = findViewById(R.id.cnic_signup);
        dob = findViewById(R.id.dob_signup);
        gender = findViewById(R.id.gender_signup);
        signUpBtn_signup = findViewById(R.id.signup_Btn_signup);
        signIn_Btn_signup = findViewById(R.id.signup_Btn_signup);

        progressDialog=new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDTW();
            }
        });

        signUpBtn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

    }

    public void createAccount(){
        String password=password_signup.getText().toString();
        if(((password_signup.getText().toString().length())<6) && (rePassword_signup.getText().toString().length()<6)){
            Toast.makeText(this, "Please Enter Correct Password", Toast.LENGTH_SHORT).show();
            password_signup.setError("Password is too small");
            rePassword_signup.setError("Password is too small");
            password_signup.setText(null);
            rePassword_signup.setText(null);
            return;
        }
        if(!(password_signup.getText().toString().equals(rePassword_signup.getText().toString()))){
            Toast.makeText(this, "Please Enter Correct Password", Toast.LENGTH_SHORT).show();
            password_signup.setError("un-match Password");
            rePassword_signup.setError("un-match Password");
            password_signup.setText(null);
            rePassword_signup.setText(null);
            return;
        }


        if(!(TextUtils.isEmpty(email_signup.getText()))) {

            email = email_signup.getText().toString();
        }
        else{
            Toast.makeText(this, "Please Write Your Number", Toast.LENGTH_SHORT).show();
            return;
        }

        if((!(TextUtils.isEmpty(rePassword_signup.getText())))&&(!(TextUtils.isEmpty(password_signup.getText()))) && (password_signup.getText().toString().equals(rePassword_signup.getText().toString()))) {
            password = password_signup.getText().toString();
        }
        else{
            Toast.makeText(this, "Please Write Your Password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Account is Creating");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            currentUserUid = currentUser.getUid();
                            String name = name_signup.getText().toString();
                            String email = email_signup.getText().toString();
                            String mobile = mobile_signup.getText().toString();
                            String cnic =  cnic_signup.getText().toString();
                            String dob1 = dob.getText().toString();
                            String gender1 =  gender.getText().toString();
//                            String seats = noOfSeats.getText().toString();

                            DriverInfo driverInfo = new DriverInfo(currentUserUid,name, email, mobile, cnic,dob1,gender1,"default");
                            riderInfo = new RiderInfo(currentUserUid,name, email, mobile, cnic,dob1,gender1,"default");

                            firebaseDatabase = FirebaseDatabase.getInstance();
                            databaseReference=firebaseDatabase.getReference();



                            databaseReference.child("users").child("Driver").child(currentUserUid).setValue(driverInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    databaseReference.child("users").child("Rider").child(currentUserUid).setValue(riderInfo);
                                }
                            });



                            progressDialog.cancel();
                            Toast.makeText(SignUpScreen.this, "Authentication Successful.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpScreen.this,MainActivity.class));
                        } else {
                            progressDialog.cancel();
                            Toast.makeText(SignUpScreen.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void setDTW() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog= new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                Toast.makeText(getContext(), dayOfMonth+"", Toast.LENGTH_SHORT).show();
                String y,mon,d;
                myYear = year;
                myMonth = month+1;
                myDay = dayOfMonth;
                y = myYear+"";
                if(myMonth<10)
                    mon = "0"+myMonth;
                else
                    mon = ""+myMonth;
                if(myDay<10)
                    d = "0"+myDay;
                else
                    d = ""+myDay;

//                dateTimeText.setText(myHour+":"+myMinute+" on "+myDay+" "+myMonth+":"+myYear);
                dob.setText(y+"-"+mon+"-"+d);

                isDate = true;

            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }
}
