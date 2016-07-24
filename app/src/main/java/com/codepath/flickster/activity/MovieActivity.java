package com.codepath.flickster.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.codepath.flickster.R;
import com.codepath.flickster.adapters.MoviesAdapter;
import com.codepath.flickster.models.Movie;
import com.codepath.flickster.models.MovieCatalog;
import com.codepath.flickster.models.Trailer;
import com.codepath.flickster.util.DividerItemDecoration;
import com.codepath.flickster.util.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieActivity extends AppCompatActivity {
  private static final String TAG = MovieActivity.class.getSimpleName();

  private MovieCatalog movieCatalog;
  private ArrayList<Movie> nowPlayingMovieList;
  private MoviesAdapter adapter;

  @BindView(R.id.swipeContainer)
  SwipeRefreshLayout swipeContainer;
  @BindView(R.id.movieRecyclerView)
  RecyclerView movieRecyclerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie);
    getSupportActionBar().setTitle("Now Playing");
    ButterKnife.bind(this);

    initSwipeRefreshLayout();

    if (savedInstanceState != null ) {
      Log.d(TAG, "Restoring instance: movie list already initialized");
      ArrayList<Movie> movieList = (ArrayList<Movie>) savedInstanceState.getSerializable("NOW_PLAYING_MOVIE_LIST");
      initMovieList(movieList);
    } else {
      Log.d(TAG, "---------- onCreate: ---------- ");
      initMovieList(new ArrayList<Movie>());
      getNowPlayingList();
    }

  }

  // Save the movie list to avoid fetching from the server every time the configuration changes (e.g. phone rotation)
  // http://guides.codepath.com/android/Handling-Configuration-Changes#saving-and-restoring-activity-state
  public void onSaveInstanceState(Bundle savedState) {
    super.onSaveInstanceState(savedState);

    Log.d(TAG, "Saving state: movie list");
    savedState.putSerializable("NOW_PLAYING_MOVIE_LIST", nowPlayingMovieList);
  }

  private void initMovieList(ArrayList<Movie> movieList) {
    this.nowPlayingMovieList = movieList;
    movieCatalog = new MovieCatalog();
    adapter = new MoviesAdapter(this, movieList);
    movieRecyclerView.setAdapter(adapter);
    movieRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    RecyclerView.ItemDecoration itemDecoration = new
        DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
    movieRecyclerView.addItemDecoration(itemDecoration);

    ItemClickSupport.addTo(movieRecyclerView).setOnItemClickListener(
        new ItemClickSupport.OnItemClickListener() {
          @Override
          public void onItemClicked(RecyclerView recyclerView, int position, View v) {
            Movie movie = nowPlayingMovieList.get(position);
            if (movie != null) {
              if (movie.isPopular()) {
                getMovieQuickPlay(movie);
              } else {
                gotoMovieDetails(movie);
              }
            }
          }
        }
    );
  }

  private void initSwipeRefreshLayout() {
    swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        getNowPlayingList();
      }
    });

    // Configure the refreshing colors
    swipeContainer.setColorSchemeResources(R.color.primary_dark,
        R.color.primary,
        R.color.primary_light,
        R.color.accent);

  }

  private void getNowPlayingList() {
    movieCatalog.getNowPlayingMovieList(new MovieCatalog.MovieCatalogHandler() {
      @Override
      public void onRequestSuccess(List requestList) {
        Log.d(TAG, "Getting now playing list");

        List<Movie> movieList = requestList;
        nowPlayingMovieList.clear();
        nowPlayingMovieList.addAll(movieList);
        adapter.notifyItemRangeChanged(0, nowPlayingMovieList.size());

        if (swipeContainer.isRefreshing()) {
          swipeContainer.setRefreshing(false);
        }
      }

      @Override
      public void onRequestFailure() {
        // TODO: toast message or something...
        if (swipeContainer.isRefreshing()) {
          swipeContainer.setRefreshing(false);
        }
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
