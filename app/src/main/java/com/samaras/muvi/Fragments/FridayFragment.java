package com.samaras.muvi.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.samaras.muvi.Backend.CinemaScheduleAdapter;
import com.samaras.muvi.Backend.Models.CinemaSchedules;
import com.samaras.muvi.R;


public class FridayFragment extends Fragment{
    RecyclerView rv;

    public FridayFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friday, container, false);
        final CinemaScheduleAdapter adapter = new CinemaScheduleAdapter(getContext(), CinemaSchedules.fridaySchedule    );
        rv = (RecyclerView) view.findViewById(R.id.movies);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        rv.setAdapter(adapter);
        // Inflate the layout for this fragment
        return  view;
    }

}