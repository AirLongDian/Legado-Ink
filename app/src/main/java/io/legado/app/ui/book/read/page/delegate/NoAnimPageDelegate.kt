package io.legado.app.ui.book.read.page.delegate

import android.view.MotionEvent
import io.legado.app.ui.book.read.page.ReadView
import io.legado.app.ui.book.read.page.entities.PageDirection
import kotlin.math.abs

/**
 * Direct page turns. No screenshot, animation bitmap, or Scroller work runs on input.
 */
class NoAnimPageDelegate(readView: ReadView) : PageDelegate(readView) {

    override fun nextPage() {
        if (hasNext()) {
            readView.fillPage(PageDirection.NEXT)
        }
    }

    override fun prevPage() {
        if (hasPrev()) {
            readView.fillPage(PageDirection.PREV)
        }
    }

    override fun onTouch(event: MotionEvent) {
        if (event.action != MotionEvent.ACTION_UP) return
        val dx = event.x - startX
        val dy = event.y - startY
        if (abs(dx) < readView.slopSquare * 2 || abs(dx) <= abs(dy)) return
        if (dx < 0) nextPage() else prevPage()
    }
}
