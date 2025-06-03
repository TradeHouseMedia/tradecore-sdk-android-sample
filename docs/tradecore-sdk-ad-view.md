# Tradecore Android SDK - Ad View Integration Guide

To load ad view, create a `TradecoreAdView` object, configure it, add it to the view hierarchy, and call its `load()`
method.

**Kotlin:**

```kotlin
    private var adView: TradecoreAdView? = null

private fun loadAd() {
    val adView = TradecoreAdView(this).apply { adView = this }
    adView.zoneId = ZONE_ID
    adView.loadAd()
    adContainer.addView(adView)
}
```

**Java:**

```java
private TradecoreAdView adView;

private void loadAd() {
    adView = new TradecoreAdView(this);
    adView.setZoneId(ZONE_ID);
    adView.loadAd();
    getAdContainer().addView(adView);
}
```

- `ZONE_ID` is the id used to apply the configuration for the current ad unit.

## Destroy method

When you leave the screen or you no longer need the ad view you should call `destroy()` method. It disables the auto refresh and clears all resources. 

**Kotlin:**

```kotlin
    override fun onDestroy() {
        super.onDestroy()
        adView?.destroy()
    }
```

**Java:**

```java
    @Override
    protected void onDestroy() {
        super.onDestroy();
        adView.destroy();
    }
```

## Ad events

By utilizing the `TradecoreAdViewListener` interface, you gain the ability to observe and respond to various ad
lifecycle events.

**Kotlin:**

```kotlin
    adView.setAdListener(
    object : TradecoreAdViewListener {
        override fun onAdLoaded(ad: TradecoreAd) {
            // Ad is loaded
        }

        override fun onAdFailed(tradecoreError: TradecoreError) {
            // Ad failed to load
            Log.d(TAG, "Ad failed to load: " + tradecoreError.message)
        }

        override fun onAdImpression(ad: TradecoreAd) {
            // Informs that an impression has occurred.
        }

        override fun onAdClicked(ad: TradecoreAd) {
            // Ad clicked
        }

        override fun onAdOpened(ad: TradecoreAd) {
            // Ad opened to full screen
        }

        override fun onAdClosed(ad: TradecoreAd) {
            // Ad closed
        }
    }
)
```

**Java:**

```java
adView.setAdListener(new TradecoreAdViewListener() {
    @Override
    public void onAdLoaded(@NotNull TradecoreAd tradecoreAd){
        // Ad is loaded
    }

    @Override
    public void onAdFailed (@NotNull TradecoreError tradecoreError){
        // Ad failed to load
        Log.d(TAG, "Ad failed to load: " + tradecoreError.getMessage());
    }

    @Override
    public void onAdImpression (@NotNull TradecoreAd tradecoreAd){
        // Informs that an impression has occurred
    }

    @Override
    public void onAdClicked (@NotNull TradecoreAd tradecoreAd){
        // Ad clicked
    }

    @Override
    public void onAdClosed (@NotNull TradecoreAd tradecoreAd){
        // Ad closed
    }

    @Override
    public void onAdOpened (@NotNull TradecoreAd tradecoreAd){
        // Ad opened to full screen
    }
});
```

Most of the interface methods provide a parameter containing metadata about the loaded ad. You can find detailed
information about the ad metadata structure and its fields in the [Ad Metadata](./tradecore-sdk-ad-metadata.md) section.

## TradecoreAdView parameters

Refer to the table below for a breakdown of the `TradecoreAdView` properties you can use for configuration.

| Property            | Description                                                                                                                                                                        |
|---------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `requestParameters` | Instance property to send first-party data for every Tradecore zone. See [Request Parameters](./tradecore-sdk-request-parameters.md#zone-parameters) section for more information. |
| `adSizes`           | Represents the sizes applied to this zone. Get-only property.                                                                                                |
    