package com.florianlanz.touch_mouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //TODO: get valid settings from config

    }

    fun saveSettings(view: View) {
        //TODO: save settings into config file

        finish()

    }
}