package com.tradehousemedia.tradecore.kotlin

import android.app.Application
import com.tradehousemedia.tradecore.core.TradecoreSDK
import com.tradehousemedia.tradecore.core.utils.TradecoreLogLevel

class TheApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        TradecoreSDK.logLevel = TradecoreLogLevel.DEBUG
        TradecoreSDK.initialize("tradecore-stack-69", this)
    }

}
