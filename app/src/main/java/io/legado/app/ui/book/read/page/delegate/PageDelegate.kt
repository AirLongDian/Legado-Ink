package io.legado.app.ui.book.read.page.delegate

import android.view.MotionEvent
import io.legado.app.ui.book.read.page.ReadView
import io.legado.app.ui.book.read.page.entities.PageDirection

/** Shared input and page-boundary handling for direct e-ink page turns. */
abstract class PageDelegate(protected val readView: ReadView) {

    protected val startX: Float get() = readView.startX
    protected val startY: Float get() = readView.startY

    init {
        readView.curPage.resetPageOffset()
    }

    fun hasPrev(): Boolean = readView.pageFactory.hasPrev()

    fun hasNext(): Boolean = readView.pageFactory.hasNext()

    fun keyTurnPage(direction: PageDirection) {
        when (direction) {
            PageDirection.NEXT -> nextPage()
            PageDirection.PREV -> prevPage()
            else -> Unit
        }
    }

    abstract fun onTouch(event: MotionEvent)
    abstract fun nextPage()
    abstract fun prevPage()

    open fun onDestroy() = Unit
}
