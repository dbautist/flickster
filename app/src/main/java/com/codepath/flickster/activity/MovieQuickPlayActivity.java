package com.codepath.flickster.activity;

import android.os.Bundle;
import android.util.Log;

import com.codepath.flickster.R;
import com.codepath.flickster.models.Trailer;
import com.codepath.flickster.util.Config;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieQuickPlayActivity extends YouTubeBaseActivity {
  private static final String TAG = MovieQuickPlayActivity.class.getSimpleName();

  @BindView(R.id.player)
  YouTubePlayerView youtubePlayerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_quick_play);

    ButterKnife.bind(this);

    final Trailer trailer = (Trailer) getIntent().getSerializableExtra("TRAILER");
    if (trailer != null) {
      youtubePlayerView.initialize(Config.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
          Log.d(TAG, "onInitializationSuccess");
          youTubePlayer.loadVideo(trailer.getKey());
        }

        @Override
        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
          Log.d(TAG, "onInitializationFailure");

        }
      });
    } else {
      // TODO: error
    }
  }
}
