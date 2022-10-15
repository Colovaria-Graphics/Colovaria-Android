package com.colovaria.image_engine.text

import android.text.StaticLayout
import android.util.LruCache
import com.colovaria.geometry.Rect
import com.colovaria.geometry.Size
import com.colovaria.image_engine.api.text.Font
import com.colovaria.image_engine.api.texture.TextInstruction
import kotlin.math.ceil
import kotlin.math.roundToInt

class TextMeasurer(
    private val fontProvider: FontProvider,
    private val cacheSize: Int = 200,
) {
    private val cache = object : LruCache<TextSizeFactors, Size>(cacheSize) {
        fun computeIfAbsent(textInstruction: TextInstruction, canvasSize: Size) : Size {
            val key = TextSizeFactors(textInstruction, canvasSize)
            return get(key) ?: run {
                val size = CanvasTextFactory.createStaticLayout(
                    textInstruction,
                    canvasSize,
                    fontProvider,
                    antiAlias = true  // Some devices ignore `false`, so we must use true for consistency.
                ).let(this@TextMeasurer::boundsByLines).size
                put(key, size)
                size
            }
        }
    }

    fun measure(textInstruction: TextInstruction, canvasSize: Size) : Size {
        return cache.computeIfAbsent(textInstruction, canvasSize)
    }

    private fun boundsByLines(layout: StaticLayout) : Rect {
        val width = (0 until layout.lineCount)
            .maxOf { ceil(layout.getLineWidth(it)) }
            .roundToInt()

        return Rect(0, layout.height, width, 0)
    }

    private data class TextSizeFactors(
        val text: String,
        val size: Float,
        val font: Font,
        val spacing: Float,
        val lineSpacing: Float,
        val maxLines: Int = 0,
        val maxWidth: Float,
        val canvasSize: Size,
    ) {
        constructor(textInstruction: TextInstruction, canvasSize: Size) : this(
            textInstruction.text,
            textInstruction.size,
            textInstruction.font,
            textInstruction.spacing,
            textInstruction.lineSpacing,
            textInstruction.maxLines,
            textInstruction.maxWidth,
            canvasSize,
        )
    }
}