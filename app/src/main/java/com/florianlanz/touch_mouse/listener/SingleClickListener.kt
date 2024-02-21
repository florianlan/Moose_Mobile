package com.florianlanz.touch_mouse.listener

import android.graphics.RectF
import android.util.Log
import android.view.MotionEvent
import android.view.View

class EventWithCoordinates(val xDown: Int, val yDown: Int, val xUp: Int, val yUp: Int, val clickDuration: Int)

class SingleClickListener(private val clickAction: (event: EventWithCoordinates) -> Unit,
                          private val tapArea1: RectF
                          ) : View.OnTouchListener {
    private var downTime: Long = 0
    private var downPointerId = -1
    private var xDown: Int = 0
    private var yDown: Int = 0
    private var isTAPValid = false

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                downTime = System.currentTimeMillis()
                downPointerId = event.getPointerId(event.actionIndex)
                xDown = event.getX(event.actionIndex).toInt()
                yDown = event.getY(event.actionIndex).toInt()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                Log.d("TAP", "action up")
                if (downPointerId == -1) return false

                Log.d("TAP", "check pointer")

                isTAPValid = tapArea1.contains(event.getX(event.actionIndex), event.getY(event.actionIndex))

                Log.d("TAP", "in field: $isTAPValid")
                if (isTAPValid && event.getPointerId(event.actionIndex) == downPointerId) {
                    Log.d("TAP", "valid and is last finger")
                    val clickDuration = System.currentTimeMillis() - downTime
                    if (clickDuration < 1000) {
                        Log.d("Memo", "is short click")
                        val xUp = event.getX(event.getPointerId(event.actionIndex)).toInt()
                        val yUp = event.getY(event.getPointerId(event.actionIndex)).toInt()
                        // Ausführen des Events für den kurzen Klick
                        clickAction.invoke(EventWithCoordinates(xDown, yDown, xUp, yUp, clickDuration.toInt()))
                    }
                }
                downPointerId = -1
            }
        }
        return true
    }
}


