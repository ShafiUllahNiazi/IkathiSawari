package com.example.shafi.ikathisawari.controllers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.shafi.ikathisawari.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View mWindow;
    private Context mContext;
    ArrayList<String> s;

    public CustomInfoWindowAdapter(Context context, ArrayList<String> s) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
        this.s = s;
    }

    private void rendowWindowText(Marker marker, View view){
        TextView estimateTime = view.findViewById(R.id.estimateTime);
        TextView estimateD = view.findViewById(R.id.estimateDistance);
        TextView estimateC = view.findViewById(R.id.estimateCharges);
        estimateC.setText(s.get(2));
        estimateD.setText(s.get(1));
        estimateTime.setText(s.get(0));


//        String title = marker.getTitle();
//        TextView estimateTime = view.findViewById(R.id.estimateTime);
//
//        if(!title.equals("")){
//            estimateTime.setText(title);
//        }
//
//        String snippet = marker.getSnippet();
//        TextView tvSnippet =  view.findViewById(R.id.estimateDistance);
//
//        if(!snippet.equals("")){
//            tvSnippet.setText(snippet);
//        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }
}
