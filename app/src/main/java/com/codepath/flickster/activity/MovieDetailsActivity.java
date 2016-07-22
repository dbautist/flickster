package com.codepath.flickster.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.flickster.R;
import com.codepath.flickster.models.Movie;
import com.codepath.flickster.util.Config;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieDetailsActivity extends YouTubeBaseActivity {
  private static final String TAG = MovieDetailsActivity.class.getSimpleName();
  private Movie mMovie;

  @BindView(R.id.youtubePlayerView)
  YouTubePlayerView youtubePlayerView;
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
  @BindView(R.id.ratingBar)
  RatingBar ratingBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie_details);
//    getSupportActionBar().hide();

    ButterKnife.bind(this);

    mMovie = (Movie) getIntent().getSerializableExtra("MOVIE");
    if (mMovie != null) {
      initMovieDetails();
    } else {
      // TODO: Error
    }
  }

  private void initMovieDetails() {
    int posterDefaultWidth = (int) getResources().getDimension(R.dimen.poster_width);
    Picasso.with(this).load(mMovie.getPosterPath()).resize(posterDefaultWidth, 0).into(posterImageView);

    titleTextview.setText(mMovie.getOriginalTitle());
    overviewTextView.setText(mMovie.getOverview());
    releaseDateTextView.setText("Release date: " + mMovie.getReleaseDate());

    if (mMovie.getRating() > 0) {
      ratingBar.setRating(mMovie.getRating());
    }

    if (mMovie.getTrailer() != null) {
      initMovieTrailer();
    }
  }

  private void initMovieTrailer() {
    youtubePlayerView.initialize(Config.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
      @Override
      public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.cueVideo(mMovie.getTrailer().getKey());
      }

      @Override
      public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

      }
    });
  }

  @OnClick(R.id.youtubePlayerView)
  public void playTrailer() {
    Log.d(TAG, "playTrailer");
  }
}

