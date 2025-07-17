# Tradecore Android SDK - Ad View Integration Guide

To load ad view, create a `TradecoreAdView` object, configure it, add it to the view hierarchy, and call its `load()`
method.

**Kotlin:**

```kotlin
private var adView: TradecoreAdView? = null

private fun loadAd() {
    // 1. Create TradecoreAdView
    val adView = TradecoreAdView(this).apply { adView = this }

    // 2. Set the zone id
    adView.zoneId = ZONE_ID

    // 3. Load the ad
    adView.loadAd()

    // 4. Add the ad view to the screen
    adContainer.addView(adView)
}
```

**Java:**

```java
private TradecoreAdView adView;

private void loadAd() {
    // 1. Create TradecoreAdView
    adView = new TradecoreAdView(this);

    // 2. Set the zone id
    adView.setZoneId(ZONE_ID);

    // 3. Load the ad
    adView.loadAd();

    // 4. Add the ad view to the screen
    getAdContainer().addView(adView);
}
```

- `ZONE_ID` is the id used to apply the configuration for the current ad unit.

## Destroy method

When you leave the screen or you no longer need the ad view you should call `destroy()` method. It disables the auto
refresh and clears all resources.

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
            
            // Change the layout according to TradecoreAdView size.
	        val adSize = ad.adSize
   		    Log.d("TAG, "Tradecore Ad View on load with size: ${ad.adSize?.width}x${ad.adSize?.height}")
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
    public void onAdLoaded (@NotNull TradecoreAd tradecoreAd){
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

| Property         | Description                                                                                                                                                                        |
|------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `zoneParameters` | Instance property to send first-party data for every Tradecore zone. See [Request Parameters](./tradecore-sdk-request-parameters.md#zone-parameters) section for more information. |

Also, you can set the custom GAM ad unit id using zone parameters.

```kotlin
adView.setZoneParameters(
    listOf(
        TradecoreGamZoneParameters(
            adUnitId = "/testapp/adunitid"
        )
    )
)
```