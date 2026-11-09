package io.legado.app.ui.book.read.page.delegate

import android.graphics.Canvas
import android.view.MotionEvent
import io.legado.app.ui.book.read.page.ReadView
import io.legado.app.ui.book.read.page.entities.PageDirection

/**
 * 立即翻页：不截图、不复制页面位图、不创建动画器。
 */
class NoAnimPageDelegate(readView: ReadView) : PageDelegate(readView) {

    override fun onTouch(event: MotionEvent) {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> abortAnim()
            MotionEvent.ACTION_MOVE -> handleMove(event)
            MotionEvent.ACTION_UP -> finishGesture(event, cancelled = false)
            MotionEvent.ACTION_CANCEL -> finishGesture(event, cancelled = true)
        }
    }

    private fun handleMove(event: MotionEvent) {
        val focusX = event.x
        val deltaX = focusX - startX
        if (!isMoved) {
            val deltaY = event.y - startY
            val distance = deltaX * deltaX + deltaY * deltaY
            if (distance <= readView.pageSlopSquare2) return
            val direction = if (deltaX > 0f) PageDirection.PREV else PageDirection.NEXT
            if (!canTurn(direction)) {
                noNext = true
                return
            }
            readView.markReadPositionChanged()
            isMoved = true
            isRunning = true
            noNext = false
            setDirection(direction)
            return
        }

        val direction = when {
            deltaX > 0f -> PageDirection.PREV
            deltaX < 0f -> PageDirection.NEXT
            else -> mDirection
        }
        if (direction != mDirection && canTurn(direction)) {
            setDirection(direction)
        }
    }

    private fun finishGesture(event: MotionEvent, cancelled: Boolean) {
        if (!isMoved) return
        isCancel = cancelled || when (mDirection) {
            PageDirection.NEXT -> event.x >= startX
            PageDirection.PREV -> event.x <= startX
            else -> true
        }
        if (!isCancel) readView.fillPage(mDirection)
        resetState()
    }

    private fun canTurn(direction: PageDirection): Boolean = when (direction) {
        PageDirection.PREV -> hasPrev()
        PageDirection.NEXT -> hasNext()
        else -> false
    }

    private fun turn(direction: PageDirection) {
        abortAnim()
        if (!canTurn(direction)) return
        readView.markReadPositionChanged()
        setDirection(direction)
        readView.fillPage(direction)
        resetState()
    }

    private fun resetState() {
        isMoved = false
        isRunning = false
        isStarted = false
        isCancel = false
        mDirection = PageDirection.NONE
    }

    override fun abortAnim() {
        resetState()
    }

    override fun nextPageByAnim(animationSpeed: Int) {
        turn(PageDirection.NEXT)
    }

    override fun prevPageByAnim(animationSpeed: Int) {
        turn(PageDirection.PREV)
    }

    override fun onDraw(canvas: Canvas) = Unit
}
