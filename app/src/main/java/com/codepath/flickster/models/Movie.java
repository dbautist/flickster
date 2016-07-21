package com.codepath.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Movie {
  private String posterPath;
  private String originalTitle;
  private String overview;
  private String backdropPath;
  private double voteAverage;

  public Movie(JSONObject jsonObject) throws JSONException {
    this.posterPath = jsonObject.getString("poster_path");
    this.originalTitle = jsonObject.getString("original_title");
    this.overview = jsonObject.getString("overview");
    this.backdropPath = jsonObject.getString("backdrop_path");
    this.voteAverage = jsonObject.getDouble("vote_average");
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

  public String getOverview() {
    return overview;
  }

  public String getPosterPath() {
    return String.format("https://image.tmdb.org/t/p/w500/%s", posterPath);
  }

  public String getOriginalTitle() {
    return originalTitle;
  }

  public String getBackdropPath() {
    return String.format("https://image.tmdb.org/t/p/w1280/%s", backdropPath);
  }

  public double getVoteAverage() {
    return voteAverage;
  }
}
