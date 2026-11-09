package io.legado.app.ui.book.read.page.delegate

import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import androidx.annotation.CallSuper
import io.legado.app.ui.book.read.page.PageView
import io.legado.app.ui.book.read.page.ReadView
import io.legado.app.ui.book.read.page.entities.PageDirection

/**
 * 墨水屏专用翻页代理基类。
 *
 * 不保留 Scroller、截图或补间动画状态，翻页只在输入结束时立即切换内容。
 */
abstract class PageDelegate(protected val readView: ReadView) {

    protected val context: Context = readView.context
    protected val startX: Float get() = readView.startX
    protected val startY: Float get() = readView.startY
    protected val nextPage: PageView get() = readView.nextPage
    val curPage: PageView get() = readView.curPage
    protected val prevPage: PageView get() = readView.prevPage

    protected var viewWidth: Int = readView.width
    protected var viewHeight: Int = readView.height

    var isMoved = false
    var noNext = true
    var mDirection = PageDirection.NONE
    var isCancel = false
    var isRunning = false
    var isStarted = false

    init {
        curPage.resetPageOffset()
    }

    @CallSuper
    open fun setViewSize(width: Int, height: Int) {
        viewWidth = width
        viewHeight = height
    }

    open fun computeScroll() = Unit
    open fun onScroll() = Unit
    abstract fun abortAnim()
    abstract fun onDraw(canvas: Canvas)
    abstract fun nextPageByAnim(animationSpeed: Int)
    abstract fun prevPageByAnim(animationSpeed: Int)
    abstract fun onTouch(event: MotionEvent)

    open fun keyTurnPage(direction: PageDirection) {
        if (isRunning) return
        when (direction) {
            PageDirection.NEXT -> nextPageByAnim(0)
            PageDirection.PREV -> prevPageByAnim(0)
            else -> Unit
        }
    }

    @CallSuper
    open fun setDirection(direction: PageDirection) {
        mDirection = direction
    }

    fun onDown() {
        isMoved = false
        noNext = false
        isRunning = false
        isStarted = false
        isCancel = false
        setDirection(PageDirection.NONE)
    }

    fun hasPrev(): Boolean {
        if (readView.pageFactory.isRefreshingResources) return false
        return readView.pageFactory.hasPrev()
    }

    fun hasNext(): Boolean {
        if (readView.pageFactory.isRefreshingResources) return false
        val hasNext = readView.pageFactory.hasNext()
        if (!hasNext) readView.callBack.autoPageStop()
        return hasNext
    }

    fun dismissSnackBar() = Unit
    fun postInvalidate() = Unit
    open fun onDestroy() = Unit
}
