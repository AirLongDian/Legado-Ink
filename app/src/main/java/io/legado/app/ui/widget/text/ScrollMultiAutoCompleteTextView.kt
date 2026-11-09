package io.legado.app.ui.widget.text

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.method.MetaKeyKeyListener
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView
import io.legado.app.lib.theme.accentColor
import io.legado.app.utils.ColorUtils
import kotlin.math.abs
import kotlin.math.min

internal fun IndexOutOfBoundsException.isMissingSelectionSpan(): Boolean =
    message?.contains("setSpan (-1 ... -1)") == true

/**
 * 嵌套惯性滚动 MultiAutoCompleteTextView
 */
open class ScrollMultiAutoCompleteTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatMultiAutoCompleteTextView(context, attrs) {

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
        highlightColor = ColorUtils.adjustAlpha(context.accentColor, 0.35f)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            isLocalePreferredLineHeightForMinimumUsed = false
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        initOffsetHeight()
    }

    override fun onTextChanged(
        text: CharSequence,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        initOffsetHeight()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
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
        val result = try {
            super.onTouchEvent(event)
        } catch (error: IndexOutOfBoundsException) {
            if (!error.isMissingSelectionSpan()) throw error
            MetaKeyKeyListener.resetMetaState(editableText)
            true
        }
        //如果是需要拦截，则再拦截，这个方法会在onScrollChanged方法之后再调用一次
        if (disallowIntercept && lineCount > maxLines) {
            parent.requestDisallowInterceptTouchEvent(true)
        }

        return result
    }

    override fun scrollTo(x: Int, y: Int) {
        super.scrollTo(x, min(y, mOffsetHeight))
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
