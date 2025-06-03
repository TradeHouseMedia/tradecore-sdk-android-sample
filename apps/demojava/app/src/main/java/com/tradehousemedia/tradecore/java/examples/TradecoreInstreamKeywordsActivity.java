package com.tradehousemedia.tradecore.java.examples;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.net.UriKt;
import com.google.ads.interactivemedia.v3.api.*;
import com.google.ads.interactivemedia.v3.api.player.AdMediaInfo;
import com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer;
import com.google.ads.interactivemedia.v3.api.player.VideoProgressUpdate;
import com.tradehousemedia.tradecore.core.TradecoreAd;
import com.tradehousemedia.tradecore.core.TradecoreError;
import com.tradehousemedia.tradecore.core.listeners.TradecoreInstreamAdListener;
import com.tradehousemedia.tradecore.core.utils.TradecoreImaUtils;
import com.tradehousemedia.tradecore.ima.TradecoreInstreamVideo;
import com.tradehousemedia.tradecore.java.R;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Default example with IMA SDK and Tradecore.
 * <a href="https://developers.google.com/interactive-media-ads/docs/sdks/android/client-side/get-started">Official guide</a>
 */
public class TradecoreInstreamKeywordsActivity extends AppCompatActivity {

    private static final String LOGTAG = "InstreamActivity";
    private static final String SAMPLE_VIDEO_URL = "https://storage.googleapis.com/gvabox/media/samples/stock.mp4";
    private static final String SAMPLE_VAST_TAG_URL =
            "https://pubads.g.doubleclick.net/gampad/ads?iu=/21775744923/external/"
                    + "single_preroll_skippable&sz=640x480&ciu_szs=300x250%2C728x90&gdfp_req=1&output=vast"
                    + "&unviewed_position_start=1&env=vp&impl=s&correlator=";

    private int savedPosition = 0;

    private ImaSdkFactory sdkFactory;
    private AdsLoader adsLoader;
    private AdsManager adsManager;
    private VideoView videoPlayer;
    private MediaController mediaController;
    private GamAdapter videoAdPlayerAdapter;
    private TradecoreInstreamVideo adUnit;

    private ImaSdkSettings imaSdkSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instream);

        imaSdkSettings = ImaSdkFactory.getInstance().createImaSdkSettings();

        sdkFactory = ImaSdkFactory.getInstance();
        sdkFactory.initialize(this, imaSdkSettings);

        mediaController = new MediaController(this);
        videoPlayer = findViewById(R.id.videoView);
        mediaController.setAnchorView(videoPlayer);
        videoPlayer.setMediaController(mediaController);

        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        videoAdPlayerAdapter = new GamAdapter(videoPlayer, audioManager);

        AdDisplayContainer adDisplayContainer = ImaSdkFactory.createAdDisplayContainer(
                findViewById(R.id.videoPlayerContainer),
                videoAdPlayerAdapter
        );
        adsLoader = sdkFactory.createAdsLoader(this, imaSdkSettings, adDisplayContainer);
        adsLoader.addAdErrorListener(createAdErrorListener());
        adsLoader.addAdsLoadedListener(createAdLoadedListener());

        View playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(view -> {
            videoPlayer.setVideoPath(SAMPLE_VIDEO_URL);
            requestAds();
            view.setVisibility(View.GONE);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adUnit != null) {
            adUnit.destroy();
        }
    }

    private AdErrorEvent.AdErrorListener createAdErrorListener() {
        return adErrorEvent -> {
            Log.i(LOGTAG, "Ad Error: " + (adErrorEvent != null && adErrorEvent.getError() != null ? adErrorEvent.getError().getMessage() : "Unknown error"));
            resumeContent();
        };
    }

    private AdsLoader.AdsLoadedListener createAdLoadedListener() {
        return adsManagerLoadedEvent -> {
            adsManager = adsManagerLoadedEvent.getAdsManager();
            adsManager.addAdErrorListener(adErrorEvent -> {
                Log.e(LOGTAG, "Ad Error: " + (adErrorEvent != null && adErrorEvent.getError() != null ? adErrorEvent.getError().getMessage() : "Unknown error"));
                String universalAdIds = adsManager.getCurrentAd() != null ?
                        java.util.Arrays.toString(adsManager.getCurrentAd().getUniversalAdIds()) : "null";
                Log.i(LOGTAG, "Discarding the current ad break with universal ad Ids: " + universalAdIds);
                adsManager.discardAdBreak();
            });
            adsManager.addAdEventListener(adEvent -> {
                if (adEvent.getType() != AdEvent.AdEventType.AD_PROGRESS) {
                    Log.i(LOGTAG, "Event: " + adEvent.getType());
                }
                switch (adEvent.getType()) {
                    case LOADED:
                        adsManager.start();
                        break;
                    case CONTENT_PAUSE_REQUESTED:
                        pauseContentForAds();
                        break;
                    case CONTENT_RESUME_REQUESTED:
                        resumeContent();
                        break;
                    case ALL_ADS_COMPLETED:
                        adsManager.destroy();
                        break;
                    case CLICKED:
                        break;
                    default:
                        break;
                }
            });
            adsManager.init(ImaSdkFactory.getInstance().createAdsRenderingSettings());
        };
    }

    private void pauseContentForAds() {
        Log.i(LOGTAG, "pauseContentForAds");
        savedPosition = videoPlayer.getCurrentPosition();
        videoPlayer.stopPlayback();
        videoPlayer.setMediaController(null);
    }

    private void resumeContent() {
        Log.i(LOGTAG, "resumeContent");
        videoPlayer.setVideoPath(SAMPLE_VIDEO_URL);
        videoPlayer.setMediaController(mediaController);
        videoPlayer.setOnPreparedListener(mediaPlayer -> {
            if (savedPosition > 0) {
                mediaPlayer.seekTo(savedPosition);
            }
            mediaPlayer.start();
        });
        videoPlayer.setOnCompletionListener(mediaPlayer -> {
            if (videoAdPlayerAdapter != null) {
                videoAdPlayerAdapter.notifyImaOnContentCompleted();
            }
        });
    }

    private void requestAds() {
        TradecoreInstreamVideo instreamVideo = new TradecoreInstreamVideo("tradecore-zone-3027", this);
        instreamVideo.setAdListener(createTradecoreListener());
        instreamVideo.loadAd();

        this.adUnit = instreamVideo;
    }

    public TradecoreInstreamAdListener createTradecoreListener() {
        return new TradecoreInstreamAdListener() {
            @Override
            public void onAdLoaded(@NotNull TradecoreAd tradecoreAd) {
                AdsRequest request = sdkFactory.createAdsRequest();

                String encodedKeywords = TradecoreImaUtils.encodeKeywords(tradecoreAd.getTargeting());
                String url = SAMPLE_VAST_TAG_URL + "&cust_params=" + encodedKeywords;

                request.setAdTagUrl(url);

                request.setContentProgressProvider(() -> {
                    if (videoPlayer.getDuration() <= 0) {
                        return VideoProgressUpdate.VIDEO_TIME_NOT_READY;
                    }
                    return new VideoProgressUpdate(
                            videoPlayer.getCurrentPosition(),
                            videoPlayer.getDuration()
                    );
                });
                adsLoader.requestAds(request);
            }

            @Override
            public void onAdFailed(@NotNull TradecoreError tradecoreError) {
                resumeContent();
            }


            @Override
            public void onAdClicked(TradecoreAd ad) {
            }

            @Override
            public void shouldResumeContent() {
            }

            @Override
            public void shouldPauseContent() {
            }

            @Override
            public void onAdFinished(@NotNull TradecoreAd tradecoreAd) {
            }

            @Override
            public void onAdStarted(@NotNull TradecoreAd tradecoreAd) {
            }
        };
    }


    public static class GamAdapter implements VideoAdPlayer {

        private final VideoView videoPlayer;
        private final AudioManager audioManager;
        private final List<VideoAdPlayerCallback> videoAdPlayerCallbacks = new ArrayList<>();
        private Timer timer = null;
        private int adDuration = 0;

        private int savedAdPosition = 0;
        private AdMediaInfo loadedAdMediaInfo = null;

        public GamAdapter(VideoView videoPlayer, AudioManager audioManager) {
            this.videoPlayer = videoPlayer;
            this.audioManager = audioManager;

            this.videoPlayer.setOnCompletionListener(mp -> notifyImaOnContentCompleted());
        }

        @Override
        public void addCallback(VideoAdPlayerCallback videoAdPlayerCallback) {
            videoAdPlayerCallbacks.add(videoAdPlayerCallback);
        }

        @Override
        public void loadAd(AdMediaInfo adMediaInfo, AdPodInfo adPodInfo) {
            loadedAdMediaInfo = adMediaInfo;
        }

        @Override
        public void pauseAd(AdMediaInfo adMediaInfo) {
            Log.i(LOGTAG, "pauseAd");
            savedAdPosition = videoPlayer.getCurrentPosition();
            stopAdTracking();
        }

        @Override
        public void playAd(AdMediaInfo adMediaInfo) {
            videoPlayer.setVideoURI(UriKt.toUri(adMediaInfo.getUrl()));

            videoPlayer.setOnPreparedListener(mediaPlayer -> {
                adDuration = mediaPlayer.getDuration();
                if (savedAdPosition > 0) {
                    mediaPlayer.seekTo(savedAdPosition);
                }
                mediaPlayer.start();
                startAdTracking();
            });

            videoPlayer.setOnErrorListener((mediaPlayer, errorType, extra) -> notifyImaSdkAboutAdError(errorType));

            videoPlayer.setOnCompletionListener(mediaPlayer -> {
                savedAdPosition = 0;
                notifyImaSdkAboutAdEnded();
            });
        }

        @Override
        public void release() {
        }

        @Override
        public void removeCallback(VideoAdPlayerCallback videoAdPlayerCallback) {
            videoAdPlayerCallbacks.remove(videoAdPlayerCallback);
        }

        @Override
        public void stopAd(AdMediaInfo adMediaInfo) {
            Log.i(LOGTAG, "stopAd");
            stopAdTracking();
        }

        @Override
        public int getVolume() {
            int current = audioManager != null ? audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) : 3;
            int max = audioManager != null ? audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) : 10;
            return max == 0 ? 1 : (current / max); // чтобы не делить на 0
        }

        private void startAdTracking() {
            Log.i(LOGTAG, "startAdTracking");
            if (timer != null) {
                return;
            }
            timer = new Timer();
            TimerTask updateTimerTask = new TimerTask() {
                @Override
                public void run() {
                    VideoProgressUpdate progressUpdate = getAdProgress();
                    notifyImaSdkAboutAdProgress(progressUpdate);
                }
            };
            timer.schedule(updateTimerTask, POLLING_TIME_MS, INITIAL_DELAY_MS);
        }

        private void notifyImaSdkAboutAdEnded() {
            Log.i(LOGTAG, "notifyImaSdkAboutAdEnded");
            savedAdPosition = 0;
            if (loadedAdMediaInfo != null) {
                for (VideoAdPlayerCallback callback : videoAdPlayerCallbacks) {
                    callback.onEnded(loadedAdMediaInfo);
                }
            }
        }

        private void notifyImaSdkAboutAdProgress(VideoProgressUpdate adProgress) {
            if (loadedAdMediaInfo != null) {
                for (VideoAdPlayerCallback callback : videoAdPlayerCallbacks) {
                    callback.onAdProgress(loadedAdMediaInfo, adProgress);
                }
            }
        }

        /**
         * @return boolean
         */
        private boolean notifyImaSdkAboutAdError(int errorType) {
            Log.i(LOGTAG, "notifyImaSdkAboutAdError");

            if (errorType == MediaPlayer.MEDIA_ERROR_UNSUPPORTED) {
                Log.e(LOGTAG, "notifyImaSdkAboutAdError: MEDIA_ERROR_UNSUPPORTED");
            } else if (errorType == MediaPlayer.MEDIA_ERROR_TIMED_OUT) {
                Log.e(LOGTAG, "notifyImaSdkAboutAdError: MEDIA_ERROR_TIMED_OUT");
            }
            if (loadedAdMediaInfo != null) {
                for (VideoAdPlayerCallback callback : videoAdPlayerCallbacks) {
                    callback.onError(loadedAdMediaInfo);
                }
            }
            return true;
        }

        public void notifyImaOnContentCompleted() {
            Log.i(LOGTAG, "notifyImaOnContentCompleted");
            for (VideoAdPlayerCallback callback : videoAdPlayerCallbacks) {
                callback.onContentComplete();
            }
        }

        private void stopAdTracking() {
            Log.i(LOGTAG, "stopAdTracking");
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        }

        @Override
        public VideoProgressUpdate getAdProgress() {
            long adPosition = videoPlayer.getCurrentPosition();
            return new VideoProgressUpdate(adPosition, adDuration);
        }

        private static final String LOGTAG = "TradecoreInstreamAdapter";
        private static final long POLLING_TIME_MS = 250;
        private static final long INITIAL_DELAY_MS = 250;
    }


}