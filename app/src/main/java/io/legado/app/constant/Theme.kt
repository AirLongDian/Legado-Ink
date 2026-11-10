package io.legado.app.constant

import io.legado.app.help.AppConfig
import io.legado.app.utils.ColorUtils

enum class Theme {
    Dark, Light, Auto, Transparent, EInk;

    companion object {
        fun getTheme() = EInk

        @Suppress("unused")
        fun fromBackground(backgroundColor: Int) =
            if (ColorUtils.isColorLight(backgroundColor)) Light
            else Dark

    }
}