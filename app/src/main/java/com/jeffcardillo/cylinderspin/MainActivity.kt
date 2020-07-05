package com.jeffcardillo.cylinderspin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val targetFps = 60
    private val targetMillisPerFrame = 1000 / targetFps

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // kick off render loop...
        GlobalScope.launch {
            while(true) {
                invalidateAllViews()
            }
        }
    }

    /*
     * Measures the amount of time it takes to render all views in the layout
     * and sleep for targetMillisPerFrame - timeToRender
     */
    private suspend fun invalidateAllViews() {
        val startTime = System.currentTimeMillis()

        for (view in root.children) {
            view.invalidate()
        }

        val sleepTime = (targetMillisPerFrame - (System.currentTimeMillis() - startTime)).coerceAtLeast(0)

        delay(sleepTime)
    }
}
