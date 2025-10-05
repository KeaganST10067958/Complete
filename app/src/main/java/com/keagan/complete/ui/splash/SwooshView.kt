package com.keagan.complete.ui.splash

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.keagan.complete.R

class SwooshView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    // Callbacks
    var onProgress: ((posX: Float, posY: Float) -> Unit)? = null
    var onFinish: (() -> Unit)? = null

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.pinkPen)
        style = Paint.Style.STROKE
        strokeWidth = 6f
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    private val fullPath = Path()
    private val drawnPath = Path()
    private lateinit var pathMeasure: PathMeasure
    private var pathLength = 0f
    private var progress = 0f

    private var animator: ValueAnimator? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        buildPath(w, h)
        startAnim()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!::pathMeasure.isInitialized) return

        drawnPath.reset()
        val stop = pathLength * progress
        pathMeasure.getSegment(0f, stop, drawnPath, true)
        canvas.drawPath(drawnPath, paint)

        // Report current tip position
        val pos = FloatArray(2)
        val tan = FloatArray(2)
        pathMeasure.getPosTan(stop, pos, tan)
        onProgress?.invoke(pos[0], pos[1])
    }

    private fun buildPath(w: Int, h: Int) {
        fullPath.reset()

        val pad = 12f
        val ww = (w - pad * 2)
        val hh = (h - pad * 2)

        val startX = pad
        val startY = hh * 0.65f + pad
        val endX = ww + pad
        val endY = hh * 0.35f + pad

        val c1X = ww * 0.35f + pad
        val c1Y = hh + pad
        val c2X = ww * 0.70f + pad
        val c2Y = pad

        fullPath.moveTo(startX, startY)
        fullPath.cubicTo(c1X, c1Y, c2X, c2Y, endX, endY)

        pathMeasure = PathMeasure(fullPath, false)
        pathLength = pathMeasure.length
    }

    private fun startAnim() {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1100L
            interpolator = FastOutSlowInInterpolator()
            addUpdateListener {
                progress = it.animatedValue as Float
                invalidate()
            }
            doOnEnd { onFinish?.invoke() }
            start()
        }
    }
}
