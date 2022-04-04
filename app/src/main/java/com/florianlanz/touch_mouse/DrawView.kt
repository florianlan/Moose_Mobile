package com.florianlanz.touch_mouse

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class DrawView(ctx: Context) : View(ctx) {
    var paint = Paint()

    private fun init() {
        paint.color = Color.BLACK
    }



    override fun onDraw(canvas: Canvas) {
        val width = this.width
        canvas.drawLine(width/2f, 0f, width/2f, width.toFloat(), paint)
        canvas.drawLine(0f, width/2f, width.toFloat(), width/2f, paint)
    }

}