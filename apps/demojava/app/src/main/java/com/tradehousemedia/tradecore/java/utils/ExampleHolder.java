package com.tradehousemedia.tradecore.java.utils;

import com.tradehousemedia.tradecore.java.examples.TradecoreAdView300x250Activity;
import com.tradehousemedia.tradecore.java.examples.TradecoreAdView320x50Activity;
import com.tradehousemedia.tradecore.java.examples.TradecoreInstreamKeywordsActivity;
import com.tradehousemedia.tradecore.java.examples.TradecoreInstreamMedia3Activity;

import java.util.Arrays;
import java.util.List;

public class ExampleHolder {

    private static Example lastExample;

    public static List<Example> getExamples() {
        return Arrays.asList(
                new Example("Tradecore Ad View 320x50", TradecoreAdView320x50Activity.class),
                new Example("Tradecore Ad View 300x250", TradecoreAdView300x250Activity.class),
                new Example("Tradecore Instream Media3", TradecoreInstreamMedia3Activity.class),
                new Example("Tradecore Instream Keywords", TradecoreInstreamKeywordsActivity.class)
        );
    }

    public static void setLastExample(Example example) {
        lastExample = example;
    }

    public static Example getLastExample() {
        if (lastExample == null) {
            return getExamples().get(0);
        }
        return lastExample;
    }

}
