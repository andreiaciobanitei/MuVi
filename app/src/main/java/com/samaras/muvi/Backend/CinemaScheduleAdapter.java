package com.samaras.muvi.Backend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.samaras.muvi.Backend.Models.CinemaMovieInfo;
import com.samaras.muvi.Backend.Models.MovieInfo;
import com.samaras.muvi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CinemaScheduleAdapter extends RecyclerView.Adapter<CinemaScheduleAdapter.MovieViewHolder>{

    private static OnItemClickListener listener;
    ArrayList<CinemaMovieInfo> movies;
    private Context context;

    public CinemaScheduleAdapter(Context context, ArrayList <CinemaMovieInfo> movies) {
        this.movies = movies;
        this.context = context;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.cinema_movie_list_item, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        CinemaMovieInfo movie = movies.get(position);
        holder.bind(context, movie, listener, position);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    public interface OnItemClickListener {
        void onItemClick(CinemaMovieInfo item, int position);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView movieTitle;
        TextView movieRating;
        TextView movieGenres;
        TextView movieLength;
        TextView movieTime;
        ImageView movieImg;

        public MovieViewHolder(View itemView) {
            super(itemView);
            movieTitle = (TextView) itemView.findViewById(R.id.title);
            movieRating = (TextView) itemView.findViewById(R.id.rating);
            movieTime = (TextView) itemView.findViewById(R.id.time);
            movieLength = (TextView) itemView.findViewById(R.id.length);
            movieGenres = (TextView) itemView.findViewById(R.id.genres);
            movieImg = (ImageView) itemView.findViewById(R.id.img);
        }

        public void bind(Context context, final CinemaMovieInfo movie, final OnItemClickListener listener, final int position ){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(movie, position);
                }
            });
            movieLength.setText("Duration: " + movie.length + " mins");
            movieTime.setText(movie.time);
            movieTitle.setText(movie.title);
            movieRating.setText("Rating: " + movie.rating);
            movieGenres.setText(movie.genres);
            Picasso.with(context)
                    .load(movie.poster_path)
                    .into(movieImg);
        }

    }
}
