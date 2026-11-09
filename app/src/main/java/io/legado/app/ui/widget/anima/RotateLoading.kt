package io.legado.app.ui.widget.anima

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import io.legado.app.R
import io.legado.app.lib.theme.accentColor
import io.legado.app.utils.dpToPx

/**
 * RotateLoading
 * Created by Victor on 2015/4/28.
 */
@Suppress("MemberVisibilityCanBePrivate")
class RotateLoading @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var mPaint: Paint

    private var loadingRectF: RectF? = null
    private var shadowRectF: RectF? = null

    private var thisWidth: Int = 0

    private var shadowPosition: Int = 0

    private var hideMode = GONE

    var isStarted = false
        private set

    var loadingColor: Int = 0
        set(value) {
            field = value
            invalidate()
        }

    init {
        loadingColor = context.accentColor
        thisWidth = DEFAULT_WIDTH.dpToPx()
        shadowPosition = DEFAULT_SHADOW_POSITION.dpToPx()
        if (null != attrs) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RotateLoading)
            loadingColor =
                typedArray.getColor(R.styleable.RotateLoading_loading_color, loadingColor)
            thisWidth = typedArray.getDimensionPixelSize(
                R.styleable.RotateLoading_loading_width,
                DEFAULT_WIDTH.dpToPx()
            )
            shadowPosition = typedArray.getInt(
                R.styleable.RotateLoading_shadow_position,
                DEFAULT_SHADOW_POSITION
            )
            typedArray.recycle()
        }
        mPaint = Paint()
        mPaint.color = loadingColor
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = thisWidth.toFloat()
        mPaint.strokeCap = Paint.Cap.ROUND
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        loadingRectF =
            RectF(
                (2 * thisWidth).toFloat(),
                (2 * thisWidth).toFloat(),
                (w - 2 * thisWidth).toFloat(),
                (h - 2 * thisWidth).toFloat()
            )
        shadowRectF = RectF(
            (2 * thisWidth + shadowPosition).toFloat(),
            (2 * thisWidth + shadowPosition).toFloat(),
            (w - 2 * thisWidth + shadowPosition).toFloat(),
            (h - 2 * thisWidth + shadowPosition).toFloat()
        )
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (!isStarted) {
            return
        }

        mPaint.color = Color.parseColor("#1a000000")
        shadowRectF?.let {
            canvas.drawArc(it, 25f, 260f, false, mPaint)
        }

        mPaint.color = loadingColor
        loadingRectF?.let {
            canvas.drawArc(it, 25f, 260f, false, mPaint)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (visibility == VISIBLE) {
            startInternal()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        isStarted = false
    }

    fun visible() {
        startInternal()
    }

    fun inVisible() {
        hideMode = INVISIBLE
        stopInternal()
    }

    fun gone() {
        hideMode = GONE
        stopInternal()
    }

    private fun startInternal() {
        visibility = VISIBLE
        isStarted = true
        invalidate()
    }

    private fun stopInternal() {
        isStarted = false
        this.visibility = hideMode
        invalidate()
    }

    companion object {
        private const val DEFAULT_WIDTH = 6
        private const val DEFAULT_SHADOW_POSITION = 2
    }

}
