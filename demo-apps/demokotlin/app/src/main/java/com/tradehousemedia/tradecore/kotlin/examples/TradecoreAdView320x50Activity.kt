package com.tradehousemedia.tradecore.kotlin.examples

import android.os.Bundle
import android.util.Log
import android.view.View
import com.tradehousemedia.tradecore.core.TradecoreAd
import com.tradehousemedia.tradecore.core.TradecoreAdView
import com.tradehousemedia.tradecore.core.TradecoreError
import com.tradehousemedia.tradecore.core.listeners.TradecoreAdViewListener
import com.tradehousemedia.tradecore.kotlin.R
import com.tradehousemedia.tradecore.kotlin.utils.BaseExampleActivity

class TradecoreAdView320x50Activity : BaseExampleActivity() {

    companion object {
        private const val TAG = "ExampleActivity"
    }

    private var adView: TradecoreAdView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)
        setTitle()
        refreshButton.setOnClickListener { v: View? -> loadAd() }

        loadAd()
    }

    override fun onDestroy() {
        super.onDestroy()
        adView?.destroy()
    }

    private fun loadAd() {
        adContainer.removeAllViews()

        val adView = TradecoreAdView(this).apply { adView = this }
        adView.zoneId = "tradecore-zone-3493"
        adView.setAdListener(createListener())
        adView.loadAd()
        adContainer.addView(adView)
    }

    private fun createListener(): TradecoreAdViewListener {
        return object : TradecoreAdViewListener {
            override fun onAdLoaded(ad: TradecoreAd) {
                // Ad is loaded
                Log.d(TAG, "Ad view width: ${ad.adSize?.width}, height: ${ad.adSize?.height}")
                Log.d(TAG, "Banner view width: ${adView?.width}, height: ${adView?.height}")
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
    }

}
