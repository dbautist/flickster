package com.codepath.flickster.models;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class Movie implements JSONSerializable, Serializable {
  private static final String TAG = Movie.class.getSimpleName();
  private static final String BASE_URL = "https://image.tmdb.org/t/p/";
  private static final int POPULAR_VOTE_THRESHOLD = 5;

  private String id;
  private String posterPath;
  private String originalTitle;
  private String overview;
  private String backdropPath;
  private double voteAverage;
  private double popularity;
  private boolean isPopular;
  private String releaseDate;
  private float rating;
  private Trailer trailer;

  @Override
  public void configureFromJSON(JSONObject jsonObject) throws JSONException {
    this.id = jsonObject.getString("id");
    this.posterPath = jsonObject.getString("poster_path");
    this.originalTitle = jsonObject.getString("original_title");
    this.overview = jsonObject.getString("overview");
    this.backdropPath = jsonObject.getString("backdrop_path");
    this.voteAverage = jsonObject.getDouble("vote_average");
    this.popularity = jsonObject.getDouble("popularity");
    this.releaseDate = jsonObject.getString("release_date");

    if (voteAverage >= POPULAR_VOTE_THRESHOLD) {
      rating = POPULAR_VOTE_THRESHOLD;
    } else {
      rating = (float) voteAverage;
    }

    this.isPopular = (this.voteAverage >= POPULAR_VOTE_THRESHOLD) ? true : false;
    requestTrailer();
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
    return "Release date: " + releaseDate;
  }

  public boolean isPopular() {
    return isPopular;
  }

  public float getRating() {
    return rating;
  }

  public void requestTrailer() {
    MovieCatalog movieCatalog = new MovieCatalog();
    movieCatalog.getMovieTrailerList(id, new MovieCatalog.MovieCatalogHandler() {
      @Override
      public void onRequestSuccess(List requestList) {
        Log.d(TAG, "Getting movie trailer list");

        List<Trailer> trailerList = requestList;
        if (trailerList != null && trailerList.size() > 0) {
          for (Trailer trailer : trailerList) {
            // just grab the first with site=YouTube
            if (trailer.getSite() != null && trailer.getSite().compareToIgnoreCase("YouTube") == 0){
              setTrailer(trailer);
              return;
            }
          }
        }
      }

      @Override
      public void onRequestFailure() {
        // TODO: toast message or something...
      }
    });
  }

  private void setTrailer(Trailer trailer) {
    this.trailer = trailer;
  }

  public Trailer getTrailer() {
    return trailer;
  }

  public String getPopularity() {
    return String.format("Popularity: %.2f%%", popularity);
  }

  private String getFullImagePath(String imagePath, String widthDimen) {
    String path = String.format("%s%s%s", BASE_URL, widthDimen, imagePath);
    Log.d(TAG, "Image path: " + path);
    return path;
  }
}
