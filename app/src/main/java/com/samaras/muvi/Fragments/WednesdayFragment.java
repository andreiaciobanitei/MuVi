package com.samaras.muvi.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.samaras.muvi.Backend.CinemaScheduleAdapter;
import com.samaras.muvi.Backend.Models.CinemaMovieInfo;
import com.samaras.muvi.Backend.Models.CinemaSchedules;
import com.samaras.muvi.R;

import java.util.ArrayList;


public class WednesdayFragment extends Fragment{

    private RecyclerView rv_movies;
    RecyclerView rv;
    public WednesdayFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wednesday, container, false);
        final CinemaScheduleAdapter adapter = new CinemaScheduleAdapter(getContext(), CinemaSchedules.wednesdaySchedule);
        rv = (RecyclerView) view.findViewById(R.id.movies);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        rv.setAdapter(adapter);
        // Inflate the layout for this fragment
        return  view;
    }

}