package com.tradehousemedia.tradecore.kotlin

import android.app.Application
import android.util.Log
import com.tradehousemedia.tradecore.core.TradecoreError
import com.tradehousemedia.tradecore.core.TradecoreSDK
import com.tradehousemedia.tradecore.core.listeners.TradecoreInitializationListener
import com.tradehousemedia.tradecore.core.utils.TradecoreLogLevel

class TheApplication : Application() {

    companion object {
        private const val TAG = "TheApplication"
    }

    override fun onCreate() {
        super.onCreate()
        TradecoreSDK.logLevel = TradecoreLogLevel.DEBUG
        TradecoreSDK.initialize("tradecore-stack-69", this, object : TradecoreInitializationListener {
            override fun onSdkInit() {
                Log.d(TAG, "Tradecore SDK initialized successfully")
            }

            override fun onSdkFailedToInit(error: TradecoreError) {
                Log.d(TAG, "Tradecore SDK failed to init: ${error.message}")
            }
        })
    }

}
