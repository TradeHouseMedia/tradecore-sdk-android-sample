# Tradecore Android SDK - Integration

## Import library using Gradle

1. Add the dependency url to the Gradle config (where you declare google(), mavenCentral()).

```kotlin
dependencyResolutionManagement {
    repositories {
        maven {
            url = uri("https://tradecore-v2.s3.eu-west-2.amazonaws.com/android/")
        }
    }
}
```

2. Add the dependencies.

```groovy
def tradecoreVersion = "1.3.8"
implementation("com.tradehousemedia:tradecore-core:$tradecoreVersion")
implementation("com.tradehousemedia:tradecore-google-ad-manager:$tradecoreVersion")
implementation("com.tradehousemedia:tradecore-prebid:$tradecoreVersion")
implementation("com.tradehousemedia:tradecore-amazon:$tradecoreVersion")
implementation("com.tradehousemedia:tradecore-ima:$tradecoreVersion")
```

3. Remove Amazon, Google ads and Prebid dependencies, because Tradecore SDK provides them inside.

## SDK initialization

Insert Google Ad Manager application id generated for your application to AndroidManifest.xml (see [documentation](https://developers.google.com/ad-manager/mobile-ads-sdk/android/quick-start) and [where to find](https://support.google.com/admanager/answer/15014197#app-id)).

```xml
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="ca-app-pub-***" />
```

Initialize Tradecore SDK during the application creation (in the `Application.onCreate()` method).

**Kotlin:**

```kotlin
class TheApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        TradecoreSDK.initialize(STACK_ID, this)
    }

}
```

**Java:**

```java
public class TheApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TradecoreSDK.initialize(STACK_ID, this);
    }

}
```

- `STACK_ID` is the identifier for the remote configuration of the app.

### Additional SDK configuration

Refer to the table below for a breakdown of the `TradecoreSDK` properties you can use for the configuration.

| Property            | Description                                                                                                                                                                |
|---------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `logLevel`          | Controls the verbosity of the SDK logs.                                                                                                                                    |
| `debugMode`         | Enables debug mode, which activates test mode in integrated ad SDKs and extra logging.                                                                                     |
| `requestParameters` | Global request parameters, e.g., test device identifiers. For more information, see the [Request Parameters](./tradecore-sdk-request-parameters.md#stack-parameters) page. |
| `version`           | Returns the current SDK version as a string.                                                                                                                               |


