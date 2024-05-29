package com.example.habifus

import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.os.Looper
import android.widget.TextView

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

        progressBar = findViewById(R.id.progressBar)

        val totalProgressTime = 3000 // Total duration of splash screen in milliseconds
        val interval = 50 // Interval in milliseconds to update progress
        var currentProgress = 0

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (currentProgress >= 100) {
                    // Navigate to LoginActivity
                    val intent = Intent(this@SplashScreenActivity, SignUpActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    currentProgress++
                    progressBar.progress = currentProgress
                    handler.postDelayed(this, interval.toLong())
                }
            }
        }, interval.toLong())
    }
}