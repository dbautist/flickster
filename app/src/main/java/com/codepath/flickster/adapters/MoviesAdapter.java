package com.codepath.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
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
import com.codepath.flickster.databinding.ItemMovieBinding;
import com.codepath.flickster.databinding.ItemPopularMovieBinding;
import com.codepath.flickster.models.Movie;
import com.codepath.flickster.util.AppConstants;
import com.codepath.flickster.util.PicassoViewHelper;
import com.squareup.picasso.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/*
    Databinding Sources:
      https://realm.io/news/data-binding-android-boyar-mount/
      http://mutualmobile.com/posts/using-data-binding-api-in-recyclerview
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
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
    ViewHolder viewHolder;
    if (viewType == TYPE_POPULAR_MOVIE) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.item_popular_movie, parent, false);
      viewHolder = new PopularMovieViewHolder(view);
    } else {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.item_movie, parent, false);
      viewHolder = new MovieViewHolder(view);
    }

    return viewHolder;
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
      popularMovieViewHolder.bindTo(movie);

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
      movieViewHolder.bindTo(movie);

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

  public static class ViewHolder extends RecyclerView.ViewHolder {

    protected ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  public static class MovieViewHolder extends ViewHolder{
    private ItemMovieBinding mBinding;

    @BindView(R.id.movieImage)
    ImageView movieImage;
    @BindView(R.id.titleTextView)
    TextView titleTextView;
    @BindView(R.id.overviewTextView)
    TextView overviewTextView;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    private MovieViewHolder(View view) {
      super(view);
      this.mBinding = DataBindingUtil.bind(view);
    }

    public void bindTo(Movie movie) {
      mBinding.setMovie(movie);
      mBinding.executePendingBindings();
    }
  }

  public static class PopularMovieViewHolder extends ViewHolder{
    private ItemPopularMovieBinding mBinding;

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
      mBinding = DataBindingUtil.bind(view);
    }

    public void bindTo(Movie movie) {
      mBinding.setMovie(movie);
      mBinding.executePendingBindings();
    }
  }
}