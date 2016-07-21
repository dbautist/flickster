package com.codepath.flickster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.codepath.flickster.adapters.MovieArrayAdapter;
import com.codepath.flickster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MovieActivity extends AppCompatActivity {
  private static final String TAG = MovieActivity.class.getSimpleName();

  private ArrayList<Movie> movieList = new ArrayList<>();
  private MovieArrayAdapter adapter;

  @BindView(R.id.movieListView)
  ListView movieListView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie);

    ButterKnife.bind(this);

    adapter = new MovieArrayAdapter(this, movieList);
    movieListView.setAdapter(adapter);

    connect();
  }

  private void connect() {
    String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    AsyncHttpClient client = new AsyncHttpClient();
    client.get(url, new JsonHttpResponseHandler(){
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        JSONArray movieJsonResults = null;

        try {
          movieJsonResults = response.getJSONArray("results");
          movieList.addAll(Movie.fromJSONArray(movieJsonResults));
          adapter.notifyDataSetChanged();
          Log.d(TAG, "========= movieJsonResults" + movieJsonResults.toString());

        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.d(TAG, "onFailure -- " + responseString);
      }
    });
  }
}
