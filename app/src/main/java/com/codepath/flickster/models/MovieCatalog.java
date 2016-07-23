package com.codepath.flickster.models;

import android.util.Log;

import com.codepath.flickster.services.HttpResponse;
import com.codepath.flickster.services.HttpService;
import com.codepath.flickster.util.JSONDeserializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MovieCatalog {
  private static final String TAG = MovieCatalog.class.getSimpleName();
  private static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
  private static final String MOVIE_TRAILER_URL = "http://api.themoviedb.org/3/movie/%s/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

  public interface MovieCatalogHandler<T extends JSONSerializable> {
    void onRequestSuccess(List<T> requestList);
    void onRequestFailure();
  }

  public void getNowPlayingMovieList(final MovieCatalogHandler listener) {
    HttpService httpService = new HttpService(new HttpService.HttpResponseListner() {
      @Override
      public void onRequestFinished(HttpResponse response) {
        if (response != null && response.getStatus() == HttpResponse.Status.Success) {
          try {
            JSONArray jsonArray = getResultList(response.getResponse());
            JSONDeserializer<Movie> deserializer = new JSONDeserializer<>(Movie.class);
            List<Movie> movieList = deserializer.fromJSONArrayToList(jsonArray);
            if (listener != null) {
              listener.onRequestSuccess(movieList);
            }
          } catch (JSONException e) {
            Log.d(TAG, "JSONException: Error encountered when fetching the now playing movie list");
            onRequestFailed(listener);
          }
        } else {
          Log.d(TAG, "Error encountered when fetching the now playing movie list");
          onRequestFailed(listener);
        }
      }
    });

    httpService.getResponse(NOW_PLAYING_URL);
  }

  public void getMovieTrailerList(String movieId, final MovieCatalogHandler listener) {
    String url = String.format(MOVIE_TRAILER_URL, movieId);
    HttpService httpService = new HttpService(new HttpService.HttpResponseListner() {
      @Override
      public void onRequestFinished(HttpResponse response) {
        if (response != null && response.getStatus() == HttpResponse.Status.Success) {
          try {
            JSONArray jsonArray = getResultList(response.getResponse());
            JSONDeserializer<Trailer> deserializer = new JSONDeserializer<>(Trailer.class);
            List<Trailer> trailerList = deserializer.fromJSONArrayToList(jsonArray);
            if (listener != null) {
              listener.onRequestSuccess(trailerList);
            }
          } catch (JSONException e) {
            Log.d(TAG, "JSONException: Error encountered when fetching the movie trailer");
            onRequestFailed(listener);
          }
        } else {
          Log.d(TAG, "Error encountered when fetching the movie trailer");
          onRequestFailed(listener);
        }
      }
    });

    httpService.getResponse(url);
  }

  private JSONArray getResultList(String response) throws JSONException {
    JSONArray jsonResultsArray = null;
    JSONObject jsonResponse = new JSONObject(response);
    if (jsonResponse != null) {
      jsonResultsArray = jsonResponse.getJSONArray("results");
    }

    return jsonResultsArray;
  }

  private void onRequestFailed(MovieCatalogHandler listener) {
    if (listener != null) {
      listener.onRequestFailure();
    }
  }
}
