# Tradecore Android SDK - Instream Integration Guide

There are two primary scenarios available for integrating **Instream** within the Tradecore SDK:

- Instream Video Rendering.
- Instream Video Bidding Only.

## Instream Video Bidding Only

In bidding-only mode, the Tradecore SDK handles the bidding process and returns the relevant targeting keywords.

To integrate Instream in this mode, create an instance of the `TradecoreInstreamAdUnit` class, configure it with
respective zone ID, listeners, and call its `load()` method.

**Kotlin:**

```kotlin
    private fun requestAds() {
    val adUnit = TradecoreInstreamAdUnit(ZONE_ID, this)
    adUnit.setAdListener(createTradecoreListener())
    adUnit.loadAd()
}
```

**Java:**

```java
    private void requestAds() {
    TradecoreInstreamAdUnit adUnit = new TradecoreInstreamAdUnit(ZONE_ID, this);
    adUnit.setAdListener(createTradecoreListener());
    adUnit.loadAd();
}
```

- `ZONE_ID` is the id used to apply the configuration for the current ad unit.

Use `TradecoreInstreamAdListener` to respond to the loading events and inject the instream ad into any custom player (
custom `showAd()` method).
You can access the received targeting keywords by using [`TradecoreAd`](./tradecore-sdk-ad-metadata.md) parameter.

**Kotlin:**

```kotlin
    fun createTradecoreListener(): TradecoreInstreamAdListener {
    return object : TradecoreInstreamAdListener {
        override fun onAdLoaded(tradecoreAd: TradecoreAd) {
            val encodedKeywords = TradecoreImaUtils.encodeKeywords(tradecoreAd.targeting)
            val url = SAMPLE_VAST_TAG_URL + "&cust_params=" + encodedKeywords
            showAd(url)
        }

        override fun onAdFailed(tradecoreError: TradecoreError) {
            resumeContent()
        }
    }
}
```

**Java:**

```java
    public TradecoreInstreamAdListener createTradecoreListener() {
    return new TradecoreInstreamAdListener() {
        @Override
        public void onAdLoaded(@NotNull TradecoreAd tradecoreAd) {
            String encodedKeywords = TradecoreImaUtils.encodeKeywords(tradecoreAd.getTargeting());
            String url = SAMPLE_VAST_TAG_URL + "&cust_params=" + encodedKeywords;
            showAd(url);
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
```

## Instream Video Rendering

In rendering mode, Tradecore SDK manages Google Interactive Media Ads SDK internally.

To integrate Instream in this mode, instantiate the `TradecoreInstreamAdUnit` class and configure it with the
appropriate zone ID and interface. Additionally, set the corresponding player, container view, and view controller and
then invoke the `load()` method.

**Kotlin:**

```kotlin
private var tradecoreInstreamAdUnit: TradecoreInstreamAdUnit? = null

private fun loadAd(playButton: View) {
    tradecoreInstreamAdUnit = TradecoreInstreamAdUnit(ZONE_ID, context)
    tradecoreInstreamAdUnit?.setVideoPlayer(videoPlayer, container)
    tradecoreInstreamAdUnit?.setAdListener(createAdListener())
    tradecoreInstreamAdUnit?.loadAd()
}
```

**Java:**

```java
    private TradecoreInstreamAdUnit tradecoreInstreamAdUnit;

private void loadAd(View playButton) {
    tradecoreInstreamAdUnit = new TradecoreInstreamAdUnit("tradecore-zone-3027", this);
    tradecoreInstreamAdUnit.setVideoPlayer(videoPlayer, container);
    tradecoreInstreamAdUnit.setAdListener(createAdListener());
    tradecoreInstreamAdUnit.loadAd();
}
```

Create `TradecoreInstreamAdListener`. It should pause and resume the original player.
Ensure to call the `show()` method in order to start ad playback once the ad is loaded.

**Kotlin:**

```kotlin
private fun createAdListener(): TradecoreInstreamAdListener {
    return object : TradecoreInstreamAdListener {
        override fun onAdLoaded(ad: TradecoreAd) {
            // Ad loaded
            Log.d("ExampleActivity", "Ad loaded for zone " + ad.zoneId + " with targeting: " + ad.targeting)
            tradecoreInstreamAdUnit?.show()
        }

        override fun onAdFailed(error: TradecoreError) {
            // Ad failed to load
        }

        override fun shouldPauseContent() {
            // Ad is going to start, the content player should be paused
            savedPosition = videoPlayer.currentPosition
            videoPlayer.stopPlayback()
            videoPlayer.setMediaController(null)
        }

        override fun shouldResumeContent() {
            // Ad is completed, the content player should be resumed
            videoPlayer.setVideoPath(SAMPLE_VIDEO_URL)
            videoPlayer.setMediaController(mediaController)
            videoPlayer.setOnPreparedListener { player: MediaPlayer ->
                if (savedPosition > 0) {
                    player.seekTo(savedPosition)
                }
                player.start()
            }
        }

        override fun onAdFinished(ad: TradecoreAd) {
            // Ad finished playing
        }

        override fun onAdStarted(ad: TradecoreAd) {
            // Ad started playing
        }

        override fun onAdClicked(ad: TradecoreAd) {
            // Ad clicked
        }
    }
}
```

**Java:**

```java
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
```

Most of the interface methods provide a parameter containing metadata about the loaded ad. You can find detailed
information about the ad metadata structure and its fields in the [Ad Metadata](./tradecore-sdk-ad-metadata.md) section.

## Destroy method

When you leave the screen or you no longer need the ad unit you should call `destroy()` method. It disables the auto refresh and clears all resources.

**Kotlin:**

```kotlin
    override fun onDestroy() {
        super.onDestroy()
        tradecoreInstreamAdUnit?.destroy()
    }
```

**Java:**

```java
    @Override
    protected void onDestroy() {
        super.onDestroy();
        tradecoreInstreamAdUnit.destroy();
    }
```

## TradecoreInstreamAdUnit configuration

Please refer to the table below for a detailed overview of the `TradecoreInstreamAdUnit` properties and methods
available for configuration in rendering mode. You can use this method to change the ad video player state.

| Property/Methods | Description                     |
|------------------|---------------------------------|
| `start`          | Used to start the ad playback.  |
| `pause`          | Used to pause the ad playback.  |
| `resume`         | Used to resume the ad playback. |
| `skip`           | Used to skip the ad playback.   |