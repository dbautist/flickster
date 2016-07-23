package com.codepath.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.flickster.R;
import com.codepath.flickster.models.Movie;
import com.codepath.flickster.util.AppConstants;
import com.codepath.flickster.util.PicassoViewHelper;
import com.squareup.picasso.Callback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MovieArrayAdapter extends ArrayAdapter<Movie> {
  private static final String TAG = MovieArrayAdapter.class.getSimpleName();
  public static final int TYPE_MOVIE = 0;
  public static final int TYPE_POPULAR_MOVIE = 1;

  public MovieArrayAdapter(Context context, ArrayList<Movie> movieList) {
    super(context, 0, movieList);
  }

  // Total number of types is the number of enum values
  @Override
  public int getViewTypeCount() {
    return 2;
  }

  @Override
  public int getItemViewType(int position) {
    Movie movie = getItem(position);
    if (movie.isPopular()) {
      return TYPE_POPULAR_MOVIE;
    } else {
      return TYPE_MOVIE;
    }
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // Get the data item for this position
    int type = getItemViewType(position);

    if (convertView == null) {

      if (type == TYPE_POPULAR_MOVIE) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_popular_movie, null);
        PopularMovieViewHolder popularMovieViewHolder = new PopularMovieViewHolder(convertView);
        convertView.setTag(popularMovieViewHolder);
      } else {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie, null);
        MovieViewHolder movieViewHolder = new MovieViewHolder(convertView);
        convertView.setTag(movieViewHolder);
      }
    }

    final Movie movie = getItem(position);
    String imagePath = movie.getPosterPath();
    float defaultDpWidth = getContext().getResources().getDimension(R.dimen.poster_width);
    int placeHolderDrawable = R.drawable.poster_image_placeholder;
    int orientation = getContext().getResources().getConfiguration().orientation;
    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
      imagePath = movie.getBackdropPath();
      defaultDpWidth = getContext().getResources().getDimension(R.dimen.background_width);
      Log.d(TAG, "ORIENTATION_LANDSCAPE: " + defaultDpWidth);
    } else {
      Log.d(TAG, "ORIENTATION_PORTRAIT: " + defaultDpWidth);
      if (movie.isPopular() ) {
        imagePath = movie.getBackdropPath();
      }
    }
    PicassoViewHelper picassoViewHelper = new PicassoViewHelper(getContext(), imagePath, placeHolderDrawable);

    if (type == TYPE_POPULAR_MOVIE) {
      final PopularMovieViewHolder popularMovieViewHolder = (PopularMovieViewHolder) convertView.getTag();
      popularMovieViewHolder.playImage.setVisibility(View.GONE);
      popularMovieViewHolder.movieImage.setImageResource(0);

      picassoViewHelper.setRoundedCorner(AppConstants.DEFAULT_ROUNDED_CORNER_RADIUS, 0, RoundedCornersTransformation.CornerType.TOP_RIGHT);
      picassoViewHelper.setRoundedCorner(AppConstants.DEFAULT_ROUNDED_CORNER_RADIUS, 0, RoundedCornersTransformation.CornerType.TOP_LEFT);
      picassoViewHelper.getRequestCreator().fit()
          .into(popularMovieViewHolder.movieImage, new Callback() {
            @Override
            public void onSuccess() {
              popularMovieViewHolder.playImage.setVisibility(View.VISIBLE);
              popularMovieViewHolder.titleLayout.setVisibility(View.VISIBLE);
              popularMovieViewHolder.titleText.setText(movie.getOriginalTitle());
            }

            @Override
            public void onError() {
              popularMovieViewHolder.playImage.setVisibility(View.GONE);
              popularMovieViewHolder.titleLayout.setVisibility(View.GONE);
            }
          });
    } else {
      MovieViewHolder movieViewHolder = (MovieViewHolder) convertView.getTag();
      movieViewHolder.titleTextView.setText(movie.getOriginalTitle());
      movieViewHolder.overviewTextView.setText(movie.getOverview());
      if (movie.getVoteAverage() > 0) {
        movieViewHolder.ratingBar.setVisibility(View.VISIBLE);
        movieViewHolder.ratingBar.setRating(movie.getRating());
      } else {
        movieViewHolder.ratingBar.setVisibility(View.GONE);
      }

      movieViewHolder.movieImage.setImageResource(0);
      // Check orientation configuration and change the image accordingly
      picassoViewHelper.setRoundedCorner(AppConstants.DEFAULT_ROUNDED_CORNER_RADIUS, 0, null);
      picassoViewHelper.getRequestCreator()
          .resize((int) defaultDpWidth, 0)
          .into(movieViewHolder.movieImage);
    }

    return convertView;
  }

  public static class MovieViewHolder {
    @BindView(R.id.movieImage)
    ImageView movieImage;
    @BindView(R.id.titleTextView)
    TextView titleTextView;
    @BindView(R.id.overviewTextView)
    TextView overviewTextView;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    public MovieViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }

  public static class PopularMovieViewHolder {
    @BindView(R.id.movieImage)
    ImageView movieImage;
    @BindView(R.id.playImage)
    ImageView playImage;
    @BindView(R.id.titleLayout)
    RelativeLayout titleLayout;
    @BindView(R.id.titleText)
    TextView titleText;

    public PopularMovieViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }
}
