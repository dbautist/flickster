package com.codepath.flickster.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Movie implements Serializable {
  private static final String TAG = Movie.class.getSimpleName();
  private static final String BASE_URL = "https://image.tmdb.org/t/p/";
  private static final int POPULAR_VOTE_THRESHOLD = 5;

  private String id;
  private String posterPath;
  private String originalTitle;
  private String overview;
  private String backdropPath;
  private double voteAverage;
  private boolean isPopular;
  private String releaseDate;
  private float rating;
  private Trailer trailer;

  public Movie(JSONObject jsonObject) throws JSONException {
    this.id = jsonObject.getString("id");
    this.posterPath = jsonObject.getString("poster_path");
    this.originalTitle = jsonObject.getString("original_title");
    this.overview = jsonObject.getString("overview");
    this.backdropPath = jsonObject.getString("backdrop_path");
    this.voteAverage = jsonObject.getDouble("vote_average");
    this.isPopular = (this.voteAverage >= POPULAR_VOTE_THRESHOLD) ? true : false;
    this.releaseDate = jsonObject.getString("release_date");

    if (voteAverage >= POPULAR_VOTE_THRESHOLD) {
      rating = POPULAR_VOTE_THRESHOLD;
    } else {
      rating = (float) voteAverage;
    }
  }

  public static ArrayList<Movie> fromJSONArray(JSONArray array) {
    ArrayList<Movie> movieList = new ArrayList<>();
    for (int i = 0; i < array.length(); i++) {
      try {
        Movie movie = new Movie(array.getJSONObject(i));
        movieList.add(movie);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }

    return movieList;
  }

  public String getId() {
    return id;
  }

  public String getOverview() {
    return overview;
  }

  public String getPosterPath() {
    return getFullImagePath(posterPath, "w500");
  }

  public String getOriginalTitle() {
    return originalTitle;
  }

  public String getBackdropPath() {
    return getFullImagePath(backdropPath, "w1280");
  }

  public double getVoteAverage() {
    return voteAverage;
  }

  public String getReleaseDate() {
    return releaseDate;
  }

  public boolean isPopular() {
    return isPopular;
  }

  public float getRating() {
    return rating;
  }

  public void setTrailer(Trailer trailer) {
    this.trailer = trailer;
  }

  public Trailer getTrailer() {
    return trailer;
  }

  private String getFullImagePath(String imagePath, String widthDimen) {
    String path = String.format("%s%s%s", BASE_URL, widthDimen, imagePath);
    Log.d(TAG, "Image path: " + path);
    return path;
  }
}
