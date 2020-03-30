package com.jeffcardillo.cylinderspin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.children
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GlobalScope.launch {
            while(true) {
                invalidateCylinder()
            }
        }
    }

    private suspend fun invalidateCylinder() {
        for (view in root.children) {
            view.invalidate()
        }
        delay(40)
    }
}
