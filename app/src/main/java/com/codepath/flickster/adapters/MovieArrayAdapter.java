package com.codepath.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.flickster.R;
import com.codepath.flickster.models.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieArrayAdapter extends ArrayAdapter<Movie> {
  private static final String TAG = MovieArrayAdapter.class.getSimpleName();
  public static final int TYPE_MOVIE = 0;
  public static final int TYPE_POPULAR_MOVIE = 1;

  public MovieArrayAdapter(Context context, ArrayList<Movie> movieList) {
    super(context, 0, movieList);
  }

  // Total number of types is the number of enum values
  @Override
  public int getViewTypeCount() {
    return 2;
  }

  @Override
  public int getItemViewType(int position) {
    Movie movie = getItem(position);
    if (movie.isPopular()) {
      return TYPE_POPULAR_MOVIE;
    } else {
      return TYPE_MOVIE;
    }
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // Get the data item for this position
    int type = getItemViewType(position);

    if (convertView == null) {

      if (type == TYPE_POPULAR_MOVIE) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_popular_movie, null);
        PopularMovieViewHolder popularMovieViewHolder = new PopularMovieViewHolder(convertView);
        convertView.setTag(popularMovieViewHolder);
      } else {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie, null);
        MovieViewHolder movieViewHolder = new MovieViewHolder(convertView);
        convertView.setTag(movieViewHolder);
      }
    }

    Movie movie = getItem(position);
    String imagePath = movie.getPosterPath();
    float defaultDpWidth = getContext().getResources().getDimension(R.dimen.poster_width);
    int orientation = getContext().getResources().getConfiguration().orientation;
    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
      imagePath = movie.getBackdropPath();
      defaultDpWidth = getContext().getResources().getDimension(R.dimen.background_width);
      Log.d(TAG, "ORIENTATION_LANDSCAPE: " + defaultDpWidth);
    } else {
      Log.d(TAG, "ORIENTATION_PORTRAIT: " + defaultDpWidth);
    }

    if (type == TYPE_POPULAR_MOVIE) {
      final PopularMovieViewHolder popularMovieViewHolder = (PopularMovieViewHolder) convertView.getTag();
      popularMovieViewHolder.playImage.setVisibility(View.GONE);
      popularMovieViewHolder.movieImage.setImageResource(0);
      Picasso.with(getContext()).load(movie.getBackdropPath())
          .fit().placeholder(R.drawable.movie_image_placeholder)
          .into(popularMovieViewHolder.movieImage, new Callback() {
            @Override
            public void onSuccess() {
              popularMovieViewHolder.playImage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
              popularMovieViewHolder.playImage.setVisibility(View.GONE);
            }
          });
    } else {
      MovieViewHolder movieViewHolder = (MovieViewHolder) convertView.getTag();
      movieViewHolder.movieImage.setImageResource(0);
      // Check orientation configuration and change the image accordingly
      Picasso.with(getContext()).load(imagePath).resize((int) defaultDpWidth, 0).placeholder(R.drawable.movie_image_placeholder).into(movieViewHolder.movieImage);

      movieViewHolder.titleTextView.setText(movie.getOriginalTitle());
      movieViewHolder.overviewTextView.setText(movie.getOverview());
      if (movie.getVoteAverage() > 0) {
        movieViewHolder.avgRatingTextView.setText("Rating: " + movie.getVoteAverage());
      }
    }

    return convertView;
  }

  public static class MovieViewHolder {
    @BindView(R.id.movieImage)
    ImageView movieImage;
    @BindView(R.id.titleTextView)
    TextView titleTextView;
    @BindView(R.id.overviewTextView)
    TextView overviewTextView;
    @BindView(R.id.avgRatingTextView)
    TextView avgRatingTextView;

    public MovieViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }

  public static class PopularMovieViewHolder {
    @BindView(R.id.movieImage)
    ImageView movieImage;
    @BindView(R.id.playImage)
    ImageView playImage;

    public PopularMovieViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }
}
