package com.tradehousemedia.tradecore.java.examples;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import com.tradehousemedia.tradecore.core.TradecoreAd;
import com.tradehousemedia.tradecore.core.TradecoreError;
import com.tradehousemedia.tradecore.core.listeners.TradecoreInstreamAdListener;
import com.tradehousemedia.tradecore.ima.TradecoreInstreamAdUnit;
import com.tradehousemedia.tradecore.java.R;
import com.tradehousemedia.tradecore.java.utils.BaseExampleActivity;
import org.jetbrains.annotations.NotNull;

public class TradecoreInstreamMedia3Activity extends BaseExampleActivity {

    private static final String SAMPLE_VIDEO_URL = "https://storage.googleapis.com/gvabox/media/samples/stock.mp4";
    private int savedPosition = 0;

    private VideoView videoPlayer;
    private MediaController mediaController;
    private TradecoreInstreamAdUnit tradecoreInstreamAdUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instream);

        videoPlayer = findViewById(R.id.videoView);
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoPlayer);
        videoPlayer.setMediaController(mediaController);

        findViewById(R.id.playButton).setOnClickListener(this::loadAd);
    }

    private void loadAd(View playButton) {
        ViewGroup container = findViewById(R.id.videoPlayerContainer);

        tradecoreInstreamAdUnit = new TradecoreInstreamAdUnit("tradecore-zone-3027", this);
        tradecoreInstreamAdUnit.setVideoPlayer(videoPlayer, container);
        tradecoreInstreamAdUnit.setAdListener(createAdListener());
        tradecoreInstreamAdUnit.loadAd();

        playButton.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tradecoreInstreamAdUnit != null)
            tradecoreInstreamAdUnit.destroy();
    }

    private TradecoreInstreamAdListener createAdListener() {
        return new TradecoreInstreamAdListener() {
            @Override
            public void onAdLoaded(@NonNull TradecoreAd ad) {
                // Ad loaded
                Log.d("ExampleActivity", "Ad loaded for zone " + ad.getZoneId() + " with targeting: " + ad.getTargeting());
                if (tradecoreInstreamAdUnit != null) {
                    tradecoreInstreamAdUnit.show();
                }
            }

            @Override
            public void onAdFailed(@NonNull TradecoreError error) {
                // Ad failed to load
            }

            @Override
            public void shouldPauseContent() {
                // Ad is going to start, the content player should be paused
                savedPosition = videoPlayer.getCurrentPosition();
                videoPlayer.stopPlayback();
                videoPlayer.setMediaController(null);
            }

            @Override
            public void shouldResumeContent() {
                // Ad is completed, the content player should be resumed
                videoPlayer.setVideoPath(SAMPLE_VIDEO_URL);
                videoPlayer.setMediaController(mediaController);
                videoPlayer.setOnPreparedListener(mp -> {
                    if (savedPosition > 0) {
                        mp.seekTo(savedPosition);
                    }
                    mp.start();
                });
            }


            @Override
            public void onAdFinished(@NotNull TradecoreAd tradecoreAd) {
                // Ad finished playing
            }

            @Override
            public void onAdStarted(@NotNull TradecoreAd tradecoreAd) {
                // Ad started playing
            }

            @Override
            public void onAdClicked(@NotNull TradecoreAd tradecoreAd) {
                // Ad clicked
            }
        };
    }
}