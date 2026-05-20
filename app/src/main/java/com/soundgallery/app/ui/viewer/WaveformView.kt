package com.soundgallery.app.ui.viewer

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.sin

/**
 * A simple animated waveform view. Shows 5 bars that bounce up and down
 * with staggered phase offsets when music is playing.
 */
class WaveformView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    var isPlaying: Boolean = false
        set(value) {
            if (field == value) return
            field = value
            if (value) animator.resume() else animator.pause()
            invalidate()
        }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#B26FFF")
        strokeCap = Paint.Cap.ROUND
    }

    private var phase = 0f
    private val barCount = 5
    private val phaseOffsets = floatArrayOf(0f, 0.4f, 0.8f, 0.2f, 0.6f)

    private val animator = ValueAnimator.ofFloat(0f, (Math.PI * 2).toFloat()).apply {
        duration = 1200
        repeatCount = ValueAnimator.INFINITE
        interpolator = LinearInterpolator()
        addUpdateListener { phase = it.animatedValue as Float; invalidate() }
        start()
        pause()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()
        val barWidth = w / (barCount * 2 - 1)
        paint.strokeWidth = barWidth * 0.7f

        for (i in 0 until barCount) {
            val x = i * barWidth * 2 + barWidth / 2
            val amplitude = if (isPlaying) {
                val raw = sin((phase + phaseOffsets[i] * Math.PI * 2).toDouble()).toFloat()
                ((raw + 1f) / 2f) * (h * 0.65f) + h * 0.2f
            } else {
                h * 0.3f
            }
            val top = (h - amplitude) / 2f
            val bottom = top + amplitude
            canvas.drawLine(x, top, x, bottom, paint)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator.cancel()
    }
}
