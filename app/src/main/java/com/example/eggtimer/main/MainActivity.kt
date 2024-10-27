package com.example.eggtimer.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eggtimer.R
import com.example.eggtimer.main.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}