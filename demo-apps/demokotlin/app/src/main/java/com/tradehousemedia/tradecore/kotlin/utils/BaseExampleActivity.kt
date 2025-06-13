package com.tradehousemedia.tradecore.kotlin.utils

import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tradehousemedia.tradecore.kotlin.R

open class BaseExampleActivity : AppCompatActivity() {

    fun setTitle() {
        val title = findViewById<TextView>(R.id.viewTitle)
        title.text = ExampleHolder.lastExample.title
    }

    val adContainer by lazy { findViewById<LinearLayout>(R.id.viewAdHolder) }

    val refreshButton by lazy { findViewById<Button>(R.id.viewRefresh) }

}

