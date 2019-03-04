package com.example.shafi.ikathisawari.controllers.fragments.rider;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shafi.ikathisawari.R;
import com.example.shafi.ikathisawari.controllers.adapters.AvailableDriversAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AvailableDrivers#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AvailableDrivers extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AvailableDriversAdapter availableDriversAdapter;

    ArrayList<String> availableDriversList;


    public AvailableDrivers() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AvailableDrivers.
     */
    // TODO: Rename and change types and number of parameters
    public static AvailableDrivers newInstance(String param1, String param2) {
        AvailableDrivers fragment = new AvailableDrivers();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_available_drivers, container, false);
        availableDriversList = getArguments().getStringArrayList("availableDriversList");
        recyclerView = view.findViewById(R.id.recyclerViewAvailableDrivers);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        availableDriversAdapter = new AvailableDriversAdapter(getActivity(), availableDriversList);

        recyclerView.setAdapter(availableDriversAdapter);

        return view;
    }

}
