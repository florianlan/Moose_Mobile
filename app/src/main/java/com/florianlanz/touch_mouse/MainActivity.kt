package com.florianlanz.touch_mouse

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    private lateinit var drawView: DrawView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    /**
     * create grid
     */
    fun createGrid(view: View) {
        drawView = DrawView(this)
        drawView.setBackgroundColor(Color.WHITE)
        setContentView(drawView)
    }

    /**
     * start Settings activity
     */
    fun settings(view: View) {
        startActivity(Intent(this, SettingsActivity::class.java))

    }

}