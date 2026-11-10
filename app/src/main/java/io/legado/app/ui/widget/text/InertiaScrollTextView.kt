package io.legado.app.ui.widget.text

import android.content.Context
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import kotlin.math.min


@Suppress("unused")
open class InertiaScrollTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    //滑动距离的最大边界
    private var mOffsetHeight: Int = 0

    init {
        movementMethod = LinkMovementMethod.getInstance()
    }

    fun atTop(): Boolean {
        return scrollY <= 0
    }

    fun atBottom(): Boolean {
        return scrollY >= mOffsetHeight
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        initOffsetHeight()
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        initOffsetHeight()
    }

    private fun initOffsetHeight() {
        val mLayoutHeight: Int

        //获得内容面板
        val mLayout = layout ?: return
        //获得内容面板的高度
        mLayoutHeight = mLayout.height

        //计算滑动距离的边界
        mOffsetHeight = mLayoutHeight + totalPaddingTop + totalPaddingBottom - measuredHeight
    }

    override fun scrollTo(x: Int, y: Int) {
        super.scrollTo(x, min(y, mOffsetHeight))
    }

}
