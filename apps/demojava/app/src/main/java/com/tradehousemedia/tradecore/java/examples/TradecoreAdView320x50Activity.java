package com.tradehousemedia.tradecore.java.examples;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import com.tradehousemedia.tradecore.core.TradecoreAd;
import com.tradehousemedia.tradecore.core.TradecoreAdView;
import com.tradehousemedia.tradecore.core.TradecoreError;
import com.tradehousemedia.tradecore.core.listeners.TradecoreAdViewListener;
import com.tradehousemedia.tradecore.java.R;
import com.tradehousemedia.tradecore.java.utils.BaseExampleActivity;
import org.jetbrains.annotations.NotNull;

public class TradecoreAdView320x50Activity extends BaseExampleActivity {

    private static final String TAG = "ExampleActivity";

    private TradecoreAdView adView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        setTitle();
        getRefreshButton().setOnClickListener(v -> loadAd());

        loadAd();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adView.destroy();
    }

    private void loadAd() {
        getAdContainer().removeAllViews();

        adView = new TradecoreAdView(this);
        adView.setZoneId("tradecore-zone-3025");
        adView.setAdListener(createListener());
        adView.loadAd();
        getAdContainer().addView(adView);
    }

    private TradecoreAdViewListener createListener() {
        return new TradecoreAdViewListener() {
            @Override
            public void onAdLoaded(@NotNull TradecoreAd tradecoreAd) {

            }

            @Override
            public void onAdFailed(@NotNull TradecoreError tradecoreError) {
                Log.d(TAG, "Ad failed to load: " + tradecoreError.getMessage());
            }

            @Override
            public void onAdImpression(@NotNull TradecoreAd tradecoreAd) {

            }

            @Override
            public void onAdClicked(@NotNull TradecoreAd tradecoreAd) {

            }

            @Override
            public void onAdClosed(@NotNull TradecoreAd tradecoreAd) {

            }

            @Override
            public void onAdOpened(@NotNull TradecoreAd tradecoreAd) {

            }
        };
    }

}
