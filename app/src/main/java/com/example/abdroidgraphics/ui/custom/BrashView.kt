package com.example.abdroidgraphics.ui.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.graphics.withMatrix
import com.example.abdroidgraphics.constant.bitmapCoordinate
import com.example.abdroidgraphics.constant.brushStrokeWidth
import com.example.abdroidgraphics.constant.imageMaxSize
import kotlin.math.abs

class BrashView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleArr: Int = 0
) : View(context, attr, defStyleArr) {
    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f
    private var currentX = 0f
    private var currentY = 0f
    private val path = Path()
    private lateinit var maskedBitmap: Bitmap
    private val emptyPaint = Paint(Paint.FILTER_BITMAP_FLAG)
    private val bitmapPaint = Paint(Paint.FILTER_BITMAP_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    }
    private lateinit var extraCanvas : Canvas
    private val bitmapRect = RectF(
        bitmapCoordinate.unaryMinus(),
        bitmapCoordinate.unaryMinus(),
        bitmapCoordinate,
        bitmapCoordinate
    )
    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop
    private val pathPaint = Paint(Paint.FILTER_BITMAP_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        color = Color.YELLOW
        isDither = true
        isAntiAlias = true
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.STROKE
        strokeWidth = brushStrokeWidth
    }
    var bitmap: Bitmap? = null
        set(value) {
            field = value
            invalidate()
        }

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            //Just for drawing uncomment this and comment below and remove
            //xfermode from pathPaint
           // drawBitmap(maskedBitmap, 0f,0f, null)
            bitmap?.let {
                canvas.save()
                translate(width.div(2f), height.div(2f))
                drawBitmap(it, null, bitmapRect, emptyPaint)
                canvas.restore()
                drawBitmap(maskedBitmap, 0f,0f, bitmapPaint)
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (::maskedBitmap.isInitialized) maskedBitmap.recycle()
        maskedBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(maskedBitmap).apply {
            drawColor(Color.WHITE)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.apply {
            motionTouchEventX = x
            motionTouchEventY = y

            when (action) {
                MotionEvent.ACTION_DOWN -> touchStart()
                MotionEvent.ACTION_MOVE -> touchMove()
                MotionEvent.ACTION_UP -> touchUp()
            }
        }

        return true
    }

    private fun touchStart() {
        path.reset()
        path.moveTo(motionTouchEventX, motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }

    private fun touchMove() {
        val dx = abs(motionTouchEventX - currentX)
        val dy = abs(motionTouchEventY - currentY)
        if (dx >= touchTolerance || dy >= touchTolerance) {
            path.quadTo(
                currentX,
                currentY,
                (motionTouchEventX + currentX) / 2,
                (motionTouchEventY + currentY) / 2
            )
            currentX = motionTouchEventX
            currentY = motionTouchEventY
            extraCanvas.drawPath(path, pathPaint)
        }
        invalidate()
    }

    private fun touchUp() {
        path.reset()
    }
}