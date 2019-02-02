package com.example.shafi.ikathisawari;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.shafi.ikathisawari.models.DriverInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DriverSignUp  extends Fragment {

    private EditText name_Driver;
    private EditText email_Driver;
    private EditText password_Driver;
    private EditText rePassword_Driver;
    private EditText mobile_Driver;
    private EditText cnic_Driver;
    private EditText type_and_model;
    private EditText regNumber;
    private EditText noOfSeats;
    private Button signUpBtn_Driver;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    String email;
    String selectedPosition;
    String selectedItem;

    ///////////////////////////////
    Button ownerlist_select_button;
    TextView selected_owners_shower;

    String [] all_owners_name_list;
    ArrayList<String> all_owners_uid_list;

    ArrayList<String> selected_owners_name_list;
    ArrayList<String> selected_owners_uid_list;

    boolean[] checkedItems;
    ArrayList<Integer> selected_owner_positions;

    ArrayList <String> temp_owners_name_Arraylist;

    String uid_of_selected_owner;
    String name_of_selected_owner;

    ///////////////////////////////////

    View sign_up_view;

    public DriverSignUp (){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        sign_up_view=inflater.inflate(R.layout.fragment_driver_sign_up,container,false);

        name_Driver= (EditText) sign_up_view.findViewById(R.id.name_Driver);
        email_Driver=(EditText) sign_up_view.findViewById(R.id.email_Driver);
        password_Driver=(EditText) sign_up_view.findViewById(R.id.password_Driver);
        rePassword_Driver=(EditText) sign_up_view.findViewById(R.id.repassword_Driver);
        mobile_Driver=(EditText) sign_up_view.findViewById(R.id.mobile_Driver);
        cnic_Driver=(EditText) sign_up_view.findViewById(R.id.cnic_Driver);
        type_and_model=(EditText) sign_up_view.findViewById(R.id.type_and_model);
        regNumber=(EditText) sign_up_view.findViewById(R.id.regNumber);
        noOfSeats=(EditText) sign_up_view.findViewById(R.id.noOfSeats);
        signUpBtn_Driver=(Button) sign_up_view.findViewById(R.id.signUpBtn_Driver);

        progressDialog=new ProgressDialog(this.getActivity());

        firebaseAuth = FirebaseAuth.getInstance();


        signUpBtn_Driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });


        return sign_up_view;
    }


    public void createAccount(){
        String password=password_Driver.getText().toString();
        if(((password_Driver.getText().toString().length())<6) && (rePassword_Driver.getText().toString().length()<6)){
            Toast.makeText(getActivity(), "Please Enter Correct Password", Toast.LENGTH_SHORT).show();
            password_Driver.setError("Password is too small");
            rePassword_Driver.setError("Password is too small");
            password_Driver.setText(null);
            rePassword_Driver.setText(null);
            return;
        }
        if(!(password_Driver.getText().toString().equals(rePassword_Driver.getText().toString()))){
            Toast.makeText(getActivity(), "Please Enter Correct Password", Toast.LENGTH_SHORT).show();
            password_Driver.setError("un-match Password");
            rePassword_Driver.setError("un-match Password");
            password_Driver.setText(null);
            rePassword_Driver.setText(null);
            return;
        }


        if(!(TextUtils.isEmpty(email_Driver.getText()))) {

            email = email_Driver.getText().toString();
        }
        else{
            Toast.makeText(getActivity(), "Please Write Your Number", Toast.LENGTH_SHORT).show();
            return;
        }

        if((!(TextUtils.isEmpty(rePassword_Driver.getText())))&&(!(TextUtils.isEmpty(password_Driver.getText()))) && (password_Driver.getText().toString().equals(rePassword_Driver.getText().toString()))) {
            password = password_Driver.getText().toString();
        }
        else{
            Toast.makeText(getActivity(), "Please Write Your Password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Account is Creating");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String name = name_Driver.getText().toString();
                            String email = email_Driver.getText().toString();
                            String mobile = mobile_Driver.getText().toString();
                            String cnic =  cnic_Driver.getText().toString();
                            String type_model = type_and_model.getText().toString();
                            String reg_no =  regNumber.getText().toString();
                            String seats = noOfSeats.getText().toString();

                            DriverInfo driverInfo = new DriverInfo(name, email, mobile, cnic, type_model, reg_no, seats);

                            firebaseDatabase = FirebaseDatabase.getInstance();
                            databaseReference=firebaseDatabase.getReference();

                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            String currentUserUid = currentUser.getUid();

                            databaseReference.child("users").child("Driver").child(currentUserUid).setValue(driverInfo);

                            progressDialog.cancel();
                            Toast.makeText(getActivity(), "Authentication Successful.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(),MainActivity.class));
                        } else {
                            progressDialog.cancel();
                            Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}














































//import android.content.Context;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//
///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link DriverSignUp.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link DriverSignUp#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class DriverSignUp extends Fragment {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    private OnFragmentInteractionListener mListener;
//
//    public DriverSignUp() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment DriverSignUp.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static DriverSignUp newInstance(String param1, String param2) {
//        DriverSignUp fragment = new DriverSignUp();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.fragment_driver_sign_up, container, false);
//        return view;
//        // Inflate the layout for this fragment
////        return inflater.inflate(R.layout.fragment_driver_sign_up, container, false);
//    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
//}
