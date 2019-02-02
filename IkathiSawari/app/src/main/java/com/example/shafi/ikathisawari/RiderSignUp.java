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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.shafi.ikathisawari.models.RiderInfo;
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

public class RiderSignUp  extends Fragment {

    private EditText name_Rider;
    private EditText email_Rider;
    private EditText password_Rider;
    private EditText rePassword_Rider;
    private EditText mobile_Rider;
    private EditText cnic_Rider;
    private Button signUpBtn_Rider;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    public String email;
//    private Spinner signup_Type;
//    private Spinner signup_city;
//    private Spinner ownerlist;

//    private String city[];
//    private String signup_tyeps_array [];
//    private ProgressDialog progressDialog;


//    String email=null;
//    String selectedPosition;
//    String selectedItem;
//
//    ///////////////////////////////
//    Button ownerlist_select_button;
//    TextView selected_owners_shower;
//
//    String [] all_owners_name_list;
//    ArrayList<String> all_owners_uid_list;
//
//    ArrayList<String> selected_owners_name_list;
//    ArrayList<String> selected_owners_uid_list;
//
//    boolean[] checkedItems;
//    ArrayList<Integer> selected_owner_positions;
//
//    ArrayList <String> temp_owners_name_Arraylist;
//
//    String uid_of_selected_owner;
//    String name_of_selected_owner;

    ///////////////////////////////////

    View sign_up_view;

    public RiderSignUp (){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        sign_up_view=inflater.inflate(R.layout.fragment_rider_sign_up,container,false);

        name_Rider= (EditText) sign_up_view.findViewById(R.id.name_Rider);
        email_Rider=(EditText) sign_up_view.findViewById(R.id.email_Rider);
        password_Rider=(EditText) sign_up_view.findViewById(R.id.password_Rider);
        rePassword_Rider=(EditText) sign_up_view.findViewById(R.id.rePassword_Rider);
        mobile_Rider=(EditText) sign_up_view.findViewById(R.id.mobile_Rider);
        cnic_Rider=(EditText) sign_up_view.findViewById(R.id.cnic_Rider);
        signUpBtn_Rider=(Button) sign_up_view.findViewById(R.id.signUpBtn_Rider);
        progressDialog=new ProgressDialog(this.getActivity());

        firebaseAuth = FirebaseAuth.getInstance();

//        progressDialog=new ProgressDialog(this.getActivity());


        signUpBtn_Rider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "name "+name_Rider.getText(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity(), "email "+email_Rider.getText(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity(), "password "+password_Rider.getText(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity(), "repass "+rePassword_Rider.getText(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity(), "mob "+mobile_Rider.getText(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity(), "cnic "+cnic_Rider.getText(), Toast.LENGTH_SHORT).show();
                createAccount();

            }
        });


        return sign_up_view;
    }


    public void createAccount(){
        String password=password_Rider.getText().toString();
        if(((password_Rider.getText().toString().length())<6) && (rePassword_Rider.getText().toString().length()<6)){
            Toast.makeText(getActivity(), "Please Enter Correct Password", Toast.LENGTH_SHORT).show();
            password_Rider.setError("Password is too small");
            rePassword_Rider.setError("Password is too small");
            password_Rider.setText(null);
            rePassword_Rider.setText(null);
            return;
        }
        if(!(password_Rider.getText().toString().equals(rePassword_Rider.getText().toString()))){
            Toast.makeText(getActivity(), "Please Enter Correct Password", Toast.LENGTH_SHORT).show();
            password_Rider.setError("un-match Password");
            rePassword_Rider.setError("un-match Password");
            password_Rider.setText(null);
            rePassword_Rider.setText(null);
            return;
        }


        if(!(TextUtils.isEmpty(email_Rider.getText()))) {

            email = email_Rider.getText().toString();
        }
        else{
            Toast.makeText(getActivity(), "Please Write Your Number", Toast.LENGTH_SHORT).show();
            return;
        }

        if((!(TextUtils.isEmpty(rePassword_Rider.getText())))&&(!(TextUtils.isEmpty(password_Rider.getText()))) && (password_Rider.getText().toString().equals(rePassword_Rider.getText().toString()))) {
            password = password_Rider.getText().toString();
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
                        HashMap<String, String> sign_up_HashMap;
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        String currentUserUid = currentUser.getUid();
                        if (task.isSuccessful()) {
                            String name = name_Rider.getText().toString();
                            String email = email_Rider.getText().toString();
                            String mobile = mobile_Rider.getText().toString();
                            String cnic =  cnic_Rider.getText().toString();

                            RiderInfo riderInfo = new RiderInfo(name, email, mobile, cnic);

                            firebaseDatabase = FirebaseDatabase.getInstance();
                            databaseReference=firebaseDatabase.getReference();

                            databaseReference.child("users").child("Rider").child(currentUserUid).setValue(riderInfo);

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









































//
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
// * {@link RiderSignUp.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link RiderSignUp#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class RiderSignUp extends Fragment {
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
//    public RiderSignUp() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment RiderSignUp.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static RiderSignUp newInstance(String param1, String param2) {
//        RiderSignUp fragment = new RiderSignUp();
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
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_rider_sign_up, container, false);
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
