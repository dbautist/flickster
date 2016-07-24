package com.codepath.flickster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.codepath.flickster.models.Movie;
import com.codepath.flickster.models.MovieCatalog;
import com.codepath.flickster.util.AppConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SplashScreenActivity extends AppCompatActivity {
  private static final String TAG = SplashScreenActivity.class.getSimpleName();
  private List<Movie> movieList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getNowPlayingList();
  }

  private void getNowPlayingList() {
    MovieCatalog movieCatalog = new MovieCatalog();
    movieCatalog.getNowPlayingMovieList(new MovieCatalog.MovieCatalogHandler() {
      @Override
      public void onRequestSuccess(List requestList) {
        Log.d(TAG, "Getting now playing list");
        movieList = requestList;
        startMovieActivity();
      }

      @Override
      public void onRequestFailure() {
        movieList = new ArrayList<Movie>();
        startMovieActivity();
      }
    });
  }

  private void startMovieActivity() {
    Intent intent = new Intent(this, MovieActivity.class);
    intent.putExtra(AppConstants.NOW_PLAYING_INTENT_EXTRA, (Serializable) movieList);
    startActivity(intent);
    finish();
  }
}