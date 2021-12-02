package com.example.abdroidgraphics.ui.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.withMatrix
import com.example.abdroidgraphics.constant.bitmapCoordinate

class BlendModeView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleArr: Int = 0
) : View(context, attr, defStyleArr) {
    var canvasMatrix: Matrix? = null
    private var rotationDegree = 0f
    private val firstBitmapPaint = Paint(Paint.FILTER_BITMAP_FLAG).apply {
        isAntiAlias = true
    }
    private var secondBitmapPaint = Paint(Paint.FILTER_BITMAP_FLAG).apply {
        isAntiAlias = true
    }
    var firstBitmap: Bitmap? = null
        set(value) {
            field = value
            invalidate()
        }

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    var secondBitmap: Bitmap? = null
        set(value) {
            field = value
            invalidate()
        }

    private val firstBitmapRect = RectF(
        bitmapCoordinate.unaryMinus(),
        bitmapCoordinate.unaryMinus(),
        bitmapCoordinate - 300,
        bitmapCoordinate - 300
    )
    private val secondBitmapRect = RectF(
        bitmapCoordinate.unaryMinus() + 300,
        bitmapCoordinate.unaryMinus() + 300,
        bitmapCoordinate,
        bitmapCoordinate
    )

    fun setPorterDuffMode(mode: PorterDuff.Mode?) {
        val currentAlpha = secondBitmapPaint.alpha
        secondBitmapPaint = mode?.let {
            secondBitmapPaint.apply {
                xfermode = PorterDuffXfermode(mode)
            }
        } ?: Paint().apply { alpha = currentAlpha }
        invalidate()
    }

    fun setAlpha(alpha: Int) {
        secondBitmapPaint.alpha = alpha
        invalidate()
    }

    fun rotateImageRight() {
        rotationDegree = if(rotationDegree == 270f) {
           0f
        } else rotationDegree + 90f
        invalidate()
    }

    fun rotateImageLeft() {
        rotationDegree = if(rotationDegree == -360f) {
            -90f
        } else rotationDegree - 90f
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.translate(width.div(2f), height.div(2f))
        canvasMatrix = Matrix()
        canvasMatrix?.preRotate(rotationDegree)
        canvasMatrix?.let { matrix ->
            canvas?.withMatrix(matrix) {
                firstBitmap?.let {
                    drawBitmap(it, null, firstBitmapRect, firstBitmapPaint)
                }
                secondBitmap?.let {
                    drawBitmap(it, null, secondBitmapRect, secondBitmapPaint)
                }
            }
        }
    }

}