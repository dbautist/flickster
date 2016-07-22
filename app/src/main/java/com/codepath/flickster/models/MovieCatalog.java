package com.codepath.flickster.models;

import android.util.Log;

import com.codepath.flickster.services.HttpResponse;
import com.codepath.flickster.services.HttpService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieCatalog {
  private static final String TAG = MovieCatalog.class.getSimpleName();
  private static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

  public interface MovieCatalogHandler {
    void onRequestSuccess(List<Movie> movieList);
    void onRequestFailure();
  }

  public void getNowPlayingMovieList(final MovieCatalogHandler listener) {
    HttpService httpService = new HttpService(new HttpService.HttpResponseListner() {
      @Override
      public void onRequestFinished(HttpResponse response) {
        if (response != null) {
          if (response.getStatus() == HttpResponse.Status.Success) {
            try {
              JSONObject jsonResponse = new JSONObject(response.getResponse());
              JSONArray movieJsonResults = jsonResponse.getJSONArray("results");
              List<Movie> movieList = new ArrayList<>();
              movieList.addAll(Movie.fromJSONArray(movieJsonResults));
              if (listener != null) {
                listener.onRequestSuccess(movieList);
              }
            } catch (JSONException e) {
              Log.d(TAG, "JSONException encountered: " + e.getMessage());
              onRequestFailed(listener);
            }
          } else {
            Log.d(TAG, "Error encountered when fetching the now playing movie list");
            onRequestFailed(listener);
          }
        } else {
          onRequestFailed(listener);
        }
      }
    });

    httpService.getResponse(NOW_PLAYING_URL);
  }

  private void onRequestFailed(MovieCatalogHandler listener) {
    if (listener != null) {
      listener.onRequestFailure();
    }
  }
}
