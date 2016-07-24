package com.codepath.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MoviesAdapter extends
    RecyclerView.Adapter<MoviesAdapter.ViewHolder> implements View.OnClickListener{
  private static final String TAG = MoviesAdapter.class.getSimpleName();
  public static final int TYPE_MOVIE = 0;
  public static final int TYPE_POPULAR_MOVIE = 1;

  private Context mContext;
  private List<Movie> mMovieList;

  public MoviesAdapter(Context context, ArrayList<Movie> movieList){
    this.mContext = context;
    this.mMovieList = movieList;
  }

  @Override
  public int getItemViewType(int position) {
    Movie movie = mMovieList.get(position);
    if (movie.isPopular()) {
      return TYPE_POPULAR_MOVIE;
    } else {
      return TYPE_MOVIE;
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);

    if (viewType == TYPE_POPULAR_MOVIE) {
      View movieView = inflater.inflate(R.layout.item_popular_movie, parent, false);
      ViewHolder viewHolder = new PopularMovieViewHolder(movieView);
      return viewHolder;
    } else {
      View movieView = inflater.inflate(R.layout.item_movie, parent, false);
      ViewHolder viewHolder = new MovieViewHolder(movieView);
      return viewHolder;
    }
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    final Movie movie = mMovieList.get(position);
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

    int type = getItemViewType(position);
    if (type == TYPE_POPULAR_MOVIE) {
      final PopularMovieViewHolder popularMovieViewHolder = (PopularMovieViewHolder) holder;
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
      MovieViewHolder movieViewHolder = (MovieViewHolder) holder;
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
  }

  @Override
  public int getItemCount() {
    return mMovieList.size();
  }

  private Context getContext() {
    return mContext;
  }

  @Override
  public void onClick(View view) {
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  public static class MovieViewHolder extends ViewHolder{
    @BindView(R.id.movieImage)
    ImageView movieImage;
    @BindView(R.id.titleTextView)
    TextView titleTextView;
    @BindView(R.id.overviewTextView)
    TextView overviewTextView;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    public MovieViewHolder(View view) {
      super(view);
    }
  }

  public static class PopularMovieViewHolder extends ViewHolder{
    @BindView(R.id.movieImage)
    ImageView movieImage;
    @BindView(R.id.playImage)
    ImageView playImage;
    @BindView(R.id.titleLayout)
    RelativeLayout titleLayout;
    @BindView(R.id.titleText)
    TextView titleText;

    public PopularMovieViewHolder(View view) {
      super(view);
    }
  }
}