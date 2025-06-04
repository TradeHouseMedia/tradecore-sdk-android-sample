package com.tradehousemedia.tradecore.kotlin.utils

class Example(
    var title: String,
    var classActivity: Class<*>?,
) {
    override fun toString(): String {
        return title
    }
}