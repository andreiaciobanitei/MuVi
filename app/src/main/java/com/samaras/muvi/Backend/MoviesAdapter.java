package com.samaras.muvi.Backend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.samaras.muvi.Backend.Models.MovieInfo;
import com.samaras.muvi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>{

    private static OnItemClickListener listener;
    ArrayList<MovieInfo> movies;
    private Context context;

    public MoviesAdapter(Context context, ArrayList <MovieInfo> movies) {
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
                .inflate(R.layout.movie_list_item, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        MovieInfo movie = movies.get(position);
        holder.bind(context, movie, listener, position);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void deleteMovie(int position) {
        movies.remove(position);
        notifyItemRemoved(position);
    }

    public interface OnItemClickListener {
        void onItemClick(MovieInfo item, int position);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView movieImg;
        TextView movieTitle;
        TextView movieRating;
        TextView movieGenres;
        TextView movieDescription;

        public MovieViewHolder(View itemView) {
            super(itemView);
            movieImg = (ImageView) itemView.findViewById(R.id.img);
            movieTitle = (TextView) itemView.findViewById(R.id.title);
            movieRating = (TextView) itemView.findViewById(R.id.rating);
            movieGenres = (TextView) itemView.findViewById(R.id.genres);
            movieDescription = (TextView) itemView.findViewById(R.id.description);
        }

        public void bind(Context context, final MovieInfo movie, final OnItemClickListener listener, final int position ){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(movie, position);
                }
            });

            movieTitle.setText(movie.title);
            movieRating.setText(movie.rating);
            movieDescription.setText(movie.shortDescription);
            movieGenres.setText(movie.genres);
            Picasso.with(context)
                    .load(movie.photoUrl)
                    .into(movieImg);
        }

    }
}
