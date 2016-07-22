package com.codepath.flickster.activity;

import android.os.Bundle;
import android.util.Log;

import com.codepath.flickster.R;
import com.codepath.flickster.models.Trailer;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class MovieQuickPlayActivity extends YouTubeBaseActivity {
  private static final String TAG = MovieQuickPlayActivity.class.getSimpleName();
  private static final String API_KEY = "AIzaSyCg_3G_QXuhUaKXnW4Woc4mAY8hNC6ypwk";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_quick_play);

    final Trailer trailer = (Trailer) getIntent().getSerializableExtra("TRAILER");

    YouTubePlayerView youTubePlayerView =
        (YouTubePlayerView) findViewById(R.id.player);
    youTubePlayerView.initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {
      @Override
      public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
        Log.d(TAG, "onInitializationSuccess");
        youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
          @Override
          public void onLoading() {

          }

          @Override
          public void onLoaded(String s) {
            Log.d(TAG, "------- onLoaded: " + s);
            youTubePlayer.play();
          }

          @Override
          public void onAdStarted() {

          }

          @Override
          public void onVideoStarted() {

          }

          @Override
          public void onVideoEnded() {

          }

          @Override
          public void onError(YouTubePlayer.ErrorReason errorReason) {

          }
        });
        youTubePlayer.cueVideo(trailer.getKey());
      }

      @Override
      public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Log.d(TAG, "onInitializationFailure");

      }
    });
  }
}
