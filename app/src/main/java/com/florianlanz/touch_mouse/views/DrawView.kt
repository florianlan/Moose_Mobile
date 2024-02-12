package com.florianlanz.touch_mouse.views

import android.content.Context
import android.content.SharedPreferences
import android.graphics.*
import android.util.TypedValue
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
    private var row1: Int
    private var row2: Int
    private var dotRadius: Int
    private var showSym: Boolean
    private var showLines: Boolean
    private var showFails: Boolean
    private var showDots: Boolean

    private var paint: Paint = Paint()

    init {
        sp = ctx.applicationContext.getSharedPreferences("grid", Context.MODE_PRIVATE)
        pTop = sp.getInt("pad_top", 0)
        pBot = sp.getInt("pad_bot", 100)
        pLeft = sp.getInt("pad_left", 0)
        pRight = sp.getInt("pad_right", 0)
        cols = sp.getInt("cols", 3)
        rows = sp.getInt("rows", 3)
        row1 = sp.getInt("row1", 1)
        row2 = sp.getInt("row2", 3)
        dotRadius = sp.getInt("dot_radius", 3)
        showSym = sp.getBoolean("show_sym", false)
        showLines = sp.getBoolean("show_lines", false)
        showFails = sp.getBoolean("show_fails", false)
        showDots = sp.getBoolean("show_dots", false)

        paint.style = Paint.Style.STROKE
        paint.color = Color.BLACK
        paint.strokeWidth = 1f

    }


    /**
     * draw the grid of selected grid size
     * looking at settings showSymbol, showLines and showDots to draw the canvas
     */
    override fun onDraw(canvas: Canvas) {
        val cellWidth = ((this.width - pLeft - pRight) / cols).toFloat()
        val cellHeight = ((this.height - pTop - pBot) / rows).toFloat()

        // Row 1 & 2 RectF
        val beginX: Float = pLeft.toFloat()
        val endX: Float = (this.width - pRight).toFloat()
        val row1TopY: Float = pTop.toFloat() + (row1 - 1) * cellHeight
        val row1BottomY: Float = row1TopY + cellHeight
        val row2TopY: Float = pTop.toFloat() + (row2 - 1) * cellHeight
        val row2BottomY: Float = row2TopY + cellHeight
        val rect1 = RectF(beginX, row1TopY, endX, row1BottomY)
        val rect2 = RectF(beginX, row2TopY, endX, row2BottomY)

        // Draw separations of row 1
        if (showLines) {
            // Draw row 1 & 2
            canvas.drawRect(rect1, paint)
            canvas.drawRect(rect2, paint)

            for (i in 1 until cols) {
                // vertical lines row 1
                canvas.drawLine(
                    beginX + i * cellWidth,
                    row1TopY,
                    beginX + i * cellWidth,
                    row1BottomY,
                    paint
                )

                // vertical lines row 1
                canvas.drawLine(
                    beginX + i * cellWidth,
                    row2TopY,
                    beginX + i * cellWidth,
                    row2BottomY,
                    paint
                )
            }
            canvas.drawRect(beginX, pTop.toFloat(), endX, pTop.toFloat() + rows * cellHeight, paint)

        } else {
            // Draw Test area
            canvas.drawRect(beginX, pTop.toFloat(), endX, pTop.toFloat() + rows * cellHeight, paint)
        }

        // show Symbols for row 1 & 2
        if (showSym) {
            paint.strokeWidth = 4f
            val padHor = cellWidth / 5f //padding for Symbols horizontally
            val padVer = cellHeight / 5f //padding for Symbols vertically
            val offsetHor = padHor * 3 / (cols - 1)

            // Draw Symbols
            for (i in 0 until cols) {
                // vertical line row 1
                canvas.drawLine(
                    beginX + padHor + i * offsetHor + i * cellWidth,
                    row1TopY + padVer,
                    beginX + padHor + i * offsetHor + i * cellWidth,
                    row1BottomY - padVer,
                    paint
                )

                // vertical line row 2
                canvas.drawLine(
                    beginX + padHor + i * offsetHor + i * cellWidth,
                    row2TopY + padVer,
                    beginX + padHor + i * offsetHor + i * cellWidth,
                    row2BottomY - padVer,
                    paint
                )

                // horizontal line row 1
                canvas.drawLine(
                    beginX + padHor + i * cellWidth,
                    row1TopY + padVer,
                    beginX + (i + 1) * cellWidth - padHor,
                    row1TopY + padVer,
                    paint
                )

                // horizontal line row 1
                canvas.drawLine(
                    beginX + padHor + i * cellWidth,
                    row2BottomY - padVer,
                    beginX + (i + 1) * cellWidth - padHor,
                    row2BottomY - padVer,
                    paint
                )

            }

        }

        if (showDots) {
            for (i in 0 until cols) {
                // draw dot for row 1
                canvas.drawCircle(
                    beginX + (i + 0.5f) * cellWidth,
                    row1TopY + 0.5f * cellHeight,
                    mmToPixels(dotRadius.toFloat()),
                    paint
                )

                // draw dot for row 2
                canvas.drawCircle(
                    beginX + (i + 0.5f) * cellWidth,
                    row2TopY + 0.5f * cellHeight,
                    mmToPixels(dotRadius.toFloat()),
                    paint
                )
            }
        }



        // Send INIT GRID size message to desktop
        Networker.get().sendMemo(Memo(STRINGS.INTRO, STRINGS.GRID, rows, cols))
        Networker.get().sendMemo(Memo(STRINGS.INTRO, STRINGS.SIZE, MainActivity.sizeX, MainActivity.sizeY))
        Networker.get().sendMemo(Memo(STRINGS.INTRO, STRINGS.SYMBOLS, if (showSym) 1 else 0 , if (showLines) 1 else 0))
        Networker.get().sendMemo(Memo(STRINGS.INTRO, STRINGS.FAILS, if (showFails) 1 else 0 , 0))
        Networker.get().sendMemo(Memo(STRINGS.INTRO, STRINGS.ROWS_ACTIVE, row1, row2))
        Networker.get().sendMemo(Memo(STRINGS.INTRO, STRINGS.DOT_RADIUS_PIXEL, mmToPixels(dotRadius.toFloat()), 0))

    }

    private fun mmToPixels(mm: Float): Float {
        val metrics = resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, mm, metrics)
    }

}