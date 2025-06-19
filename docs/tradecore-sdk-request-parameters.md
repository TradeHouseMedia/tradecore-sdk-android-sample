# Tradecore Android SDK - Request Parameters

Request parameters come in two types: stack-level and zone-specific.

## Stack Parameters

Stack parameters are global settings that apply universally across the SDK, affecting all zones (placements) within the
stack.

### GAM

The `TradecoreGamStackParameters` class is designed to manage Google Ad Manager (GAM) configuration for the
entire stack. Below is an overview of its properties:

| Property      | Description                                            |
|---------------|--------------------------------------------------------|
| `testDevices` | Array of test device IDs that always request test ads. |

Example of usage:

```kotlin
TradecoreSDK.requestParameters = listOf(
    TradecoreGamStackParameters(listOf("testDeviceId"))
)
```

## Zone Parameters

Zone parameters are settings unique to each placement or zone, enabling tailored configurations for each ad request.

### GAM

The `TradecoreGamZoneParameters` class should be used to configure Google Ad Manager (GAM) settings at the zone
level.

| Property              | Description                                                                                                             |
|-----------------------|-------------------------------------------------------------------------------------------------------------------------|
| `adUnitId`            | Custom GAM ad unit id for this ad view.                                                                                 |
| `customTargeting`     | Key-value pairs used for custom targeting in ad requests.                                                               |
| `categoryExclusions`  | Array of categories to exclude from ad results.                                                                         |
| `keywords`            | Array of keywords describing current user activity (e.g., "Sports Scores" or "Football"). Set to nil to clear keywords. |
| `contentUrl`          | URL of a webpage matching the app’s primary content, used for targeting and brand safety purposes.                      |
| `publisherProvidedId` | User ID provided by the publisher for targeting purposes.                                                               |
| `adMobExtras`         | Additional ad network parameters sent to Google networks for AdMob adapter.                                             |

Example of usage: 

```kotlin
adView.setZoneParameters(
    listOf(
        TradecoreGamZoneParameters(
            adUnitId = "/testapp/adunitid",
            customTargeting = listOf(
                TradecoreGamZoneParameters.CustomTargeting.of("key_1", "value_1"),
                TradecoreGamZoneParameters.CustomTargeting.of("key_2", listOf("array_1", "array_2")),
            ),
            categoryExclusions = listOf("exclusion_1", "exclusion_2"),
            keywords = listOf("keyword_1", "keyword_2"),
            contentUrl = "test_content_url",
            publisherProvidedId = "test_publisher_provided_id",
            adMobExtras = Bundle().apply {
                putString("correlator", "test_correlator")
            }
        )
    )
)
```