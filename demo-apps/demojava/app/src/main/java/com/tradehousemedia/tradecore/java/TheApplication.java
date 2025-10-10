package com.tradehousemedia.tradecore.java;

import android.app.Application;
import android.util.Log;
import com.tradehousemedia.tradecore.core.TradecoreError;
import com.tradehousemedia.tradecore.core.TradecoreSDK;
import com.tradehousemedia.tradecore.core.listeners.TradecoreInitializationListener;
import com.tradehousemedia.tradecore.core.utils.TradecoreLogLevel;
import org.jetbrains.annotations.NotNull;

public class TheApplication extends Application {

    private static final String TAG = "TheApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        TradecoreSDK.setLogLevel(TradecoreLogLevel.DEBUG);
        TradecoreSDK.initialize("tradecore-stack-69", this, new TradecoreInitializationListener() {
            @Override
            public void onSdkInit() {
                Log.d(TAG, "Tradecore SDK initialized successfully");
            }

            @Override
            public void onSdkFailedToInit(@NotNull TradecoreError error) {
                Log.d(TAG, "Tradecore SDK failed to init: " + error.getMessage());
            }
        });
    }

}
