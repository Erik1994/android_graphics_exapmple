package com.example.abdroidgraphics

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class xMyView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleArr: Int = 0
): View(context, attr, defStyleArr) {
    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var text = "Hello Custom View!!!"
    var canvas: Canvas? = null
    var color: Int? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measuredHeight = measureHeight(heightMeasureSpec)
        val measuredWidth = measureWidth(widthMeasureSpec)

        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    private fun measureHeight(measureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        // Default size in pixels if no limits are specified
        val result: Int = 500

        return when(specMode) {
            MeasureSpec.AT_MOST -> specSize
            MeasureSpec.EXACTLY -> specSize
            else -> result
        }
    }

    private fun measureWidth(measureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        val result: Int = 500

        return when(specMode) {
            MeasureSpec.AT_MOST -> specSize
            MeasureSpec.EXACTLY -> specSize
            else -> result
        }
    }

    override fun onDraw(canvas: Canvas?) {
        this.canvas = canvas
        val height = measuredHeight
        val width = measuredWidth

        val px: Float = width/2f
        val py: Float = height/2f

        val textWidth = paint.measureText(text)
        color?.let {
            canvas?.drawColor(it)
        }
        canvas?.drawText(text, px-textWidth/2, py, paint)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                color = Color.RED
            }
            MotionEvent.ACTION_MOVE -> {
                color = Color.BLUE
            }
        }
        invalidate()
        return true
    }
}