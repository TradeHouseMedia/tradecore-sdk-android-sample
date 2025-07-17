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

class TradecoreAdView300x250Activity : BaseExampleActivity() {

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
        adView.zoneId = "tradecore-zone-3492"
        adView.setAdListener(createListener())
        adView.loadAd()
        adContainer.addView(adView)
    }

    private fun createListener(): TradecoreAdViewListener {
        return object : TradecoreAdViewListener {

            override fun onAdLoaded(ad: TradecoreAd) {
                Log.d(TAG, "Ad view width: ${ad.adSize?.width}, height: ${ad.adSize?.height}")
                Log.d(TAG, "Banner view width: ${adView?.width}, height: ${adView?.height}")
                super.onAdLoaded(ad)
            }

            override fun onAdFailed(tradecoreError: TradecoreError) {
                Log.d(TAG, "Ad failed to load: " + tradecoreError.message)
            }
        }
    }

}
