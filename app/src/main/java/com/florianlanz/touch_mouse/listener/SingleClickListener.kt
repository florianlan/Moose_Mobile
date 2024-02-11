package com.florianlanz.touch_mouse.listener

import android.graphics.RectF
import android.util.Log
import android.view.MotionEvent
import android.view.View

class SingleClickListener(private val clickAction: (event: MotionEvent) -> Unit,
                          private val tapArea1: RectF
                          ) : View.OnTouchListener {
    private var downTime: Long = 0
    private var lastPointerId = -1
    private var isTAPValid = false

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                downTime = System.currentTimeMillis()
                lastPointerId = event.getPointerId(event.actionIndex)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                Log.d("TAP", "action up")
                if (lastPointerId == -1) return false

                Log.d("TAP", "check pointer")

                isTAPValid = tapArea1.contains(event.getX(event.actionIndex), event.getY(event.actionIndex))

                Log.d("TAP", "in field: $isTAPValid")
                if (isTAPValid && event.getPointerId(event.actionIndex) == lastPointerId) {
                    Log.d("TAP", "valid and is last finger")
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


