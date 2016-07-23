package com.codepath.flickster.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.flickster.R;
import com.codepath.flickster.adapters.MovieArrayAdapter;
import com.codepath.flickster.models.Movie;
import com.codepath.flickster.models.MovieCatalog;
import com.codepath.flickster.models.Trailer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieActivity extends AppCompatActivity {
  private static final String TAG = MovieActivity.class.getSimpleName();

  private MovieCatalog movieCatalog;
  private ArrayList<Movie> nowPlayingMovieList;
  private MovieArrayAdapter adapter;

  @BindView(R.id.swipeContainer)
  SwipeRefreshLayout swipeContainer;
  @BindView(R.id.movieListView)
  ListView movieListView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie);
    getSupportActionBar().setTitle("Now Playing");
    ButterKnife.bind(this);

    initSwipeRefreshLayout();
    initMovieList();
    getNowPlayingList();
  }

  private void initMovieList() {
    movieCatalog = new MovieCatalog();
    nowPlayingMovieList = new ArrayList<>();
    adapter = new MovieArrayAdapter(this, nowPlayingMovieList);
    movieListView.setAdapter(adapter);

    movieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Movie movie = adapter.getItem(position);
        if (movie != null) {
          if (movie.isPopular()) {
            getMovieQuickPlay(movie);
          } else {
            gotoMovieDetails(movie);
          }
        }
      }
    });
  }

  private void initSwipeRefreshLayout() {
    swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        getNowPlayingList();
      }
    });

    // Configure the refreshing colors
    swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
        android.R.color.holo_green_light,
        android.R.color.holo_orange_light,
        android.R.color.holo_red_light);

  }

  private void getNowPlayingList() {
    movieCatalog.getNowPlayingMovieList(new MovieCatalog.MovieCatalogHandler() {
      @Override
      public void onRequestSuccess(List requestList) {
        Log.d(TAG, "Getting now playing list");

        List<Movie> movieList = requestList;
        adapter.clear();
        nowPlayingMovieList.addAll(movieList);
        adapter.notifyDataSetChanged();

        if (swipeContainer.isRefreshing()) {
          swipeContainer.setRefreshing(false);
        }
      }

      @Override
      public void onRequestFailure() {
        // TODO: toast message or something...
      }
    });
  }

  private void gotoMovieDetails(Movie movie) {
    Intent intent = new Intent(this, MovieDetailsActivity.class);
    intent.putExtra("MOVIE", movie);
    startActivity(intent);
  }

  private void getMovieQuickPlay(Movie movie) {
    Log.d(TAG, "Fetching movie trailer on listItemClick");

    movieCatalog.getMovieTrailerList(movie.getId(), new MovieCatalog.MovieCatalogHandler() {
      @Override
      public void onRequestSuccess(List requestList) {
        Log.d(TAG, "Getting movie trailer list");

        List<Trailer> trailerList = requestList;
        if (trailerList != null && trailerList.size() > 0) {
          gotoMovieQuickPlay(trailerList.get(0));
        }
      }

      @Override
      public void onRequestFailure() {
        // TODO: toast message or something...
      }
    });
  }

  private void gotoMovieQuickPlay(Trailer trailer) {
    Intent intent = new Intent(this, MovieQuickPlayActivity.class);
    intent.putExtra("TRAILER", trailer);
    startActivity(intent);
  }
}
