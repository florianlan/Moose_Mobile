package com.florianlanz.touch_mouse.views

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import com.florianlanz.touch_mouse.controller.Networker
import com.florianlanz.touch_mouse.data.Consts.STRINGS
import com.florianlanz.touch_mouse.data.Memo

class DrawView(ctx: Context) : View(ctx) {
    private var sp: SharedPreferences
    private var pTop: Int
    private var pBot: Int
    private var pLeft: Int
    private var pRight: Int
    private var cols: Int
    private var rows: Int
    private var showSym: Boolean

    private var paint = Paint()

    init {
        sp = ctx.applicationContext.getSharedPreferences("grid", Context.MODE_PRIVATE)
        pTop = sp.getInt("pad_top", 0)
        pBot = sp.getInt("pad_bot", 100)
        pLeft = sp.getInt("pad_left", 0)
        pRight = sp.getInt("pad_right", 0)
        cols = sp.getInt("cols", 3)
        rows = sp.getInt("rows", 3)
        showSym = sp.getBoolean("show_sym", false)

        paint.color = Color.BLACK
    }


    /**
     * draw the grid of selected grid size
     */
    override fun onDraw(canvas: Canvas) {
        val width = (this.width - pLeft - pRight) / cols
        val height = (this.height - pTop - pBot) / rows

        //vertical lines
        for (i in 0 until cols + 1) {
            canvas.drawLine(
                i * width.toFloat() + pLeft,
                pTop.toFloat(),
                i * width.toFloat() + pLeft,
                rows * height.toFloat() + pTop,
                paint
            )

        }

        //horizontal lines
        for (i in 0 until rows + 1) {
            canvas.drawLine(
                pLeft.toFloat(),
                i * height.toFloat() + pTop,
                cols * width.toFloat() + pLeft,
                i * height.toFloat() + pTop,
                paint
            )

        }

        //TODO: draw grid only when Symbols are shown too
        //show Symbols in the Grid
        if (showSym) {
            paint.strokeWidth = 4f
            val padHor = width / 5f //padding for Symbols horizontally
            val padVer = height / 5f //padding for Symbols vertically
            val offsetVer = padVer * 3 / (rows - 1)
            val offsetHor = padHor * 3 / (cols - 1)

            for (i in 0 until rows) {
                for (j in 0 until cols) {
                    //vertical line
                    canvas.drawLine(
                        j * width.toFloat() + j * offsetHor + pLeft + padHor,
                        i * height.toFloat() + pTop + padVer,
                        j * width.toFloat() + j * offsetHor + pLeft + padHor,
                        (i + 1) * height.toFloat() + pTop - padVer,
                        paint
                    )

                    //horizontal line
                    canvas.drawLine(
                        j * width.toFloat() + pLeft + padHor,
                        i * height.toFloat() + i * offsetVer + pTop + padVer,
                        (j + 1) * width.toFloat() + pLeft - padHor,
                        i * height.toFloat() + i * offsetVer + pTop + padVer,
                        paint
                    )
                }
            }
        }

        // Send INIT GRID size message to desktop
        Networker.get().sendMemo(Memo(STRINGS.INTRO, STRINGS.GRID, rows, cols))

    }

}