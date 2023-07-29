package com.r360.radioApp.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.r360.radioApp.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed(Runnable {
            gotoMainActivity()
        },4000)
    }

    private fun gotoMainActivity() {

        startActivity(Intent(this@SplashActivity,MainActivity2::class.java))
        finish();
    }
}