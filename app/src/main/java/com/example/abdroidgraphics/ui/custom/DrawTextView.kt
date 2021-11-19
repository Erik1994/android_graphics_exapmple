package com.example.abdroidgraphics.ui.custom

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import kotlin.math.max
import kotlin.math.min

private const val DEFAULT_TEXT_SIZE = 100f
class DrawTextView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleArr: Int = 0
) : View(context, attr, defStyleArr) {
    private val paint = TextPaint().apply {
        textSize = DEFAULT_TEXT_SIZE
        style = Paint.Style.FILL
        color = Color.WHITE
    }
    private val boundF = RectF()
    private val bound = Rect()
    private val path = Path()
    var drawingText: String = ""
        set(value) {
            field = value.trim()
            invalidate()
        }

    var bitmapBitmap: Bitmap? = null
        set(value) {
            field = value?.apply {
                paint.shader = BitmapShader(this, Shader.TileMode.REPEAT, Shader.TileMode.MIRROR)
            }
            invalidate()
        }

    var drawingTextList: Array<String> = arrayOf()
        set(value) {
            field = value
            invalidate()
        }

    fun setFont(typeface: Typeface) {
        paint.typeface = typeface
        invalidate()
    }

    fun setShader(bitmap: Bitmap) {
        paint.shader = BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        invalidate()
    }

    fun setTextSize(textSize: Float) {
        paint.textSize = textSize + DEFAULT_TEXT_SIZE
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let { can ->
            can.translate(0f, height.div(2f) - height.div(8f))
            var dx = 0f
            var dy = 0f
            paint.getTextBounds(drawingText, 0, drawingText.length, bound)
            if (bound.width() <= width) {
                can.drawText(drawingText, dx, dy, paint)
            } else {
                val textArray = drawingText.split(" ")
                var text = ""
                textArray.forEachIndexed { index, s ->
                    val temp = text + if (index == textArray.lastIndex) {
                        s
                    } else {
                        "$s "
                    }
                    paint.getTextBounds(temp, 0, temp.length, bound)
                    if(bound.width() > width) {
                        can.drawText(text, 0f, dy, paint)
                        dy += bound.height() + 21f
                        text = "$s "
                    } else {
                        text = temp
                    }
                }
                //for last line
                can.drawText(text, 0f, dy, paint)
            }
        }
    }
}