package com.example.shafi.ikathisawari.controllers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shafi.ikathisawari.R;

import java.util.ArrayList;

public class AvailableDriversAdapter extends RecyclerView.Adapter<AvailableDriversAdapter.ViewHolder> {

    Context context;
    ArrayList<String> availableDriversList;

    public AvailableDriversAdapter(Context context, ArrayList<String> availableDriversList) {
        this.context = context;
        this.availableDriversList = availableDriversList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.available_drivers_card, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.driversKey.setText(availableDriversList.get(i));

    }

    @Override
    public int getItemCount() {
        return availableDriversList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView driversKey;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            driversKey = itemView.findViewById(R.id.driverKey);

        }
    }
}
