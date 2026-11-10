package io.legado.app.model

import android.annotation.SuppressLint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import io.legado.app.R
import io.legado.app.constant.PreferKey
import io.legado.app.utils.BitmapUtils
import io.legado.app.utils.getPrefBoolean
import io.legado.app.utils.getPrefString
import splitties.init.appCtx

object BookCover {

    var drawBookName = true
        private set
    var drawBookAuthor = true
        private set
    lateinit var defaultDrawable: Drawable
        private set

    init {
        upDefaultCover()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun upDefaultCover() {
        drawBookName = appCtx.getPrefBoolean(PreferKey.coverShowName, true)
        drawBookAuthor = appCtx.getPrefBoolean(PreferKey.coverShowAuthor, true)
        val path = appCtx.getPrefString(PreferKey.defaultCover)
        if (path.isNullOrBlank()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                defaultDrawable = appCtx.resources.getDrawable(R.drawable.image_cover_default, null)
            } else {
                defaultDrawable = appCtx.resources.getDrawable(R.drawable.image_cover_default)
            }
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            defaultDrawable = kotlin.runCatching {
                BitmapDrawable(appCtx.resources, BitmapUtils.decodeBitmap(path, 600, 900))
            }.getOrDefault(appCtx.resources.getDrawable(R.drawable.image_cover_default, null))
        } else {
            defaultDrawable = kotlin.runCatching {
                BitmapDrawable(appCtx.resources, BitmapUtils.decodeBitmap(path, 600, 900))
            }.getOrDefault(appCtx.resources.getDrawable(R.drawable.image_cover_default))
        }
    }

}
