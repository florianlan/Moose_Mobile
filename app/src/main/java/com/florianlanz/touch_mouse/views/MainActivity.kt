package com.florianlanz.touch_mouse.views

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.Vibrator
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.florianlanz.touch_mouse.R
import com.florianlanz.touch_mouse.controller.Networker
import com.florianlanz.touch_mouse.data.Consts.INTS
import com.florianlanz.touch_mouse.data.Consts.STRINGS
import com.florianlanz.touch_mouse.data.Memo

class MainActivity : AppCompatActivity() {
    private lateinit var drawView: DrawView
    private var dialogBuilder: AlertDialog.Builder? = null //for creating dialogs
    private val dialog: AlertDialog? = null

    private var sizeX: Int = 0
    private var sizeY: Int = 0
    private var xDown: Int = 0
    private var yDown: Int = 0
    private var xUp: Int = 0
    private var yUp: Int = 0
    private var screenX: Int = 0
    private var screenY: Int = 0

    private lateinit var sp: SharedPreferences //shared preferences
    private var top: Int = 0
    private var bot: Int = 0
    private var left: Int = 0
    private var right: Int = 0
    private var cols: Int = 0
    private var rows: Int = 0

    private val offset = 347

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

        // Get Instances of config
        sp = applicationContext.getSharedPreferences("grid", Context.MODE_PRIVATE)
        top = sp.getInt("pad_top", 0)
        bot = sp.getInt("pad_bot", 100)
        left = sp.getInt("pad_left", 0)
        right = sp.getInt("pad_right", 0)
        cols = sp.getInt("cols", 3)
        rows = sp.getInt("rows", 3)
        screenX = Resources.getSystem().displayMetrics.widthPixels
        screenY = Resources.getSystem().displayMetrics.heightPixels

        // Calculate GridSize
        sizeX = screenX - left - right
        sizeY = screenY - top - bot - offset


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

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x: Int = event!!.x.toInt()
        val y: Int = event.y.toInt()

        // Send touch event to desktop app
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                xDown = x - left
                yDown = y - offset - top
                Log.d("Koordinate_Down", "$xDown;$yDown")
                //save time of eventDown
            }
            MotionEvent.ACTION_UP -> {
                xUp = x - left
                yUp = y - offset - top
                Log.d("Koordinate_Up", "$xDown;$yDown")
                //save time of eventUp

                var memo = Memo(
                    STRINGS.SCROLL,
                    STRINGS.DRAG,
                    getStringOfCoord(xDown, yDown),
                    getStringOfCoord(sizeX, sizeY)
                )
                Networker.get().sendMemo(memo)
                Log.d("Memo", memo.toString())

            }
        }

        return super.onTouchEvent(event)
    }

    private fun getStringOfCoord(x: Int, y: Int): String {
        return "$x,$y"
    }


}