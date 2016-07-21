package com.codepath.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.flickster.R;
import com.codepath.flickster.models.Movie;
import com.codepath.flickster.util.DeviceDimensionsHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieArrayAdapter extends ArrayAdapter<Movie> {
  private static final String TAG = MovieArrayAdapter.class.getSimpleName();
  public static final int TYPE_MOVIE = 0;
  public static final int TYPE_POPULAR_MOVIE = 1;

  public MovieArrayAdapter(Context context, ArrayList<Movie> movieList) {
    super(context, R.layout.item_movie, movieList);
  }

  @Override
  public int getItemViewType(int position) {
    Movie movie = getItem(position);
    if (movie.getVoteAverage() >= 5) {
      return TYPE_POPULAR_MOVIE;
    } else {
      return TYPE_MOVIE;
    }
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // Get the data item for this position
    Movie movie = getItem(position);
    ViewHolder viewHolder;

    Log.d(TAG, "======== getView position: " + position + " voteAverage: " + movie.getVoteAverage());
    if (convertView == null) {
      convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie, parent, false);
      viewHolder = new ViewHolder(convertView);
      convertView.setTag(viewHolder);
    }
    viewHolder = (ViewHolder) convertView.getTag();

    viewHolder.movieImage.setImageResource(0);
    // Check orientation configuration and change the image accordingly
    String imagePath = movie.getPosterPath();
    int defaultDpWidth = 120;
    int orientation = getContext().getResources().getConfiguration().orientation;
    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
      imagePath = movie.getBackdropPath();
      defaultDpWidth = 400;
    }

    int pxWidth = (int) DeviceDimensionsHelper.convertDpToPixel(defaultDpWidth, getContext());
    Log.d(TAG, "====== defaultDpWidth " + defaultDpWidth + ", pxWidth " + pxWidth);
    Picasso.with(getContext()).load(imagePath).resize(pxWidth, 0).placeholder(R.drawable.movie_image_placeholder).into(viewHolder.movieImage);

    viewHolder.titleText.setText(movie.getOriginalTitle());
    viewHolder.overviewText.setText(movie.getOverview());

    return convertView;
  }

  static class ViewHolder {
    @BindView(R.id.movieImage)
    ImageView movieImage;
    @BindView(R.id.titleTextView)
    TextView titleText;
    @BindView(R.id.overviewTextView)
    TextView overviewText;

    public ViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }
}
