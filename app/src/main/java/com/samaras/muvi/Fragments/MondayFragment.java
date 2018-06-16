package com.samaras.muvi.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.samaras.muvi.Activities.MainActivity;
import com.samaras.muvi.Backend.CinemaScheduleAdapter;
import com.samaras.muvi.Backend.Models.CinemaMovieInfo;
import com.samaras.muvi.Backend.Models.CinemaSchedules;
import com.samaras.muvi.Backend.Models.MovieInfo;
import com.samaras.muvi.Backend.MoviesAdapter;
import com.samaras.muvi.R;

import java.util.ArrayList;


public class MondayFragment extends Fragment{
    RecyclerView rv;

    public MondayFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_monday, container, false);
        final CinemaScheduleAdapter adapter = new CinemaScheduleAdapter(getContext(), CinemaSchedules.mondaySchedule);
        adapter.setListener(new CinemaScheduleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CinemaMovieInfo item, int position) {
                final CinemaMovieInfo movie = item;
                new MaterialDialog.Builder(getContext())
                        .title(movie.title)
                        .content(movie.description)
                        .negativeText("Close")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            }})
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }})

                        .negativeColor(getResources().getColor(R.color.md_red_700))
                        .show();
            }
        });



        rv = (RecyclerView) view.findViewById(R.id.movies);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        rv.setAdapter(adapter);
        // Inflate the layout for this fragment
        return  view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }
}