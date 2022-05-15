package com.florianlanz.touch_mouse

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View

class DrawView(ctx: Context) : View(ctx) {
    private var sp: SharedPreferences
    private var pTop: Int
    private var pBot: Int
    private var pLeft: Int
    private var pRight: Int
    private var cols: Int
    private var rows: Int

    private var paint = Paint()

    init {
        sp = ctx.applicationContext.getSharedPreferences("grid", Context.MODE_PRIVATE)
        pTop = sp.getInt("pad_top", 0)
        pBot = sp.getInt("pad_bot", 100)
        pLeft = sp.getInt("pad_left", 0)
        pRight = sp.getInt("pad_right", 0)
        cols = sp.getInt("cols", 3)
        rows = sp.getInt("rows", 3)

    }

    private fun init() {
        paint.color = Color.BLACK
    }

    /**
     * draw the grid of selected grid size
     */
    override fun onDraw(canvas: Canvas) {
        val width = (this.width - pLeft - pRight) / cols
        val height = (this.height - pTop - pBot) / rows

        //vertical lines
        for (i in 0 until cols+1) {
            canvas.drawLine(
                i * width.toFloat() + pLeft,
                pTop.toFloat(),
                i * width.toFloat() + pLeft,
                rows * height.toFloat() + pTop,
                paint
            )

        }

        //horizontal lines
        for (i in 0 until rows+1) {
            canvas.drawLine(
                pLeft.toFloat(),
                i * height.toFloat() + pTop,
                cols * width.toFloat() + pLeft,
                i * height.toFloat() + pTop,
                paint
            )

        }
    }


}