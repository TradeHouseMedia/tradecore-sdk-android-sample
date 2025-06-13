package com.tradehousemedia.tradecore.kotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.tradehousemedia.tradecore.kotlin.utils.Example
import com.tradehousemedia.tradecore.kotlin.utils.ExampleHolder

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setExampleList()
    }

    private fun setExampleList() {
        val examples = ExampleHolder.examples

        val viewExamples = findViewById<ListView>(R.id.viewExamples)
        viewExamples.setOnItemClickListener { adapterView: AdapterView<*>?, view: View?, i: Int, l: Long ->
            val classActivity = examples[i].classActivity
            ExampleHolder.lastExample = examples[i]
            startActivity(Intent(this, classActivity))
        }
        val adapter = ArrayAdapter<Example?>(this, android.R.layout.simple_list_item_1, examples)
        viewExamples.setAdapter(adapter)
    }

}