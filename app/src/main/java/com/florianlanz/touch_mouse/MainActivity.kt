package com.florianlanz.touch_mouse

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

    fun create2x2Grid(view: View) {
        drawView = DrawView(this, 2)
        drawView.setBackgroundColor(Color.WHITE)
        setContentView(drawView)
    }

    fun create3x3Grid(view: View) {
        drawView = DrawView(this, 3)
        drawView.setBackgroundColor(Color.WHITE)
        setContentView(drawView)
    }

    fun create4x4Grid(view: View) {
        drawView = DrawView(this, 4)
        drawView.setBackgroundColor(Color.WHITE)
        setContentView(drawView)
    }


}