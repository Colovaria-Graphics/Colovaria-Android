package com.image_engine.text

import android.content.Context
import android.graphics.Typeface
import com.image_engine.api.text.Font
import com.image_engine.api.resources.*

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
            !bold && !italic -> Typeface.NORMAL
            bold && !italic -> Typeface.BOLD
            !bold && italic -> Typeface.ITALIC
            bold && italic -> Typeface.BOLD_ITALIC
            else -> error("Unknown state")
        })
    }
}