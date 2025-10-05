package com.keagan.complete.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.keagan.complete.R
import com.keagan.complete.ui.splash.SwooshView

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val swoosh = findViewById<SwooshView>(R.id.swooshView)
        val pen = findViewById<ImageView>(R.id.ivPen)

        // Track the pen along the swoosh path
        swoosh.onProgress = { x, y ->
            // Position relative to the parent: add swoosh's left/top
            pen.translationX = swoosh.left + x - pen.width * 0.15f
            pen.translationY = swoosh.top + y - pen.height * 0.85f
        }

        swoosh.onFinish = {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
