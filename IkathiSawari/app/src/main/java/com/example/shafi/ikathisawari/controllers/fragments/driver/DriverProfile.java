package com.example.shafi.ikathisawari.controllers.fragments.driver;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.models.DriverInfo;
import com.example.shafi.ikathisawari.models.RiderInfo;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class DriverProfile extends Fragment {

    private static final int GALLERY_PICK = 1;
    private EditText name_Driver;
    private EditText email_Driver;
 
    private EditText mobile_Driver;
    private EditText cnic_Driver;
    private EditText dob;
    private EditText gender;
    ImageView driverProfileImage_profile;
    Button update_driver_profile;
    RiderInfo riderInfo;
    String currentUserUid;
    DatabaseReference databaseReference;

    TextView update_driver_profile_img;

    public DriverProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_driver_profile, container, false);
        driverProfileImage_profile = view.findViewById(R.id.driverProfileImage_profile);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserUid = currentUser.getUid();

        FirebaseDatabase.getInstance().getReference().child("users").child("Driver").child(currentUserUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String image = dataSnapshot.child("image").getValue().toString();
                if (!image.equals("default")) {
                    Picasso.get().load(image).into(driverProfileImage_profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        name_Driver= view.findViewById(R.id.name_Driver_profile);
        email_Driver= view.findViewById(R.id.email_Driver_profile);
        
        mobile_Driver= view.findViewById(R.id.mobile_Driver_profile);
        cnic_Driver= view.findViewById(R.id.cnic_Driver_profile);
        dob= view.findViewById(R.id.dob_Driver_profile);
        gender= view.findViewById(R.id.gender_Driver_profile);
        update_driver_profile= view.findViewById(R.id.update_driver_profile);
        update_driver_profile_img= view.findViewById(R.id.update_driver_profile_img);


        update_driver_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "Choose Image"), GALLERY_PICK);
            }
        });

        update_driver_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = name_Driver.getText().toString();
                String email = email_Driver.getText().toString();
                String mobile = mobile_Driver.getText().toString();
                String cnic =  cnic_Driver.getText().toString();
                String dob1 = dob.getText().toString();
                String gender1 =  gender.getText().toString();
                DriverInfo driverInfo = new DriverInfo(currentUserUid,name, email, mobile, cnic,dob1,gender1,"");
                riderInfo = new RiderInfo(currentUserUid,name, email, mobile, cnic,dob1,gender1,"");

                databaseReference = FirebaseDatabase.getInstance().getReference();



                databaseReference.child("users").child("Driver").child(currentUserUid).setValue(driverInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        databaseReference.child("users").child("Rider").child(currentUserUid).setValue(riderInfo);
                    }
                });
            }
        });


        return view;
    }



    StorageReference mStorageReference;
    @Override
    public void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            Uri imageUri = data.getData();
            StorageReference mStorageReference;
            mStorageReference = FirebaseStorage.getInstance().getReference();

            final StorageReference imgPath = mStorageReference.child("profile_imgs").child(currentUserUid + ".jpg");
            imgPath.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {

                        throw task.getException();

                    }

                    return imgPath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downUri = task.getResult();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("Driver").child(currentUserUid);
                        databaseReference.child("image").setValue(downUri.toString());
//                        Toast.makeText(Setting.this, downUri.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

}
