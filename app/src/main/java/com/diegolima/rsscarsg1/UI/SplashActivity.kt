package com.diegolima.rsscarsg1.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.diegolima.rsscarsg1.R
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*

class SplashActivity : AppCompatActivity() {

    private val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashActivity.setOnClickListener {
            jump()
        }

        timer.schedule(object : TimerTask() {
            override fun run() {
                jump()
            }
        }, 2000)
    }

    private fun jump() {
        timer.cancel()
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}