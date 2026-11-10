package io.legado.app.ui.widget.recycler

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

/** List changes and scrolling stop immediately on an e-ink screen. */
open class InkRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    init {
        itemAnimator = null
        overScrollMode = OVER_SCROLL_NEVER
    }

    override fun fling(velocityX: Int, velocityY: Int) = false
}
