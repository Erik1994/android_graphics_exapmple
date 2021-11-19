package com.example.abdroidgraphics

import android.content.Context
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class CompassView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleArr: Int = 0
) : View(context, attr, defStyleArr) {


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measuredWidth = measure(widthMeasureSpec)
        val measuredHeight = measure(heightMeasureSpec)

        val d = min(measuredWidth, measuredHeight)

        setMeasuredDimension(d, d)
    }

    private fun measure(measureSpec: Int): Int {
        // Decode the measurement specifications
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        return when(specMode) {
            MeasureSpec.UNSPECIFIED -> 200
            else -> specSize
        }
    }
}