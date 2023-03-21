package com.florianlanz.touch_mouse.listener

import android.graphics.RectF
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import kotlin.math.sqrt

class SingleClickListener(private val clickAction: (event: MotionEvent) -> Unit, private val validClickArea: RectF) : View.OnTouchListener {
    private var downTime: Long = 0
    private var lastPointerId = -1
    private var isClickValid = false

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                downTime = System.currentTimeMillis()
                lastPointerId = event.getPointerId(event.actionIndex)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                Log.d("Memo", "action up")
                if (lastPointerId == -1) return false

                Log.d("Memo", "check pointer")
                isClickValid = validClickArea.contains(event.getX(event.actionIndex), event.getY(event.actionIndex))
                Log.d("Memo", "in field: " + isClickValid.toString())
                if (isClickValid && event.getPointerId(event.actionIndex) == lastPointerId) {
                    Log.d("Memo", "valid and is last finger")
                    val clickDuration = System.currentTimeMillis() - downTime
                    if (clickDuration < 200) {
                        Log.d("Memo", "is short click")
                        // Ausführen des Events für den kurzen Klick
                        clickAction.invoke(event)
                    }
                }
                lastPointerId = -1
            }
        }
        return true
    }
}


