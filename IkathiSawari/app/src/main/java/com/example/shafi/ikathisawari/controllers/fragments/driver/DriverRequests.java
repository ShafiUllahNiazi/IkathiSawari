package com.example.shafi.ikathisawari.controllers.fragments.driver;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shafi.ikathisawari.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DriverRequests extends Fragment {


    public DriverRequests() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_requests, container, false);
    }

}
