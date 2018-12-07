package com.nathan.equipapp.feature

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.nathan.equipapp.feature.ui.talks.TalksFragment

class Talks : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.talks_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, TalksFragment.newInstance())
                    .commitNow()
        }
    }

}
