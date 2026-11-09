package io.legado.app.ui.book.read.page.provider

import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class ReviewIconColorSourceTest {

    @Test
    fun `review icon color is persisted and exported`() {
        val source = projectFile(
            "src/main/java/io/legado/app/help/config/ReadBookConfig.kt"
        ).readText().normalizeLines()

        assertTrue(source.contains("var reviewIconColor: Int = 0"))
        assertTrue(source.contains("get() = config.reviewIconColor"))
        assertTrue(source.contains("exportConfig.reviewIconColor = shareConfig.reviewIconColor"))
        assertTrue(source.contains("\"reviewIconColor\" to reviewIconColor"))
    }

    @Test
    fun `provider prefers configured color and keeps theme fallback`() {
        val source = projectFile(
            "src/main/java/io/legado/app/ui/book/read/page/provider/ChapterProvider.kt"
        ).readText().normalizeLines()

        assertTrue(
            Regex(
                """reviewPaint\.color = ReadBookConfig\.reviewIconColor\.takeIf \{ it != 0 \}"""
            ).containsMatchIn(source)
        )
        assertTrue(source.contains("ColorUtils.lightenColor(contentPaint.color)"))
        assertTrue(source.contains("ColorUtils.darkenColor(contentPaint.color)"))
    }

    private fun String.normalizeLines(): String = replace("\r\n", "\n")

    private fun projectFile(pathInApp: String): File {
        return listOf(File(pathInApp), File("app/$pathInApp"))
            .firstOrNull { it.isFile }
            ?: error("Missing project file: $pathInApp")
    }
}
