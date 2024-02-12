package com.florianlanz.touch_mouse.views

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Color
import android.graphics.RectF
import android.os.*
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
import com.florianlanz.touch_mouse.listener.EventWithCoordinates
import com.florianlanz.touch_mouse.listener.SingleClickListener

class MainActivity : AppCompatActivity() {

    private lateinit var drawView: DrawView
    private var dialogBuilder: AlertDialog.Builder? = null //for creating dialogs
    private val dialog: AlertDialog? = null

    private var xDown: Int = 0
    private var yDown: Int = 0
    private var xUp: Int = 0
    private var yUp: Int = 0
    private var screenW: Int = 0
    private var screenH: Int = 0

    private lateinit var sp: SharedPreferences //shared preferences
    private var top: Int = 0
    private var bot: Int = 0
    private var left: Int = 0
    private var right: Int = 0

    // Two rows vars
    private val expRows = intArrayOf(1, 3)

    private lateinit var singleClickListener: SingleClickListener

    companion object {
        var sizeX: Int = 0
        var sizeY: Int = 0

    }

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
        top = sp.getInt("pad_top", 50)
        bot = sp.getInt("pad_bot", 800)
        left = sp.getInt("pad_left", 50)
        right = sp.getInt("pad_right", 50)

        // Subtract the height of the status bar and navigation bar
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        val statusBarHeight = if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0

        screenW = Resources.getSystem().displayMetrics.widthPixels
        screenH = Resources.getSystem().displayMetrics.heightPixels

        // Calculate GridSize
        sizeX = screenW - left - right
        sizeY = screenH - top - bot - statusBarHeight

        // Fill shared Preferences
        val editor = sp.edit()
        //TODO: implement logic to set active rows
        editor.apply {
            putInt("row1", expRows[0])
            putInt("row2", expRows[1])
            apply()
        }

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

        val singleClickListener = SingleClickListener({
                event: EventWithCoordinates ->
            xDown = event.xDown - left
            yDown = event.yDown - top
            xUp = event.xUp - left
            yUp = event.yUp - top

            Log.d("Coordinates", "down: $xDown;$yDown up: $xUp;$yUp")
            //save time of eventUp

            //TODO: test if down and up coordinates works
            val memo = Memo(
                STRINGS.SCROLL,
                STRINGS.DRAG,
                getStringOfCoord(xDown, yDown),
                getStringOfCoord(xUp, yUp)
            )
            Networker.get().sendMemo(memo)
            Log.d("Memo", memo.toString())

/*
                //vibration on click event
                val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
*/

            // Get the Vibrator service
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

            // Check if the device supports vibration
            if (vibrator.hasVibrator()) {
                vibrator.vibrate(100)
            }

        }, RectF(left.toFloat(), top.toFloat(), left.toFloat() + sizeX, top.toFloat() + sizeY))

        drawView.setOnTouchListener(singleClickListener)

    }

    /**
     * start Settings activity
     */
    fun settings(view: View) {
        startActivity(Intent(this, SettingsActivity::class.java))

    }

    private fun getStringOfCoord(x: Int, y: Int): String {
        return "$x,$y"
    }


}