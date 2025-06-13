package com.tradehousemedia.tradecore.kotlin.utils

import com.tradehousemedia.tradecore.kotlin.examples.TradecoreAdView300x250Activity
import com.tradehousemedia.tradecore.kotlin.examples.TradecoreAdView320x50Activity
import com.tradehousemedia.tradecore.kotlin.examples.TradecoreInstreamKeywordsActivity
import com.tradehousemedia.tradecore.kotlin.examples.TradecoreInstreamMedia3Activity

object ExampleHolder {

    val examples = listOf(
        Example(
            "Tradecore Ad View 320x50",
            TradecoreAdView320x50Activity::class.java
        ),
        Example(
            "Tradecore Ad View 300x250",
            TradecoreAdView300x250Activity::class.java
        ),
//        Example(
//            "Tradecore Instream Media3",
//            TradecoreInstreamMedia3Activity::class.java
//        ),
        Example(
            "Tradecore Instream Keywords",
            TradecoreInstreamKeywordsActivity::class.java
        )
    )

    var lastExample: Example = examples[0]

}
