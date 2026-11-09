package io.legado.app.ui.font

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.legado.app.R
import io.legado.app.databinding.ItemFontBinding
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FontSelectionInflationTest {

    @Test
    fun itemFontInflatesWithEInkTheme() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        val context = ContextThemeWrapper(appContext, R.style.AppTheme_EInk)
        ItemFontBinding.inflate(
            LayoutInflater.from(context),
            FrameLayout(context),
            false,
        )
    }
}
