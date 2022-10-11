package com.colovaria.image_engine.text

import android.content.Context
import android.graphics.Typeface
import com.colovaria.image_engine.api.resources.*
import com.colovaria.image_engine.api.text.Font

class FontProvider(
    private val context: Context,
) {
    fun provide(font: Font, bold: Boolean, italic: Boolean): Typeface {
        val family = when (font.path) {
            is AssetResourcePath -> Typeface.createFromAsset(context.assets, font.path.path)
            is InternalStorageResourcePath -> Typeface.createFromFile(font.path.path)
            is ExternalStorageResourcePath -> Typeface.createFromFile(font.path.uri.path)
            is AndroidResResourcePath -> context.resources.getFont(font.path.id)
            is WebResourcePath -> error("can't load font from web")
        }

        return Typeface.create(family, when {
            bold && italic -> Typeface.BOLD_ITALIC
            bold && !italic -> Typeface.BOLD
            !bold && italic -> Typeface.ITALIC
            else -> Typeface.NORMAL
        })
    }
}