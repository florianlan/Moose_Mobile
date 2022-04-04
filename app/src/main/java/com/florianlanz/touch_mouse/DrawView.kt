package com.florianlanz.touch_mouse

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.KeyEvent
import android.view.View
import android.widget.TextView

class DrawView(ctx: Context) : View(ctx) {
    var size = 2
    var paint = Paint()

    constructor(ctx: Context, size: Int) : this(ctx) {
        this.size = size
    }

    private fun init() {
        paint.color = Color.BLACK
    }


    /**
     * draw the grid of selected grid size
     */
    override fun onDraw(canvas: Canvas) {
        val width = this.width / size
        for (i in 1 until size) {
            //vertical lines
            canvas.drawLine(
                i * width.toFloat(),
                0f,
                i * width.toFloat(),
                size * width.toFloat(),
                paint
            )
            //horizontal lines
            canvas.drawLine(
                0f,
                i * width.toFloat(),
                size * width.toFloat(),
                i * width.toFloat(),
                paint
            )
        }
    }

}