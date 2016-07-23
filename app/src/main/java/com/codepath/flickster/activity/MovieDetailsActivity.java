package com.codepath.flickster.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.flickster.R;
import com.codepath.flickster.databinding.ActivityMovieDetailsBinding;
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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
//    getSupportActionBar().hide();

    ActivityMovieDetailsBinding binding = DataBindingUtil.setContentView(
        this, R.layout.activity_movie_details);
    ButterKnife.bind(this);

    mMovie = (Movie) getIntent().getSerializableExtra("MOVIE");
    if (mMovie != null) {
      binding.setMovie(mMovie);
      initMovieDetails();
    } else {
      // TODO: Error
    }
  }

  private void initMovieDetails() {
    int posterDefaultWidth = (int) getResources().getDimension(R.dimen.poster_width);
    Picasso.with(this).load(mMovie.getPosterPath()).resize(posterDefaultWidth, 0).into(posterImageView);

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

