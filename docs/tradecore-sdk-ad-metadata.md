# Tradecore Android SDK - Ad Metadata

The `TradecoreAd` class encapsulates essential metadata and targeting information related to an ad instance. It is
commonly used across the SDK for `TradecoreAdView` and `TradecoreInstreamVideo`, and appears as a parameter in most
interface callbacks.

This class can be especially useful for:

- ad tracking and reporting;
- debugging ad delivery and targeting issues;
- accessing ad network-specific response data.

The table below provides an overview of the available properties in `TradecoreAd`:

| Property       | Description                                                                                                       |
|----------------|-------------------------------------------------------------------------------------------------------------------|
| `zoneId`       | ID used to obtain configuration from the global stack.                                                            |
| `responseInfo` | Google Mobile Ads specific information about the ad. Should be cast to `com.google.android.gms.ads.ResponseInfo`. |
| `targeting`    | Google Mobile Ads targeting information that should be applied to the GAM's `cust_params` query parameter.        |