package com.tradehousemedia.tradecore.java;

import android.app.Application;
import com.tradehousemedia.tradecore.core.TradecoreSDK;
import com.tradehousemedia.tradecore.core.utils.TradecoreLogLevel;

public class TheApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TradecoreSDK.setLogLevel(TradecoreLogLevel.DEBUG);
        TradecoreSDK.initialize("tradecore-stack-65", this);
    }

}
