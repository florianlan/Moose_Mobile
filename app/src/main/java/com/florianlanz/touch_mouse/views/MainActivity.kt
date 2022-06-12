package com.florianlanz.touch_mouse.views

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.florianlanz.touch_mouse.R
import com.florianlanz.touch_mouse.controller.Networker
import com.florianlanz.touch_mouse.data.Consts.INTS

class MainActivity : AppCompatActivity() {
    private lateinit var drawView: DrawView
    private var dialogBuilder: AlertDialog.Builder? = null //for creating dialogs
    private val dialog: AlertDialog? = null

    // Main Handler
    @SuppressLint("HandlerLeak")
    private val mainHandler: Handler = object : Handler() {
        override fun handleMessage(mssg: Message) {
            Log.d("MainActivity/", "handleMessage: " + mssg.what)
            if (mssg.what == INTS.CLOSE_DLG) {
                dialog?.dismiss()
                //drawUI()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE) //removing the status bar
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setting
        dialogBuilder = AlertDialog.Builder(this)
        Networker.get().setVibrator(getSystemService(VIBRATOR_SERVICE) as Vibrator)
        Networker.get().setMainHandler(mainHandler)

        // Connecting to desktop...
        Toast.makeText(this, "connecting to desktop...", Toast.LENGTH_LONG).show()
        Networker.get().connect()

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