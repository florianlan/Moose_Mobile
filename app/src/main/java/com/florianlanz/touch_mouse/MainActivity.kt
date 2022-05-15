package com.florianlanz.touch_mouse

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    private lateinit var drawView: DrawView

    //TODO: outsource vars for the dimensions
    private var pad_top: Int = 0
    private var pad_bottom: Int = 50
    private var pad_left: Int = 20
    private var pad_right: Int = 20
    private var columns: Int = 3
    private var rows: Int = 3


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