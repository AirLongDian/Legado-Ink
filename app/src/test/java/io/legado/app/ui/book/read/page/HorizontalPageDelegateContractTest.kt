package io.legado.app.ui.book.read.page

import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class HorizontalPageDelegateContractTest {

    @Test
    fun `ink gestures switch pages without animation or screenshots`() {
        val source = File(
            "src/main/java/io/legado/app/ui/book/read/page/delegate/NoAnimPageDelegate.kt"
        ).readText()

        assertTrue(source.contains("readView.fillPage(mDirection)"))
        assertTrue(source.contains("readView.fillPage(direction)"))
        assertTrue(!source.contains("screenshot"))
        assertTrue(!source.contains("ValueAnimator"))
        assertTrue(!source.contains("Scroller"))
    }
}
