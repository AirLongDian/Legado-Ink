package io.legado.app.ui.widget.text

import android.annotation.SuppressLint
import android.content.Context
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.appcompat.widget.AppCompatTextView
import io.legado.app.lib.theme.accentColor
import io.legado.app.utils.ColorUtils
import kotlin.math.abs
import kotlin.math.min


/**
 * 嵌套惯性滚动 TextView
 */
class ScrollTextView(context: Context, attrs: AttributeSet?) :
    AppCompatTextView(context, attrs) {

    var onTextLayoutChanged: (() -> Unit)? = null
    var internalScrollEnabled = true
    var maxMeasuredHeight: Int? = null
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }
    var isMeasuredHeightLimited = false
        private set

    //是否到顶或者到底的标志
    private var disallowIntercept = true

    private val scrollStateIdle = 0
    private val scrollStateDragging = 1
    private var mScrollState = scrollStateIdle
    private var mLastTouchY: Int = 0
    private var mTouchSlop: Int = 0

    //滑动距离的最大边界
    private var mOffsetHeight: Int = 0

    private val gestureDetector = GestureDetector(context,
        object : GestureDetector.SimpleOnGestureListener() {

            override fun onDown(e: MotionEvent): Boolean {
                disallowIntercept = true
                return super.onDown(e)
            }

            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                val y = scrollY + distanceY
                if (y < 0 || y > mOffsetHeight) {
                    disallowIntercept = false
                    //这里触发父布局或祖父布局的滑动事件
                    parent.requestDisallowInterceptTouchEvent(false)
                } else {
                    disallowIntercept = true
                }
                return true
            }

        })

    init {
        val vc = ViewConfiguration.get(context)
        mTouchSlop = vc.scaledTouchSlop
        movementMethod = LinkMovementMethod.getInstance()
        highlightColor = ColorUtils.adjustAlpha(context.accentColor, 0.35f)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        isMeasuredHeightLimited = false
        maxMeasuredHeight?.let { heightLimit ->
            if (measuredHeight > heightLimit) {
                isMeasuredHeightLimited = true
                setMeasuredDimension(measuredWidth, heightLimit)
            }
        }
        initOffsetHeight()
        postTextLayoutChanged()
    }

    override fun onTextChanged(
        text: CharSequence,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        initOffsetHeight()
        postTextLayoutChanged()
    }

    private fun postTextLayoutChanged() {
        val callback = onTextLayoutChanged ?: return
        post {
            if (onTextLayoutChanged === callback) {
                callback()
            }
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (!internalScrollEnabled) {
            return super.dispatchTouchEvent(event)
        }
        if (lineCount > maxLines) {
            gestureDetector.onTouchEvent(event)
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                setScrollState(scrollStateIdle)
                mLastTouchY = (event.y + 0.5f).toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                val y = (event.y + 0.5f).toInt()
                var dy = mLastTouchY - y
                if (mScrollState != scrollStateDragging) {
                    var startScroll = false

                    if (abs(dy) > mTouchSlop) {
                        if (dy > 0) {
                            dy -= mTouchSlop
                        } else {
                            dy += mTouchSlop
                        }
                        startScroll = true
                    }
                    if (startScroll) {
                        setScrollState(scrollStateDragging)
                    }
                }
                if (mScrollState == scrollStateDragging) {
                    mLastTouchY = y
                }
            }
            MotionEvent.ACTION_UP -> {
                setScrollState(scrollStateIdle)
            }
            MotionEvent.ACTION_CANCEL -> setScrollState(scrollStateIdle)
        }
        return super.dispatchTouchEvent(event)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val result = super.onTouchEvent(event)
        //如果是需要拦截，则再拦截，这个方法会在onScrollChanged方法之后再调用一次
        if (internalScrollEnabled && disallowIntercept && lineCount > maxLines) {
            parent.requestDisallowInterceptTouchEvent(true)
        }

        return result
    }

    override fun scrollTo(x: Int, y: Int) {
        super.scrollTo(x, if (internalScrollEnabled) min(y, mOffsetHeight) else 0)
    }

    private fun initOffsetHeight() {
        val mLayoutHeight: Int

        //获得内容面板
        val mLayout = layout ?: return
        //获得内容面板的高度
        mLayoutHeight = mLayout.height
        //获取上内边距
        val paddingTop: Int = totalPaddingTop
        //获取下内边距
        val paddingBottom: Int = totalPaddingBottom

        //获得控件的实际高度
        val mHeight: Int = measuredHeight

        //计算滑动距离的边界
        mOffsetHeight = mLayoutHeight + paddingTop + paddingBottom - mHeight
        if (mOffsetHeight <= 0) {
            scrollTo(0, 0)
        }
    }

    private fun setScrollState(state: Int) {
        if (state == mScrollState) {
            return
        }
        mScrollState = state
    }

}
