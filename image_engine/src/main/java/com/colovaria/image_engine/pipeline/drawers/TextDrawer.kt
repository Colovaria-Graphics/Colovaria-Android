package com.colovaria.image_engine.pipeline.drawers

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import com.colovaria.geometry.Size
import com.colovaria.graphics.gles.GTexture
import com.colovaria.image_engine.api.blend.BlenderInstruction
import com.colovaria.image_engine.api.text.TextAlignment
import com.colovaria.image_engine.api.text.TextDirection
import com.colovaria.image_engine.api.texture.TextInstruction
import com.colovaria.image_engine.pipeline.drawers.base.CachedTextureDrawer
import com.colovaria.image_engine.text.CanvasTextFactory
import com.colovaria.image_engine.text.FontProvider
import com.colovaria.image_engine.text.TextMeasurer
import com.colovaria.image_engine.utils.BitmapsLruCache

class TextDrawer(
    private val textMeasurer: TextMeasurer,
    private val fontProvider: FontProvider,
    private val size: Size,
) : CachedTextureDrawer<TextInstruction>() {
    private val bitmapCache = BitmapsLruCache()

    override fun drawInternal(
        instruction: TextInstruction,
        blending: BlenderInstruction
    ): GTexture {
        val targetSize = textMeasurer.measure(instruction, size)
        val bitmap = bitmapCache.computeIfAbsent(targetSize)
        val canvas = Canvas(bitmap).apply { drawColor(Color.TRANSPARENT) }

        val staticLayout = CanvasTextFactory.createStaticLayout(instruction, size, fontProvider)

        val textAsCharArray = staticLayout.text.toString().toCharArray()
        val tempRect = Rect()
        var topMargin = 0
        for (i in (0 until staticLayout.lineCount)) {
            staticLayout.paint.getTextBounds(
                textAsCharArray,
                staticLayout.getLineStart(i),
                staticLayout.getLineEnd(i) - staticLayout.getLineStart(i),
                tempRect
            )

            if (i == 0) {
                topMargin = staticLayout.getLineBaseline(i) + tempRect.top
            }

            val widthMargin = when (instruction.alignment to instruction.direction) {
                TextAlignment.NORMAL to TextDirection.LTR -> 0f
                TextAlignment.NORMAL to TextDirection.RTL -> (targetSize.width - tempRect.width()).toFloat()
                TextAlignment.CENTER to TextDirection.LTR -> (targetSize.width - tempRect.width()) / 2f
                TextAlignment.CENTER to TextDirection.RTL -> (targetSize.width - tempRect.width()) / 2f
                TextAlignment.OPPOSITE to TextDirection.LTR -> (targetSize.width - tempRect.width()).toFloat()
                TextAlignment.OPPOSITE to TextDirection.RTL -> 0f
                else -> 0f
            }

            canvas.drawText(
                textAsCharArray,
                staticLayout.getLineStart(i),
                staticLayout.getLineEnd(i) - staticLayout.getLineStart(i),
                widthMargin - tempRect.left.toFloat(),
                (staticLayout.getLineBaseline(i) + tempRect.bottom - topMargin).toFloat(),
                staticLayout.paint
            )
        }

        return GTexture(bitmap)
    }

    override fun dispose() {
        super.dispose()
        bitmapCache.evictAll()
    }
}