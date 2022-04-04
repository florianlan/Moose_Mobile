package com.florianlanz.touch_mouse

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    lateinit var drawView: DrawView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawView = DrawView(this)
        drawView.setBackgroundColor(Color.WHITE)
        setContentView(drawView)

    }


}