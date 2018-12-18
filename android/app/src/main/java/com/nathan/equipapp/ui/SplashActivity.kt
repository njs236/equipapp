package com.nathan.equipapp.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }
}
