package com.codepath.flickster.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.flickster.R;
import com.codepath.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity{
  private static final String TAG = MovieDetailsActivity.class.getSimpleName();
  private Movie mMovie;

  @BindView(R.id.backgroundImageView)
  ImageView backgroundImageView;
  @BindView(R.id.posterImageView)
  ImageView posterImageView;
  @BindView(R.id.titleTextview)
  TextView titleTextview;
  @BindView(R.id.bookmarkLayout)
  RelativeLayout bookmarkLayout;
  @BindView(R.id.notInterestedLayout)
  RelativeLayout notInterestedLayout;
  @BindView(R.id.overviewTextView)
  TextView overviewTextView;
  @BindView(R.id.releaseDateTextView)
  TextView releaseDateTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie_details);
    getSupportActionBar().hide();

    ButterKnife.bind(this);

    mMovie = (Movie) getIntent().getSerializableExtra("MOVIE");
    if(mMovie != null) {
      initMovieDetails();
    } else {
      // TODO: Error
    }
  }

  private void initMovieDetails() {
    Picasso.with(this).load(mMovie.getBackdropPath()).fit().into(backgroundImageView);

    int posterDefaultWidth = (int) getResources().getDimension(R.dimen.poster_width);
    Picasso.with(this).load(mMovie.getPosterPath()).resize(posterDefaultWidth, 0).into(posterImageView);

    titleTextview.setText(mMovie.getOriginalTitle());
    overviewTextView.setText(mMovie.getOverview());
    releaseDateTextView.setText("Release date: " + mMovie.getReleaseDate());
  }
}

