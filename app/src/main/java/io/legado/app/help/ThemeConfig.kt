package io.legado.app.help

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatDelegate
import io.legado.app.constant.EventBus
import io.legado.app.lib.theme.ThemeStore
import io.legado.app.model.BookCover
import io.legado.app.utils.postEvent

/** The only app theme is black on white for e-ink screens. */
object ThemeConfig {
    fun applyDayNight(context: Context) {
        ReadBookConfig.upBg()
        applyTheme(context)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        BookCover.upDefaultCover()
        postEvent(EventBus.RECREATE, "")
    }

    fun applyTheme(context: Context) {
        ThemeStore.editTheme(context)
            .primaryColor(Color.WHITE)
            .accentColor(Color.BLACK)
            .backgroundColor(Color.WHITE)
            .bottomBackground(Color.WHITE)
            .apply()
    }

}
